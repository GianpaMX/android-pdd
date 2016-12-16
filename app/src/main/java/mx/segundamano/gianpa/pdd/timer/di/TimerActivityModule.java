package mx.segundamano.gianpa.pdd.timer.di;

import dagger.Module;
import dagger.Provides;
import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;
import mx.segundamano.gianpa.pdd.di.ActivityScope;
import mx.segundamano.gianpa.pdd.timer.TimerActivity;
import mx.segundamano.gianpa.pdd.timer.TimerPresenter;
import mx.segundamano.gianpa.pdd.timer.TimerUseCase;

@Module
public class TimerActivityModule {
    TimerActivity activity;

    public TimerActivityModule(TimerActivity activity) {
        this.activity = activity;
    }

    @Provides
    @ActivityScope
    public TimerPresenter provideTimerPresenter(TimerUseCase timerUseCase) {
        return new TimerPresenter(timerUseCase);
    }


    @Provides
    @ActivityScope
    public TimerUseCase provideTimerUseCase(PomodoroRepository pomodoroRepository, AlarmGateway alarmGateway) {
        return new TimerUseCase(pomodoroRepository, alarmGateway);
    }
}
