package mx.segundamano.gianpa.pdd.notify;

public interface NotificationGateway {
    void showOnGoingNotification(long when);

    void showBreakOnGoingNotification(long when);

    void showPomodoroTimeUpNotification();

    void showBreakTimeUpNotification();

    void hideAllNotifications();
}
