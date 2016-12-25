package io.github.gianpamx.pdd.pomodorotimer;

import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.data.Break;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.Pomodoro;
import io.github.gianpamx.pdd.data.PomodoroRepository;
import io.github.gianpamx.pdd.data.SettingsGateway;
import io.github.gianpamx.pdd.notify.NotificationGateway;
import io.github.gianpamx.pdd.ticker.Ticker;

public class PomodoroTimerUseCase implements Ticker.TickListener, Alarm.ActiveTimeUpListener {

    public static final String TAG = PomodoroTimerUseCase.class.getSimpleName();

    public static final int ERROR_ACTION_KEEP_AND_DISCARD_TIME = 1;
    public static final int ERROR_ACTION_DISCARD = 2;

    private static final String[] STOP_REASONS = {"Boss interrupted", "Colleage interrupted", "Email", "Phone call", "Web browsing"};

    private final PomodoroRepository pomodoroRepository;
    private final Ticker ticker;
    private final Alarm alarm;
    private final NotificationGateway notificationGateway;
    private final SettingsGateway settingsGateway;
    private final BreakTimerRepository breakTimerRepository;

    private UserActiveCallback userActiveCallback;
    private Pomodoro activePomodoro;

    public PomodoroTimerUseCase(PomodoroRepository pomodoroRepository, Ticker ticker, Alarm alarm, NotificationGateway notificationGateway, SettingsGateway settingsGateway, BreakTimerRepository breakTimerRepository) {
        this.pomodoroRepository = pomodoroRepository;
        this.ticker = ticker;
        this.alarm = alarm;
        this.notificationGateway = notificationGateway;
        this.settingsGateway = settingsGateway;
        this.breakTimerRepository = breakTimerRepository;
    }

    public void userActive(final UserActiveCallback userActiveCallback) {
        this.userActiveCallback = userActiveCallback;
        alarm.setActiveTimeUpListener(this);
        notificationGateway.hideAllNotifications();

        pomodoroRepository.findTimeUpPomodoro(findTimeUpPomodoroCallback);
    }

    private final PomodoroRepository.Callback<Pomodoro> findTimeUpPomodoroCallback = new PomodoroRepository.Callback<Pomodoro>() {
        @Override
        public void onSuccess(Pomodoro result) {
            if (result == null) {
                pomodoroRepository.findActivePomodoro(findActivePomodoroCallback);
                return;
            }

            activePomodoro = result;

            ticker.stop();
            userActiveCallback.onTick(0);
            userActiveCallback.onTimeUp();
        }
    };

    private PomodoroRepository.Callback<Pomodoro> findActivePomodoroCallback = new PomodoroRepository.Callback<Pomodoro>() {
        @Override
        public void onSuccess(Pomodoro result) {
            activePomodoro = result;

            if (activePomodoro == null) {
                userActiveCallback.onTick(settingsGateway.readPomodoroTime());

                if(breakTimerRepository.findBreak().status == Break.INTERRUPTED) {
                    startPomodoro();
                }

                return;
            }

            if (activePomodoro.endTimeInMillis < System.currentTimeMillis()) {
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.ERROR);
                return;
            }


            ticker.resume(PomodoroTimerUseCase.this);
            userActiveCallback.onPomodoroStatusChanged(Pomodoro.ACTIVE);
        }
    };

    public void userInactive() {
        userActiveCallback = null;
        alarm.setActiveTimeUpListener(null);
        ticker.stop();

        if (activePomodoro != null) {
            notificationGateway.showOnGoingNotification(activePomodoro.endTimeInMillis);
        }
    }

    @Override
    public void onTick() {
        userActiveCallback.onTick(activePomodoro.getRemainingTime(System.currentTimeMillis()));
    }

    public void startPomodoro() {
        long startTimeInMillis = System.currentTimeMillis();

        Pomodoro pomodoro = Pomodoro.Builder()
                .setStartTimeInMillis(startTimeInMillis)
                .setEndTimeInMillis(startTimeInMillis + settingsGateway.readPomodoroTime())
                .setStatus(Pomodoro.ACTIVE)
                .build();

        pomodoroRepository.persist(pomodoro, new PomodoroRepository.Callback<Pomodoro>() {
            @Override
            public void onSuccess(Pomodoro result) {
                activePomodoro = result;
                alarm.setWakeUpTime(activePomodoro.endTimeInMillis, TAG);
                ticker.resume(PomodoroTimerUseCase.this);
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.ACTIVE);

                Break activeBreak = breakTimerRepository.findBreak();
                activeBreak.status = Break.INACTIVE;
                breakTimerRepository.persist(activeBreak);
            }
        });
    }

    public void stopPomodoro(StopPomodoroCallback callback) {
        callback.onStopReasonsReady(STOP_REASONS);
    }

    public void stopPomodoro(int stopReason) {
        activePomodoro.status = Pomodoro.INTERRUPTED;
        activePomodoro.stopReason = stopReason;
        pomodoroRepository.persist(activePomodoro, new PomodoroRepository.Callback<Pomodoro>() {
            @Override
            public void onSuccess(Pomodoro result) {
                ticker.stop();
                alarm.cancel(TAG);
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.INTERRUPTED);
                userActiveCallback.onTick(settingsGateway.readPomodoroTime());
                activePomodoro = null;
            }
        });
    }

    public void stopPomodoroOnError(int errorAction) {
        activePomodoro.status = Pomodoro.COMPLETE;

        if (errorAction == ERROR_ACTION_DISCARD) activePomodoro.status = Pomodoro.DISCARDED;
        if (errorAction == ERROR_ACTION_KEEP_AND_DISCARD_TIME)
            activePomodoro.endTimeInMillis = activePomodoro.startTimeInMillis;

        pomodoroRepository.persist(activePomodoro, new PomodoroRepository.Callback<Pomodoro>() {
            @Override
            public void onSuccess(Pomodoro result) {
                ticker.stop();
                userActiveCallback.onPomodoroStatusChanged(activePomodoro.status);
                userActiveCallback.onTick(settingsGateway.readPomodoroTime());
                activePomodoro = null;
            }
        });
    }

    @Override
    public void onTimeUp() {
        ticker.stop();
        userActiveCallback.onTimeUp();
    }

    public void complete(boolean isComplete) {
        activePomodoro.status = isComplete ? Pomodoro.COMPLETE : Pomodoro.DISCARDED;

        pomodoroRepository.persist(activePomodoro, new PomodoroRepository.Callback<Pomodoro>() {
            @Override
            public void onSuccess(Pomodoro result) {
                userActiveCallback.onPomodoroStatusChanged(activePomodoro.status);
                userActiveCallback.onTick(settingsGateway.readPomodoroTime());
                activePomodoro = null;
            }
        });
    }

    public interface UserActiveCallback {
        int POMODORO_STATUS_ACTIVE = Pomodoro.ACTIVE;
        int POMODORO_STATUS_INTERRUPTED = Pomodoro.INTERRUPTED;
        int POMODORO_STATUS_ERROR = Pomodoro.ERROR;
        int POMODORO_STATUS_COMPLETE = Pomodoro.COMPLETE;
        int POMODORO_STATUS_DISCARDED = Pomodoro.DISCARDED;

        void onTick(long remainingTime);

        void onPomodoroStatusChanged(int status);

        void onTimeUp();
    }

    public interface StopPomodoroCallback {
        void onStopReasonsReady(String[] stopReasons);
    }
}
