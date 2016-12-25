package io.github.gianpamx.pdd.notify;

import io.github.gianpamx.pdd.breaktimer.BreakTimerUseCase;
import io.github.gianpamx.pdd.pomodorotimer.PomodoroTimerUseCase;

public class NotifyUseCase {

    private NotificationGateway notificationGateway;

    public NotifyUseCase(NotificationGateway notificationGateway) {
        this.notificationGateway = notificationGateway;
    }

    public void timeUp(String tag) {
        if (PomodoroTimerUseCase.TAG.equals(tag)) {
            notificationGateway.showPomodoroTimeUpNotification();
        } else if (BreakTimerUseCase.TAG.equals(tag)) {
            notificationGateway.showBreakTimeUpNotification();
        }
    }
}
