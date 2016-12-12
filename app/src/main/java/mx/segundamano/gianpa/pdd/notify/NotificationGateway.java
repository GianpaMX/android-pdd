package mx.segundamano.gianpa.pdd.notify;

public interface NotificationGateway {
    void showOnGoingNotification(long when);

    void showTimeUpNotification();

    void hideAllNotifications();
}
