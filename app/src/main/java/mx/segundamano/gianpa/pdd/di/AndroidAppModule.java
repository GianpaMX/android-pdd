package mx.segundamano.gianpa.pdd.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.timer.AlarmGateway;
import mx.segundamano.gianpa.pdd.timer.alarmgateway.AlarmGatewayImpl;

@Module
public class AndroidAppModule {
    protected AndroidApp androidApp;

    public AndroidAppModule(AndroidApp androidApp) {
        this.androidApp = androidApp;
    }

    @Singleton
    @Provides
    public Context provideContext() {
        return androidApp;
    }

    @Singleton
    @Provides
    public AlarmGateway provideAlarmGateway() {
        return new AlarmGatewayImpl(androidApp);
    }
}
