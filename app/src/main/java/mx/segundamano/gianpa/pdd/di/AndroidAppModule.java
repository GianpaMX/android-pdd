package mx.segundamano.gianpa.pdd.di;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.ticker.Ticker;
import mx.segundamano.gianpa.pdd.ticker.TickerImpl;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;
import mx.segundamano.gianpa.pdd.notify.NotificationGateway;
import mx.segundamano.gianpa.pdd.notify.NotificationGatewayImpl;
import mx.segundamano.gianpa.pdd.notify.NotifyUseCase;
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
    public Ticker provideAlarmGateway(AlarmManager alarmManager) {
        return new TickerImpl();
    }

    @Provides
    @Singleton
    public WakeupUseCase provideWakeupUseCase(Ticker ticker) {
        return new WakeupUseCase(ticker);
    }

    @Provides
    @Singleton
    public NotifyUseCase provideNotifyUseCase(Ticker ticker, NotificationGateway notificationGateway) {
        return new NotifyUseCase(ticker, notificationGateway);
    }

    @Provides
    @Singleton
    public NotificationManager provideNotificationManager() {
        return (NotificationManager) androidApp.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Provides
    @Singleton
    public NotificationGateway provideNotificationGateway(NotificationManager notificationManager) {
        return new NotificationGatewayImpl(androidApp, notificationManager);
    }

    @Provides
    @Singleton
    public AlarmManager provideAlarmManager() {
        return (AlarmManager) androidApp.getSystemService(Context.ALARM_SERVICE);
    }

    @Provides
    @Singleton
    public PomodoroRepository providesPomodoroRepository(Realm realm) {
        return new PomodoroRepository(realm);
    }

    @Singleton
    @Provides
    public Realm provideRealm() {
        Realm.init(androidApp);

        return Realm.getDefaultInstance();
    }

}
