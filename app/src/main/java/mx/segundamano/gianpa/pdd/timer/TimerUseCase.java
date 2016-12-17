package mx.segundamano.gianpa.pdd.timer;

import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;
import mx.segundamano.gianpa.pdd.data.Pomodoro;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;

public class TimerUseCase implements AlarmGateway.TickListener {

    public static final int ERROR_ACTION_KEEP_AND_DISCARD_TIME = 1;
    public static final int ERROR_ACTION_DISCARD = 2;

    private static final String[] STOP_REASONS = {"Boss interrupted", "Colleage interrupted", "Email", "Phone call", "Web browsing"};

    private PomodoroRepository pomodoroRepository;
    private UserActiveCallback userActiveCallback;
    private Pomodoro activePomodoro;
    private AlarmGateway alarmGateway;

    public TimerUseCase(PomodoroRepository pomodoroRepository, AlarmGateway alarmGateway) {
        this.pomodoroRepository = pomodoroRepository;
        this.alarmGateway = alarmGateway;
    }

    public void userActive(final UserActiveCallback userActiveCallback) {
        this.userActiveCallback = userActiveCallback;

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

                alarmGateway.resumeTicker(TimerUseCase.this);
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.ACTIVE);
            }
        });
    }

    public void userInactive() {
        this.userActiveCallback = null;

        if (activePomodoro != null) {
            alarmGateway.stopTicker();
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
                .setEndTimeInMillis(TimeConstants.sum(startTimeInMillis, TimeConstants.DEFAULT_POMODORO_TIME))
                .setStatus(Pomodoro.ACTIVE)
                .build();

        pomodoroRepository.persist(pomodoro, new PomodoroRepository.Callback<Pomodoro>() {
            @Override
            public void onSuccess(Pomodoro result) {
                activePomodoro = result;
                alarmGateway.resumeTicker(TimerUseCase.this);
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
                alarmGateway.stopTicker();
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.INTERRUPTED);
                userActiveCallback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
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
                alarmGateway.stopTicker();
                userActiveCallback.onPomodoroStatusChanged(activePomodoro.status);
                userActiveCallback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
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
    }

    public interface StopPomodoroCallback {
        void onStopReasonsReady(String[] stopReasons);
    }
}
