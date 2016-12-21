package mx.segundamano.gianpa.pdd.di;

import javax.inject.Singleton;

import dagger.Component;
import mx.segundamano.gianpa.pdd.breaktimer.di.BreakTimerActivityModule;
import mx.segundamano.gianpa.pdd.breaktimer.di.BreakTimerComponent;
import mx.segundamano.gianpa.pdd.complete.di.CompleteComponent;
import mx.segundamano.gianpa.pdd.complete.di.CompleteServiceModule;
import mx.segundamano.gianpa.pdd.pomodorotimer.di.PomodoroTimerActivityModule;
import mx.segundamano.gianpa.pdd.pomodorotimer.di.PomodoroTimerComponent;
import mx.segundamano.gianpa.pdd.wakeup.AlarmReceiver;

@Component(modules = {AndroidAppModule.class})
@Singleton
public interface AndroidAppComponent {
    PomodoroTimerComponent pomodoroTimerComponent(PomodoroTimerActivityModule pomodoroTimerActivityModule);

    CompleteComponent completeComponent(CompleteServiceModule completeServiceModule);

    BreakTimerComponent breakTimerComponent(BreakTimerActivityModule breakTimerActivityModule);

    void inject(AlarmReceiver alarmReceiver);
}
