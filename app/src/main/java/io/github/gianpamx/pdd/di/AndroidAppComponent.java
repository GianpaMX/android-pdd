package io.github.gianpamx.pdd.di;

import javax.inject.Singleton;

import dagger.Component;
import io.github.gianpamx.pdd.breaktimer.di.BreakTimerActivityModule;
import io.github.gianpamx.pdd.breaktimer.di.BreakTimerComponent;
import io.github.gianpamx.pdd.complete.di.CompleteComponent;
import io.github.gianpamx.pdd.complete.di.CompleteServiceModule;
import io.github.gianpamx.pdd.pomodorotimer.di.PomodoroTimerActivityModule;
import io.github.gianpamx.pdd.pomodorotimer.di.PomodoroTimerComponent;
import io.github.gianpamx.pdd.wakeup.AlarmReceiver;

@Component(modules = {AndroidAppModule.class})
@Singleton
public interface AndroidAppComponent {
    PomodoroTimerComponent pomodoroTimerComponent(PomodoroTimerActivityModule pomodoroTimerActivityModule);

    CompleteComponent completeComponent(CompleteServiceModule completeServiceModule);

    BreakTimerComponent breakTimerComponent(BreakTimerActivityModule breakTimerActivityModule);

    void inject(AlarmReceiver alarmReceiver);
}
