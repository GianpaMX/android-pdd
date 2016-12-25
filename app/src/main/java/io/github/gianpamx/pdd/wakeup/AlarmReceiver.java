package io.github.gianpamx.pdd.wakeup;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import io.github.gianpamx.pdd.AndroidApp;
import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.di.AndroidAppComponent;
import io.github.gianpamx.pdd.notify.NotifyUseCase;

public class AlarmReceiver extends BroadcastReceiver implements WakeupUseCase.Callback {
    @Inject
    public WakeupUseCase wakeupUseCase;

    @Inject
    public NotifyUseCase notifyUseCase;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLocker.acquire(context);
        this.context = context;

        inject(this);

        wakeupUseCase.timeUp(intent.getStringExtra(Alarm.EXTRA_TAG), this);
    }

    private void inject(AlarmReceiver alarmReceiver) {
        getAndroidAppComponent().inject(alarmReceiver);
    }

    private AndroidAppComponent getAndroidAppComponent() {
        return getAndroidApp().getAndroidAppComponent();
    }

    private AndroidApp getAndroidApp() {
        return (AndroidApp) context.getApplicationContext();
    }

    @Override
    public void onTimeUp(String tag) {
        notifyUseCase.timeUp(tag);
        WakeLocker.release();
    }

    @Override
    public void ignore() {
        WakeLocker.release();
    }
}
