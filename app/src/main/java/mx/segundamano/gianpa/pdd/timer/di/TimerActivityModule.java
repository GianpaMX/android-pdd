package mx.segundamano.gianpa.pdd.timer.di;

import dagger.Module;
import dagger.Provides;
import mx.segundamano.gianpa.pdd.alarmgateway.AlarmGateway;
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
    public TimerPresenter provideTimerPresenter(TimerUseCase useCase) {
        return new TimerPresenter(useCase);
    }


    @Provides
    @ActivityScope
    public TimerUseCase provideTimerUseCase(AlarmGateway alarmGateway) {
        return new TimerUseCase(alarmGateway);
    }
}
