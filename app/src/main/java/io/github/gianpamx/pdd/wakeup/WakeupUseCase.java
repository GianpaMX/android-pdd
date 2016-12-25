package io.github.gianpamx.pdd.wakeup;

import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.breaktimer.BreakTimerUseCase;
import io.github.gianpamx.pdd.data.Break;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.Pomodoro;
import io.github.gianpamx.pdd.data.PomodoroRepository;
import io.github.gianpamx.pdd.pomodorotimer.PomodoroTimerUseCase;

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
        } else if (BreakTimerUseCase.TAG.equals(tag)) {
            Break activeBreak = breakTimerRepository.findBreak();
            activeBreak.status = Break.TIME_UP;
            breakTimerRepository.persist(activeBreak);
            executeCallback();
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
