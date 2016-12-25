package io.github.gianpamx.pdd.alarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import io.github.gianpamx.pdd.wakeup.AlarmReceiver;

public class AlarmImpl implements Alarm {

    private ActiveTimeUpListener activeTimeUpListener;

    private Context context;
    private AlarmManager alarmManager;

    public AlarmImpl(Context context, AlarmManager alarmManager) {
        this.context = context;
        this.alarmManager = alarmManager;
    }

    @Override
    public void setWakeUpTime(long wakeUpTime, String tag) {
        alarmManager.cancel(getPendingIntent(tag));
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, wakeUpTime, getPendingIntent(tag));
    }

    private PendingIntent getPendingIntent(String tag) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.setType(tag);
        intent.putExtra(EXTRA_TAG, tag);
        return PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void setActiveTimeUpListener(ActiveTimeUpListener activeTimeUpListener) {
        this.activeTimeUpListener = activeTimeUpListener;
    }

    public ActiveTimeUpListener getActiveTimeUpListener() {
        return activeTimeUpListener;
    }

    @Override
    public void cancel(String tag) {
        alarmManager.cancel(getPendingIntent(tag));
    }
}
