package mx.segundamano.gianpa.pdd.complete.di;

import dagger.Subcomponent;
import mx.segundamano.gianpa.pdd.complete.CompleteService;

@Subcomponent(modules = {CompleteServiceModule.class})
public interface CompleteComponent {
    void inject(CompleteService completeService);
}
