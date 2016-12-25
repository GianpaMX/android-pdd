package mx.segundamano.gianpa.pdd.notify;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import mx.segundamano.gianpa.pdd.R;
import mx.segundamano.gianpa.pdd.breaktimer.BreakTimerActivity;
import mx.segundamano.gianpa.pdd.pomodorotimer.PomodoroTimerActivity;

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
        PendingIntent timerPendingIntent = getTimerPendingIntent();

        PendingIntent stopPendingIntent = getStopPendingIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Notification.Action stopAction = new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_stop_24dp), context.getString(R.string.timer_fragment_button_stop_text), stopPendingIntent).build();
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.notification_active_pomodoro_title))
                    .setContentText(context.getString(R.string.notification_active_pomodoro_text))
                    .setContentIntent(timerPendingIntent)
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
                    .setContentIntent(timerPendingIntent)
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
    public void showBreakOnGoingNotification(long when) {
        PendingIntent timerPendingIntent = getBreakTimerPendingIntent();

        PendingIntent stopPendingIntent = getStartPendingIntent();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Notification.Action stopAction = new Notification.Action.Builder(Icon.createWithResource(context, R.drawable.ic_play_arrow_24dp), context.getString(R.string.timer_fragment_button_start_text), stopPendingIntent).build();
            notification = new Notification.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle(context.getString(R.string.notification_active_pomodoro_title))
                    .setContentText(context.getString(R.string.notification_active_pomodoro_text))
                    .setContentIntent(timerPendingIntent)
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
                    .setContentIntent(timerPendingIntent)
                    .setOngoing(true)
                    .setTicker(context.getString(R.string.notification_active_pomodoro_ticker))
                    .setWhen(when)
                    .setUsesChronometer(true)
                    .addAction(R.drawable.ic_play_arrow_24dp, context.getString(R.string.timer_fragment_button_start_text), stopPendingIntent)
                    .build();
        }
        notificationManager.notify(1, this.notification);
    }

    @Override
    public void showPomodoroTimeUpNotification() {
        PendingIntent timerPendingIntent = getTimerPendingIntent();

        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_time_up_title))
                .setContentText(context.getString(R.string.notification_time_up_text))
                .setContentIntent(timerPendingIntent)
                .setOngoing(true)
                .addAction(R.drawable.ic_check_24dp, context.getString(R.string.pomodoro_complete_actions_complete), getCompletePendingIntent(true))
                .addAction(R.drawable.ic_close_24dp, context.getString(R.string.pomodoro_complete_actions_discard), getCompletePendingIntent(false))
                .build();

        notificationManager.notify(1, notification);
    }

    @Override
    public void showBreakTimeUpNotification() {
        PendingIntent timerPendingIntent = getBreakTimerPendingIntent();

        notification = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(context.getString(R.string.notification_time_up_title))
                .setContentText(context.getString(R.string.notification_time_up_text))
                .setContentIntent(timerPendingIntent)
                .setOngoing(true)
                .addAction(R.drawable.ic_play_arrow_24dp, context.getString(R.string.timer_fragment_button_start_text), getStartPendingIntent())
                .build();

        notificationManager.notify(1, notification);
    }

    private PendingIntent getStopPendingIntent() {
        Intent stopTimerActivityIntent = new Intent(context, PomodoroTimerActivity.class);
        stopTimerActivityIntent.setAction(Intent.ACTION_EDIT);
        stopTimerActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        stopTimerActivityIntent.putExtra(PomodoroTimerActivity.IS_STOP_INTENT, true);

        return PendingIntent.getActivity(context, 0, stopTimerActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getCompletePendingIntent(boolean isComplete) {
        Intent completeIntent = new Intent(isComplete ? context.getString(R.string.COMPLETE_AND_COUND) : context.getString(R.string.COMPLETE_AND_DISCARD));
        return PendingIntent.getService(context, 0, completeIntent, PendingIntent.FLAG_ONE_SHOT);
    }

    public PendingIntent getTimerPendingIntent() {
        Intent timerActivityIntent = new Intent(context, PomodoroTimerActivity.class);
        timerActivityIntent.setAction(Intent.ACTION_VIEW);
        timerActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        return PendingIntent.getActivity(context, 0, timerActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getBreakTimerPendingIntent() {
        Intent breakTimerActivityIntent = new Intent(context, BreakTimerActivity.class);
        breakTimerActivityIntent.setAction(Intent.ACTION_VIEW);
        breakTimerActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        taskStackBuilder.addNextIntentWithParentStack(breakTimerActivityIntent);

        return taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getStartPendingIntent() {
        Intent completeIntent = new Intent(context.getString(R.string.COMPLETE_BREAK));
        return PendingIntent.getService(context, 0, completeIntent, PendingIntent.FLAG_ONE_SHOT);
    }


    @Override
    public void hideAllNotifications() {
        notificationManager.cancelAll();
    }
}
