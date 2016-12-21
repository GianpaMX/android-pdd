package mx.segundamano.gianpa.pdd.notify;

import mx.segundamano.gianpa.pdd.pomodorotimer.PomodoroTimerUseCase;

public class NotifyUseCase {

    private NotificationGateway notificationGateway;

    public NotifyUseCase(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public void timeUp(String tag) {
        if (PomodoroTimerUseCase.TAG.equals(tag)) {
            notificationGateway.showPomodoroTimeUpNotification();
        }
    }
}
