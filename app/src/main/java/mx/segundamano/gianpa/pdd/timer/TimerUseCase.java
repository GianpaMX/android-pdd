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

                    userActiveCallback.onPomodoroResume();
                }
            }
        });
    }

    public void userInactive() {
        this.userActiveCallback = null;

        if(activePomodoro != null) {
            alarmGateway.stopTicker();
        }
    }

    @Override
    public void onTick() {
        userActiveCallback.onTick(activePomodoro.getRemainingTime(System.currentTimeMillis()));
    }

    public interface UserActiveCallback {

        void onTick(long remainingTime);

        void onPomodoroResume();
    }
}
