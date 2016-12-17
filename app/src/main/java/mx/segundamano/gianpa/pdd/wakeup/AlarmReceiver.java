package mx.segundamano.gianpa.pdd.wakeup;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import javax.inject.Inject;

import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.di.AndroidAppComponent;
import mx.segundamano.gianpa.pdd.notify.NotifyUseCase;
import mx.segundamano.gianpa.pdd.timer.TimerActivity;

public class AlarmReceiver extends BroadcastReceiver implements WakeupUseCase.Callback {
    @Inject
    public WakeupUseCase wakeupUseCase;

//    @Inject
//    public NotifyUseCase notifyUseCase;

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLocker.acquire(context);
        this.context = context;

        inject(this);

        wakeupUseCase.timeUp(this);
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
    public void onTimeUp() {
//        notifyUseCase.timeUp();
        WakeLocker.release();
    }

    @Override
    public void ignore() {
        WakeLocker.release();
    }
}
