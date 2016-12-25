package io.github.gianpamx.pdd.complete.di;

import dagger.Subcomponent;
import io.github.gianpamx.pdd.complete.CompleteService;

@Subcomponent(modules = {CompleteServiceModule.class})
public interface CompleteComponent {
    void inject(CompleteService completeService);
}
