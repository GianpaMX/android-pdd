package mx.segundamano.gianpa.pdd.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import mx.segundamano.gianpa.pdd.wakeup.AlarmReceiver;

public class AlarmImpl implements Alarm {
    private ActiveTimeUpListener activeTimeUpListener;

    private Context context;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    public AlarmImpl(Context context, AlarmManager alarmManager) {
        this.context = context;
        this.alarmManager = alarmManager;
    }

    @Override
    public void setWakeUpTime(long wakeUpTime) {
        alarmManager.cancel(getPendingIntent());
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        if (pendingIntent == null) {
            Intent intent = new Intent(context, AlarmReceiver.class);
            pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }

        return pendingIntent;
    }


    public void setActiveTimeUpListener(ActiveTimeUpListener activeTimeUpListener) {
        this.activeTimeUpListener = activeTimeUpListener;
    }

    public ActiveTimeUpListener getActiveTimeUpListener() {
        return activeTimeUpListener;
    }

    @Override
    public void cancel() {
        alarmManager.cancel(getPendingIntent());
    }
}
