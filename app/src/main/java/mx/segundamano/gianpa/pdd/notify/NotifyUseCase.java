package mx.segundamano.gianpa.pdd.notify;

public class NotifyUseCase {

    private NotificationGateway notificationGateway;

    public NotifyUseCase(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public void timeUp() {
        notificationGateway.showTimeUpNotification();
    }
}
