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
import mx.segundamano.gianpa.pdd.timer.TimerActivity;

public class AlarmReceiver extends BroadcastReceiver implements WakeupUseCase.Callback {
    @Inject
    public WakeupUseCase wakeupUseCase;

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
        if (getAndroidApp().getCurrentActivity() == null) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent resultIntent = new Intent(context, TimerActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(TimerActivity.class);
            stackBuilder.addNextIntent(resultIntent);

            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("My notification")
                    .setContentText("Hello World!")
                    .setContentIntent(resultPendingIntent).build();

            notificationManager.notify(0, notification);
        }

        WakeLocker.release();
    }
}
