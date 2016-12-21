package mx.segundamano.gianpa.pdd.breaktimer.di;

import dagger.Subcomponent;
import mx.segundamano.gianpa.pdd.breaktimer.BreakTimerActivity;
import mx.segundamano.gianpa.pdd.di.ActivityScope;
import mx.segundamano.gianpa.pdd.pomodorotimer.di.PomodoroTimerActivityModule;

@ActivityScope
@Subcomponent(modules = {BreakTimerActivityModule.class})
public interface BreakTimerComponent {
    void inject(BreakTimerActivity breakTimerActivity);
}
