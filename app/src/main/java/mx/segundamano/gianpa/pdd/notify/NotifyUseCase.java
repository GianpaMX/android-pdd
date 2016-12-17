package mx.segundamano.gianpa.pdd.notify;

import mx.segundamano.gianpa.pdd.ticker.Ticker;

public class NotifyUseCase {

    private Ticker ticker;
    private NotificationGateway notificationGateway;

    public NotifyUseCase(Ticker ticker, NotificationGateway notificationGateway) {
        this.ticker = ticker;
        this.notificationGateway = notificationGateway;
    }

    public void timeUp() {
        notificationGateway.showTimeUpNotification();
    }

    public void foreground() {
        notificationGateway.hideAllNotifications();
    }
}
