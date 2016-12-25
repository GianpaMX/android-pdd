package io.github.gianpamx.pdd.complete;

import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.breaktimer.BreakTimerUseCase;
import io.github.gianpamx.pdd.data.Break;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.Pomodoro;
import io.github.gianpamx.pdd.data.PomodoroRepository;
import io.github.gianpamx.pdd.data.SettingsGateway;
import io.github.gianpamx.pdd.notify.NotificationGateway;
import io.github.gianpamx.pdd.pomodorotimer.PomodoroTimerUseCase;

public class CompleteUseCase {

    private final NotificationGateway notificationGateway;
    private final PomodoroRepository pomodoroRepository;
    private final SettingsGateway settingsGateway;
    private final BreakTimerRepository breakTimerRepository;
    private final Alarm alarm;

    public CompleteUseCase(PomodoroRepository pomodoroRepository, NotificationGateway notificationGateway, SettingsGateway settingsGateway, BreakTimerRepository breakTimerRepository, Alarm alarm) {
        this.pomodoroRepository = pomodoroRepository;
        this.notificationGateway = notificationGateway;
        this.settingsGateway = settingsGateway;
        this.breakTimerRepository = breakTimerRepository;
        this.alarm = alarm;
    }

    public void complete(boolean isComplete) {
        completePomodoro(isComplete);

        if (isComplete) {
            startBreak();
        }
    }

    public void startBreak() {
        long startTimeInMillis = System.currentTimeMillis();

        Break activeBreak = new Break();
        activeBreak.startTimeInMillis = startTimeInMillis;
        activeBreak.endTimeInMillis = startTimeInMillis + settingsGateway.readBreakTime();
        activeBreak.status = Break.ACTIVE;

        breakTimerRepository.persist(activeBreak);

        alarm.setWakeUpTime(activeBreak.endTimeInMillis, BreakTimerUseCase.TAG);
        alarm.setActiveTimeUpListener(null);
        notificationGateway.showBreakOnGoingNotification(activeBreak.endTimeInMillis);
    }

    public void completePomodoro(boolean isComplete) {
        Pomodoro activePomodoro = pomodoroRepository.findTimeUpPomodoro();
        if (activePomodoro != null) {
            activePomodoro.status = isComplete ? Pomodoro.COMPLETE : Pomodoro.DISCARDED;
            pomodoroRepository.persist(activePomodoro);
        }

        notificationGateway.hideAllNotifications();
    }

    public void completeBreakAndStartPomodoro() {
        completeBreak();

        startPomodoro();
    }

    public void completeBreak() {
        Break activeBreak = breakTimerRepository.findBreak();
        activeBreak.status = Break.INACTIVE;
        breakTimerRepository.persist(activeBreak);
        alarm.cancel(BreakTimerUseCase.TAG);
    }

    public void startPomodoro() {
        long startTimeInMillis = System.currentTimeMillis();

        Pomodoro activePomodoro = Pomodoro.Builder()
                .setStartTimeInMillis(startTimeInMillis)
                .setEndTimeInMillis(startTimeInMillis + settingsGateway.readPomodoroTime())
                .setStatus(Pomodoro.ACTIVE)
                .build();

        pomodoroRepository.persist(activePomodoro);
        alarm.setWakeUpTime(activePomodoro.endTimeInMillis, PomodoroTimerUseCase.TAG);
        alarm.setActiveTimeUpListener(null);
        notificationGateway.showOnGoingNotification(activePomodoro.endTimeInMillis);
    }
}
