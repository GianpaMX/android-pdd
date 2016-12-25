package io.github.gianpamx.pdd.pomodorotimer.di;

import dagger.Module;
import dagger.Provides;
import io.github.gianpamx.pdd.alarm.Alarm;
import io.github.gianpamx.pdd.data.BreakTimerRepository;
import io.github.gianpamx.pdd.data.PomodoroRepository;
import io.github.gianpamx.pdd.data.SettingsGateway;
import io.github.gianpamx.pdd.di.ActivityScope;
import io.github.gianpamx.pdd.notify.NotificationGateway;
import io.github.gianpamx.pdd.pomodorotimer.PomodoroTimerActivity;
import io.github.gianpamx.pdd.pomodorotimer.PomodoroTimerPresenter;
import io.github.gianpamx.pdd.pomodorotimer.PomodoroTimerUseCase;
import io.github.gianpamx.pdd.ticker.Ticker;

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
    public PomodoroTimerUseCase providePomodoroTimerUseCase(PomodoroRepository pomodoroRepository,
                                                            Ticker ticker,
                                                            Alarm alarm,
                                                            NotificationGateway notificationGateway,
                                                            SettingsGateway settingsGateway,
                                                            BreakTimerRepository breakTimerRepository) {

        return new PomodoroTimerUseCase(pomodoroRepository, ticker, alarm, notificationGateway, settingsGateway, breakTimerRepository);
    }
}
