package mx.segundamano.gianpa.pdd.timer;

import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;
import mx.segundamano.gianpa.pdd.data.Pomodoro;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;

public class TimerUseCase implements AlarmGateway.TickListener {

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

                if (activePomodoro != null) {
                    alarmGateway.resumeTicker(TimerUseCase.this);
                    userActiveCallback.onPomodoroStatusChanged(Pomodoro.ACTIVE);
                } else {
                    userActiveCallback.onTick(TimeConstants.DEFAULT_POMODORO_TIME);
                }
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

    public void stopPomodoro() {
        activePomodoro.status = Pomodoro.INTERRUPTED;
        pomodoroRepository.persist(activePomodoro, new PomodoroRepository.Callback<Pomodoro>() {
            @Override
            public void onSuccess(Pomodoro result) {
                alarmGateway.stopTicker();
                userActiveCallback.onPomodoroStatusChanged(Pomodoro.INTERRUPTED);
            }
        });
    }

    public interface UserActiveCallback {
        int POMODORO_STATUS_ACTIVE = 1;
        int POMODORO_STATUS_INTERRUPTED = 2;

        void onTick(long remainingTime);

        void onPomodoroStatusChanged(int status);
    }
}
