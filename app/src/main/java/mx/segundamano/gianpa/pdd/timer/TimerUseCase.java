package mx.segundamano.gianpa.pdd.timer;

import mx.segundamano.gianpa.pdd.alarm.Alarm;
import mx.segundamano.gianpa.pdd.data.Pomodoro;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;
import mx.segundamano.gianpa.pdd.ticker.Ticker;

import static mx.segundamano.gianpa.pdd.timer.TimerUseCase.UserActiveCallback.POMODORO_STATUS_COMPLETE;

public class TimerUseCase implements Ticker.TickListener, Alarm.ActiveTimeUpListener {

    public static final int ERROR_ACTION_KEEP_AND_DISCARD_TIME = 1;
    public static final int ERROR_ACTION_DISCARD = 2;

    private static final String[] STOP_REASONS = {"Boss interrupted", "Colleage interrupted", "Email", "Phone call", "Web browsing"};

    private PomodoroRepository pomodoroRepository;
    private UserActiveCallback userActiveCallback;
    private Pomodoro activePomodoro;
    private Ticker ticker;
    private Alarm alarm;

    public TimerUseCase(PomodoroRepository pomodoroRepository, Ticker ticker, Alarm alarm) {
        this.pomodoroRepository = pomodoroRepository;
        this.ticker = ticker;
        this.alarm = alarm;
    }

    public void userActive(final UserActiveCallback userActiveCallback) {
        this.userActiveCallback = userActiveCallback;
        alarm.setActiveTimeUpListener(TimerUseCase.this);

        this.pomodoroRepository.findActivePomodoro(new PomodoroRepository.Callback<Pomodoro>() {
            @Override
            public void onSuccess(Pomodoro result) {
                activePomodoro = result;

                if (activePomodoro == null) {
                    userActiveCallback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
                    return;
                }

                if (activePomodoro.endTimeInMillis < System.currentTimeMillis()) {
                    userActiveCallback.onPomodoroStatusChanged(Pomodoro.ERROR);
                    return;
                }


                ticker.resume(TimerUseCase.this);
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.ACTIVE);
            }
        });
    }

    public void userInactive() {
        userActiveCallback = null;
        alarm.setActiveTimeUpListener(null);
        ticker.stop();
    }

    @Override
    public void onTick() {
        userActiveCallback.onTick(activePomodoro.getRemainingTime(System.currentTimeMillis()));
    }

    public void startPomodoro() {
        long startTimeInMillis = System.currentTimeMillis();

        Pomodoro pomodoro = Pomodoro.Builder()
                .setStartTimeInMillis(startTimeInMillis)
                .setEndTimeInMillis(TimeConstants.sum(startTimeInMillis, TimeConstants.DEFAULT_POMODORO_TIME))
                .setStatus(Pomodoro.ACTIVE)
                .build();

        pomodoroRepository.persist(pomodoro, new PomodoroRepository.Callback<Pomodoro>() {
            @Override
            public void onSuccess(Pomodoro result) {
                activePomodoro = result;
                alarm.setWakeUpTime(activePomodoro.endTimeInMillis);
                ticker.resume(TimerUseCase.this);
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.ACTIVE);
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
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.INTERRUPTED);
                userActiveCallback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
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
                userActiveCallback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
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
                userActiveCallback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
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
