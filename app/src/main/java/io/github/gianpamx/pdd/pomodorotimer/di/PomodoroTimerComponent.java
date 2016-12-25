package io.github.gianpamx.pdd.pomodorotimer.di;

import dagger.Subcomponent;
import io.github.gianpamx.pdd.di.ActivityScope;
import io.github.gianpamx.pdd.pomodorotimer.PomodoroTimerActivity;

@ActivityScope
@Subcomponent(modules = {PomodoroTimerActivityModule.class})
public interface PomodoroTimerComponent {
    void inject(PomodoroTimerActivity pomodoroTimerActivity);
}
