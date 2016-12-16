package mx.segundamano.gianpa.pdd.notify;

import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;

public class NotifyUseCase {

    private AlarmGateway alarmGateway;
    private NotificationGateway notificationGateway;
    private boolean isBackground;

    public NotifyUseCase(AlarmGateway alarmGateway, NotificationGateway notificationGateway) {
        this.alarmGateway = alarmGateway;
        this.notificationGateway = notificationGateway;
    }

    public void timeUp() {
        if (isBackground) {
            notificationGateway.showTimeUpNotification();
        }
    }

    public void background() {
        isBackground = true;

        if (alarmGateway.isActive()) {
            notificationGateway.showOnGoingNotification(alarmGateway.getEndTimeInMillis());
        }
    }

    public void foreground() {
        isBackground = false;
        notificationGateway.hideAllNotifications();
    }
}
