package mx.segundamano.gianpa.pdd.pomodorotimer.di;

import dagger.Module;
import dagger.Provides;
import mx.segundamano.gianpa.pdd.alarm.Alarm;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;
import mx.segundamano.gianpa.pdd.data.SettingsGateway;
import mx.segundamano.gianpa.pdd.di.ActivityScope;
import mx.segundamano.gianpa.pdd.notify.NotificationGateway;
import mx.segundamano.gianpa.pdd.pomodorotimer.PomodoroTimerActivity;
import mx.segundamano.gianpa.pdd.pomodorotimer.PomodoroTimerPresenter;
import mx.segundamano.gianpa.pdd.pomodorotimer.PomodoroTimerUseCase;
import mx.segundamano.gianpa.pdd.ticker.Ticker;

@Module
public class PomodoroTimerActivityModule {
    PomodoroTimerActivity activity;

    public PomodoroTimerActivityModule(PomodoroTimerActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public PomodoroTimerPresenter providePomodoroTimerPresenter(PomodoroTimerUseCase pomodoroTimerUseCase) {
        return new PomodoroTimerPresenter(pomodoroTimerUseCase);
    }


    @Provides
    @ActivityScope
    public PomodoroTimerUseCase providePomodoroTimerUseCase(PomodoroRepository pomodoroRepository, Ticker ticker, Alarm alarm, NotificationGateway notificationGateway, SettingsGateway settingsGateway) {
        return new PomodoroTimerUseCase(pomodoroRepository, ticker, alarm, notificationGateway, settingsGateway);
    }
}
