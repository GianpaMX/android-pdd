package mx.segundamano.gianpa.pdd.pomodorotimer.di;

import dagger.Subcomponent;
import mx.segundamano.gianpa.pdd.di.ActivityScope;
import mx.segundamano.gianpa.pdd.pomodorotimer.PomodoroTimerActivity;

@ActivityScope
@Subcomponent(modules = {PomodoroTimerActivityModule.class})
public interface PomodoroTimerComponent {
    void inject(PomodoroTimerActivity pomodoroTimerActivity);
}
