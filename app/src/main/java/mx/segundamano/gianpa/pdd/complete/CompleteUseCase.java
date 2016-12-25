package mx.segundamano.gianpa.pdd.complete;

import mx.segundamano.gianpa.pdd.alarm.Alarm;
import mx.segundamano.gianpa.pdd.breaktimer.BreakTimerUseCase;
import mx.segundamano.gianpa.pdd.data.Break;
import mx.segundamano.gianpa.pdd.data.BreakTimerRepository;
import mx.segundamano.gianpa.pdd.data.Pomodoro;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;
import mx.segundamano.gianpa.pdd.data.SettingsGateway;
import mx.segundamano.gianpa.pdd.notify.NotificationGateway;

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
}
