package io.github.gianpamx.pdd.breaktimer.di;

import dagger.Subcomponent;
import io.github.gianpamx.pdd.breaktimer.BreakTimerActivity;
import io.github.gianpamx.pdd.di.ActivityScope;

@ActivityScope
@Subcomponent(modules = {BreakTimerActivityModule.class})
public interface BreakTimerComponent {
    void inject(BreakTimerActivity breakTimerActivity);
}
