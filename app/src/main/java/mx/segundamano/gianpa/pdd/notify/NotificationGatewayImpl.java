package mx.segundamano.gianpa.pdd.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.complete.CompleteService;
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
        PendingIntent resultPendingIntent = getTimerPendingIntent();

        PendingIntent stopPendingIntent = getStopPendingIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Notification.Action stopAction = new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_stop_24dp), context.getString(R.string.timer_fragment_button_stop_text), stopPendingIntent).build();
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.notification_active_pomodoro_title))
                    .setContentText(context.getString(R.string.notification_active_pomodoro_text))
                    .setContentIntent(resultPendingIntent)
                    .setOngoing(true)
                    .setTicker(context.getString(R.string.notification_active_pomodoro_ticker))
                    .setWhen(when)
                    .setUsesChronometer(true)
                    .setChronometerCountDown(true)
                    .addAction(stopAction)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.notification_active_pomodoro_title))
                    .setContentText(context.getString(R.string.notification_active_pomodoro_text))
                    .setContentIntent(resultPendingIntent)
                    .setOngoing(true)
                    .setTicker(context.getString(R.string.notification_active_pomodoro_ticker))
                    .setWhen(when)
                    .setUsesChronometer(true)
                    .addAction(R.drawable.ic_stop_24dp, context.getString(R.string.timer_fragment_button_stop_text), stopPendingIntent)
                    .build();
        }
        notificationManager.notify(1, this.notification);
    }

    @Override
    public void showTimeUpNotification() {
        PendingIntent resultPendingIntent = getTimerPendingIntent();

        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_time_up_title))
                .setContentText(context.getString(R.string.notification_time_up_text))
                .setContentIntent(resultPendingIntent)
                .setOngoing(true)
                .addAction(R.drawable.ic_check_24dp, context.getString(R.string.pomodoro_complete_actions_complete), getCompletePendingIntent(true))
                .addAction(R.drawable.ic_close_24dp, context.getString(R.string.pomodoro_complete_actions_discard), getCompletePendingIntent(false))
                .build();

        notificationManager.notify(1, notification);
    }

    private PendingIntent getStopPendingIntent() {
        Intent stopTimerActivityIntent = new Intent(context, TimerActivity.class);
        stopTimerActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        stopTimerActivityIntent.putExtra(TimerActivity.IS_STOP_INTENT, true);

        return PendingIntent.getActivity(context, 0, stopTimerActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getCompletePendingIntent(boolean isComplete) {
        Intent completeIntent = CompleteService.newIntent(context, isComplete);

        return PendingIntent.getService(context, 0, completeIntent, PendingIntent.FLAG_ONE_SHOT);
    }

    public PendingIntent getTimerPendingIntent() {
        Intent  timerActivityIntent= new Intent(context, TimerActivity.class);
        timerActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(context, 0, timerActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void hideAllNotifications() {
        notificationManager.cancelAll();
    }
}
