package mx.segundamano.gianpa.pdd.di;

import javax.inject.Singleton;

import dagger.Component;
import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.complete.di.CompleteComponent;
import mx.segundamano.gianpa.pdd.complete.di.CompleteServiceModule;
import mx.segundamano.gianpa.pdd.timer.di.TimerActivityModule;
import mx.segundamano.gianpa.pdd.timer.di.TimerComponent;
import mx.segundamano.gianpa.pdd.wakeup.AlarmReceiver;

@Component(modules = {AndroidAppModule.class})
@Singleton
public interface AndroidAppComponent {
    TimerComponent timerComponent(TimerActivityModule timerActivityModule);

    CompleteComponent completeComponent(CompleteServiceModule completeServiceModule);

    void inject(AlarmReceiver alarmReceiver);
}
