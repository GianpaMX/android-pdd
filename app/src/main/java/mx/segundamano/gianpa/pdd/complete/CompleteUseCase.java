package mx.segundamano.gianpa.pdd.complete;

import mx.segundamano.gianpa.pdd.data.Pomodoro;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;
import mx.segundamano.gianpa.pdd.notify.NotificationGateway;

public class CompleteUseCase {

    private NotificationGateway notificationGateway;
    private PomodoroRepository pomodoroRepository;

    public CompleteUseCase(PomodoroRepository pomodoroRepository, NotificationGateway notificationGateway) {
        this.pomodoroRepository = pomodoroRepository;
        this.notificationGateway = notificationGateway;
    }

    public void complete(boolean isComplete) {
        Pomodoro activePomodoro = pomodoroRepository.findTimeUpPomodoro();
        if (activePomodoro != null) {
            activePomodoro.status = isComplete ? Pomodoro.COMPLETE : Pomodoro.DISCARDED;
            pomodoroRepository.persist(activePomodoro);
        }

        notificationGateway.hideAllNotifications();
    }
}
