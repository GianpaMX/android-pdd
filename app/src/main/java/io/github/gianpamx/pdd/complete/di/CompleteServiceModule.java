package io.github.gianpamx.pdd.complete.di;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.complete.CompleteService;
import io.github.gianpamx.pdd.complete.CompleteUseCase;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.PomodoroRepository;
import io.github.gianpamx.pdd.data.SettingsGateway;
import io.github.gianpamx.pdd.notify.NotificationGateway;

@Module
public class CompleteServiceModule {
    private final CompleteService service;

    public CompleteServiceModule(CompleteService completeService) {
        service = completeService;
    }

    @Provides
    public CompleteUseCase provideCompleteUseCase(NotificationGateway notificationGateway, SettingsGateway settingsGateway, BreakTimerRepository breakTimerRepository, Alarm alarm) {
        Realm realm = Realm.getDefaultInstance();
        PomodoroRepository pomodoroRepository = new PomodoroRepository(realm);
        return new CompleteUseCase(pomodoroRepository, notificationGateway, settingsGateway, breakTimerRepository, alarm);
    }
}
