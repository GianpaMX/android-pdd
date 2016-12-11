package mx.segundamano.gianpa.pdd.timer.alarmgateway;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.di.AndroidAppComponent;
import mx.segundamano.gianpa.pdd.timer.AlarmGateway;

public class AlarmReceiver extends BroadcastReceiver {
    @Inject
    public AlarmGateway alarmGateway;

    @Override
    public void onReceive(Context context, Intent intent) {
        WakeLocker.acquire(context);

        inject(this, context);

        alarmGateway.timeUp();
    }

    private void inject(AlarmReceiver alarmReceiver, Context context) {
        getAndroidAppComponent(context).inject(alarmReceiver);
    }

    private AndroidAppComponent getAndroidAppComponent(Context context) {
        return ((AndroidApp) context.getApplicationContext()).getAndroidAppComponent();
    }
}
