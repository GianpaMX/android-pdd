package mx.segundamano.gianpa.pdd.breaktimer.di;

import dagger.Module;
import mx.segundamano.gianpa.pdd.breaktimer.BreakTimerActivity;

@Module
public class BreakTimerActivityModule {
    private final BreakTimerActivity breakTimerActivity;

    public BreakTimerActivityModule(BreakTimerActivity breakTimerActivity) {
        this.breakTimerActivity = breakTimerActivity;
    }
}
