package mx.segundamano.gianpa.pdd.wakeup;

import mx.segundamano.gianpa.pdd.alarm.Alarm;
import mx.segundamano.gianpa.pdd.data.BreakTimerRepository;
import mx.segundamano.gianpa.pdd.data.Pomodoro;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;
import mx.segundamano.gianpa.pdd.pomodorotimer.PomodoroTimerUseCase;

public class WakeupUseCase {
    private Alarm alarm;
    private PomodoroRepository pomodoroRepository;
    private final BreakTimerRepository breakTimerRepository;

    private Callback callback;
    private String tag;

    public WakeupUseCase(Alarm alarm, PomodoroRepository pomodoroRepository, BreakTimerRepository breakTimerRepository) {
        this.alarm = alarm;
        this.pomodoroRepository = pomodoroRepository;
        this.breakTimerRepository = breakTimerRepository;
    }

    public void timeUp(String tag, final Callback callback) {
        this.callback = callback;
        this.tag = tag;

        if (PomodoroTimerUseCase.TAG.equals(tag)) {
            pomodoroRepository.findActivePomodoro(onFindActivePomodoro);
        } else {
            callback.onTimeUp(tag);
        }
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
            executeCallback();
        }
    };

    public void executeCallback() {
        Alarm.ActiveTimeUpListener activeTimeUpListener = alarm.getActiveTimeUpListener();
        if (activeTimeUpListener != null) {
            callback.ignore();
            activeTimeUpListener.onTimeUp();
            return;
        }

        callback.onTimeUp(tag);
    }

    public interface Callback {
        void onTimeUp(String tag);

        void ignore();
    }
}
