package mx.segundamano.gianpa.pdd.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;
import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGatewayImpl;
import mx.segundamano.gianpa.pdd.wakeup.WakeupUseCase;

@Module
public class AndroidAppModule {
    protected AndroidApp androidApp;

    public AndroidAppModule(AndroidApp androidApp) {
        this.androidApp = androidApp;
    }

    @Provides
    @Singleton
    public Context provideContext() {
        return androidApp;
    }

    @Provides
    @Singleton
    public AlarmGateway provideAlarmGateway() {
        return new AlarmGatewayImpl(androidApp);
    }

    @Provides
    @Singleton
    public WakeupUseCase provideWakeupUseCase(AlarmGateway alarmGateway) {
        return new WakeupUseCase(alarmGateway);
    }
}
