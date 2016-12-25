package io.github.gianpamx.pdd.breaktimer;

import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.data.Break;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.SettingsGateway;
import io.github.gianpamx.pdd.notify.NotificationGateway;
import io.github.gianpamx.pdd.ticker.Ticker;

public class BreakTimerUseCase implements Alarm.ActiveTimeUpListener, Ticker.TickListener {
    public static final String TAG = BreakTimerUseCase.class.getSimpleName();

    private final BreakTimerRepository breakTimerRepository;
    private final Ticker ticker;
    private final Alarm alarm;
    private final NotificationGateway notificationGateway;
    private final SettingsGateway settingsGateway;

    private UserActiveCallback userActiveCallback;
    private Break activeBreak;

    public BreakTimerUseCase(BreakTimerRepository breakTimerRepository, Ticker ticker, Alarm alarm, NotificationGateway notificationGateway, SettingsGateway settingsGateway) {
        this.breakTimerRepository = breakTimerRepository;
        this.ticker = ticker;
        this.alarm = alarm;
        this.notificationGateway = notificationGateway;
        this.settingsGateway = settingsGateway;
    }

    public void userActive(UserActiveCallback userActiveCallback) {
        this.userActiveCallback = userActiveCallback;

        ticker.resume(this);
        alarm.setActiveTimeUpListener(this);
        notificationGateway.hideAllNotifications();

        activeBreak = breakTimerRepository.findBreak();

        if (activeBreak == null || activeBreak.status == Break.INACTIVE || activeBreak.status == Break.INTERRUPTED) {
            startBreak();
            return;
        }

        if (activeBreak.shouldTimeUp(System.currentTimeMillis())) {
            activeBreak.status = Break.TIME_UP;
            breakTimerRepository.persist(activeBreak);
        }

        if (activeBreak.status == Break.TIME_UP) {
            userActiveCallback.onTick(0, activeBreak.status);
            userActiveCallback.onBreakStatusChanged(activeBreak.status);
            return;
        }

        userActiveCallback.onBreakStatusChanged(Break.ACTIVE);
    }

    public void userInactive() {
        userActiveCallback = null;
        alarm.setActiveTimeUpListener(null);
        ticker.stop();

        if (activeBreak != null) {
            notificationGateway.showBreakOnGoingNotification(activeBreak.endTimeInMillis);
        }
    }

    private void startBreak() {
        long startTimeInMillis = System.currentTimeMillis();

        activeBreak = new Break();
        activeBreak.startTimeInMillis = startTimeInMillis;
        activeBreak.endTimeInMillis = startTimeInMillis + settingsGateway.readBreakTime();
        activeBreak.status = Break.ACTIVE;

        breakTimerRepository.persist(activeBreak);

        alarm.setWakeUpTime(activeBreak.endTimeInMillis, TAG);
        userActiveCallback.onBreakStatusChanged(Break.ACTIVE);
    }

    public void stopBreak(boolean hasUserStopIt) {
        ticker.stop();
        alarm.cancel(TAG);
        activeBreak.status = hasUserStopIt ? Break.INTERRUPTED : Break.INACTIVE;
        breakTimerRepository.persist(activeBreak);
        userActiveCallback.onBreakStatusChanged(activeBreak.status);
        activeBreak = null;
    }

    @Override
    public void onTimeUp() {
        activeBreak = breakTimerRepository.findBreak();
        userActiveCallback.onBreakStatusChanged(activeBreak.status);
    }

    @Override
    public void onTick() {
        userActiveCallback.onTick(activeBreak.getRemainingTime(System.currentTimeMillis()), activeBreak.status);
    }

    public interface UserActiveCallback {
        int BREAK_STATUS_INACTIVE = Break.INACTIVE;
        int BREAK_STATUS_TIMIE_UP = Break.TIME_UP;
        int BREAK_STATUS_ACTIVE = Break.ACTIVE;
        int BREAK_STATUS_INTERRUPTED = Break.INTERRUPTED;

        void onTick(long remainingTime, int status);

        void onBreakStatusChanged(int status);
    }
}
