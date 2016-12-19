package mx.segundamano.gianpa.pdd.wakeup;

import mx.segundamano.gianpa.pdd.alarm.Alarm;
import mx.segundamano.gianpa.pdd.data.Pomodoro;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;

public class WakeupUseCase {
    private Alarm alarm;
    private PomodoroRepository pomodoroRepository;

    private Callback callback;

    public WakeupUseCase(Alarm alarm, PomodoroRepository pomodoroRepository) {
        this.alarm = alarm;
        this.pomodoroRepository = pomodoroRepository;
    }

    public void timeUp(final Callback callback) {
        this.callback = callback;
        pomodoroRepository.findActivePomodoro(onFindActivePomodoro);
    }

    private PomodoroRepository.Callback<Pomodoro> onFindActivePomodoro = new PomodoroRepository.Callback<Pomodoro>() {
        @Override
        public void onSuccess(Pomodoro result) {
            result.status = Pomodoro.TIME_UP;
            pomodoroRepository.persist(result, onPersistActivePomodoro);
        }
    };

    private PomodoroRepository.Callback<Pomodoro> onPersistActivePomodoro = new PomodoroRepository.Callback<Pomodoro>() {
        @Override
        public void onSuccess(Pomodoro result) {
            Alarm.ActiveTimeUpListener activeTimeUpListener = alarm.getActiveTimeUpListener();
            if (activeTimeUpListener != null) {
                callback.ignore();
                activeTimeUpListener.onTimeUp();
                return;
            }

            callback.onTimeUp();
        }
    };

    public interface Callback {
        void onTimeUp();

        void ignore();
    }
}
