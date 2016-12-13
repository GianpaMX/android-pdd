package mx.segundamano.gianpa.pdd.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.timer.TimerActivity;

public class NotificationGatewayImpl implements NotificationGateway {
    private Context context;
    private NotificationManager notificationManager;
    private Notification notification;

    public NotificationGatewayImpl(Context context, NotificationManager notificationManager) {
        this.context = context;
        this.notificationManager = notificationManager;
    }

    @Override
    public void showOnGoingNotification(long when) {
        PendingIntent resultPendingIntent = getPendingIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Active Pomodor")
                    .setContentText("Time until break")
                    .setContentIntent(resultPendingIntent)
                    .setOngoing(true)
                    .setTicker("Time until break")
                    .setWhen(when)
                    .setUsesChronometer(true)
                    .setChronometerCountDown(true)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Active Pomodor")
                    .setContentText("Time until break")
                    .setContentIntent(resultPendingIntent)
                    .setOngoing(true)
                    .setTicker("Time until break")
                    .setWhen(when)
                    .setUsesChronometer(true)
                    .build();

        }
        notificationManager.notify(1, this.notification);
    }

    @Override
    public void showTimeUpNotification() {
        PendingIntent resultPendingIntent = getPendingIntent();

        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Time up")
                .setContentText("Time to take a break")
                .setContentIntent(resultPendingIntent)
                .build();

        notificationManager.notify(1, notification);
    }

    public PendingIntent getPendingIntent() {
        Intent resultIntent = new Intent(context, TimerActivity.class);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addParentStack(TimerActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        return stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void hideAllNotifications() {
        notificationManager.cancelAll();
    }
}
