package mx.segundamano.gianpa.pdd.notify;

import mx.segundamano.gianpa.pdd.ticker.Ticker;

public class NotifyUseCase {

    private Ticker ticker;
    private NotificationGateway notificationGateway;
    private boolean isBackground;

    public NotifyUseCase(Ticker ticker, NotificationGateway notificationGateway) {
        this.ticker = ticker;
        this.notificationGateway = notificationGateway;
    }

    public void timeUp() {
        if (isBackground) {
            notificationGateway.showTimeUpNotification();
        }
    }

    public void background() {
        isBackground = true;
    }

    public void foreground() {
        isBackground = false;
        notificationGateway.hideAllNotifications();
    }
}
