package mx.segundamano.gianpa.pdd.breaktimer.di;

import dagger.Module;
import dagger.Provides;
import mx.segundamano.gianpa.pdd.alarm.Alarm;
import mx.segundamano.gianpa.pdd.breaktimer.BreakTimerActivity;
import mx.segundamano.gianpa.pdd.breaktimer.BreakTimerPresenter;
import mx.segundamano.gianpa.pdd.breaktimer.BreakTimerUseCase;
import mx.segundamano.gianpa.pdd.data.BreakTimerRepository;
import mx.segundamano.gianpa.pdd.data.SettingsGateway;
import mx.segundamano.gianpa.pdd.di.ActivityScope;
import mx.segundamano.gianpa.pdd.notify.NotificationGateway;
import mx.segundamano.gianpa.pdd.ticker.Ticker;

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
