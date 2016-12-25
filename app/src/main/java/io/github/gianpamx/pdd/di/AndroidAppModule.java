package io.github.gianpamx.pdd.di;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.github.gianpamx.pdd.AndroidApp;
import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.alarm.AlarmImpl;
import io.github.gianpamx.pdd.breaktimer.BreakTimerRepositoryImpl;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.PomodoroRepository;
import io.github.gianpamx.pdd.data.SettingsGateway;
import io.github.gianpamx.pdd.notify.NotificationGateway;
import io.github.gianpamx.pdd.notify.NotificationGatewayImpl;
import io.github.gianpamx.pdd.notify.NotifyUseCase;
import io.github.gianpamx.pdd.settings.SettingsGatewayImpl;
import io.github.gianpamx.pdd.ticker.Ticker;
import io.github.gianpamx.pdd.ticker.TickerImpl;
import io.github.gianpamx.pdd.wakeup.WakeupUseCase;

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
    public Ticker provideTicker(AlarmManager alarmManager) {
        return new TickerImpl();
    }

    @Provides
    @Singleton
    public Alarm provideAlarm(AlarmManager alarmManager) {
        return new AlarmImpl(androidApp, alarmManager);
    }

    @Provides
    @Singleton
    public WakeupUseCase provideWakeupUseCase(Alarm alarm, PomodoroRepository pomodoroRepository, BreakTimerRepository breakTimerRepository) {
        return new WakeupUseCase(alarm, pomodoroRepository, breakTimerRepository);
    }

    @Provides
    @Singleton
    public NotifyUseCase provideNotifyUseCase(NotificationGateway notificationGateway) {
        return new NotifyUseCase(notificationGateway);
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
        return Realm.getDefaultInstance();
    }

    @Singleton
    @Provides
    public SettingsGateway provideSettingsRepository() {
        return new SettingsGatewayImpl(androidApp);
    }

    @Singleton
    @Provides
    public BreakTimerRepository provideBreakTimerRepository(SettingsGateway settingsGateway) {
        return new BreakTimerRepositoryImpl(settingsGateway);
    }
}
