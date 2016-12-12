package mx.segundamano.gianpa.pdd.di;

import javax.inject.Singleton;

import dagger.Component;
import mx.segundamano.gianpa.pdd.AndroidApp;
import mx.segundamano.gianpa.pdd.wakeup.AlarmReceiver;
import mx.segundamano.gianpa.pdd.timer.di.TimerActivityModule;
import mx.segundamano.gianpa.pdd.timer.di.TimerComponent;

@Component(modules = {AndroidAppModule.class})
@Singleton
public interface AndroidAppComponent {
    TimerComponent timerComponent(TimerActivityModule timerActivityModule);

    void inject(AlarmReceiver alarmReceiver);

    void inject(AndroidApp androidApp);
}
