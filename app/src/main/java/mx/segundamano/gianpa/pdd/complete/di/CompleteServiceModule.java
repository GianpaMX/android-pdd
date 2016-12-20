package mx.segundamano.gianpa.pdd.complete.di;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;
import mx.segundamano.gianpa.pdd.complete.CompleteService;
import mx.segundamano.gianpa.pdd.complete.CompleteUseCase;
import mx.segundamano.gianpa.pdd.data.PomodoroRepository;
import mx.segundamano.gianpa.pdd.notify.NotificationGateway;

@Module
public class CompleteServiceModule {
    private final CompleteService service;

    public CompleteServiceModule(CompleteService completeService) {
        service = completeService;
    }

    @Provides
    public CompleteUseCase provideCompleteUseCase(NotificationGateway notificationGateway) {
        Realm realm = Realm.getDefaultInstance();
        PomodoroRepository pomodoroRepository = new PomodoroRepository(realm);
        return new CompleteUseCase(pomodoroRepository, notificationGateway);
    }
}