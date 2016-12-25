package io.github.gianpamx.pdd.breaktimer.di;

import dagger.Module;
import dagger.Provides;
import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.breaktimer.BreakTimerActivity;
import io.github.gianpamx.pdd.breaktimer.BreakTimerPresenter;
import io.github.gianpamx.pdd.breaktimer.BreakTimerUseCase;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.SettingsGateway;
import io.github.gianpamx.pdd.di.ActivityScope;
import io.github.gianpamx.pdd.notify.NotificationGateway;
import io.github.gianpamx.pdd.ticker.Ticker;

@Module
public class BreakTimerActivityModule {
    private final BreakTimerActivity breakTimerActivity;

    public BreakTimerActivityModule(BreakTimerActivity breakTimerActivity) {
        this.breakTimerActivity = breakTimerActivity;
    }

    @Provides
    @ActivityScope
    public BreakTimerPresenter provideBreakTimerPresenter(BreakTimerUseCase breakTimerUseCase) {
        return new BreakTimerPresenter(breakTimerUseCase);
    }

    @Provides
    @ActivityScope
    public BreakTimerUseCase provideBreakTimerUseCase(BreakTimerRepository breakTimerRepository, Ticker ticker, Alarm alarm, NotificationGateway notificationGateway, SettingsGateway settingsGateway) {
        return new BreakTimerUseCase(breakTimerRepository, ticker, alarm, notificationGateway, settingsGateway);
    }
}
