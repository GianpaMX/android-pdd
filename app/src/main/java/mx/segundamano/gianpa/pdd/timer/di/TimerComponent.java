package mx.segundamano.gianpa.pdd.timer.di;

import dagger.Subcomponent;
import mx.segundamano.gianpa.pdd.di.ActivityScope;
import mx.segundamano.gianpa.pdd.timer.TimerActivity;

@ActivityScope
@Subcomponent(modules = {TimerActivityModule.class})
public interface TimerComponent {
    void inject(TimerActivity timerActivity);
}
