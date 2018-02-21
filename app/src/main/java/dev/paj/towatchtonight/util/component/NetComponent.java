package dev.paj.towatchtonight.util.component;

import javax.inject.Singleton;

import dagger.Component;
import dev.paj.towatchtonight.util.module.AppModule;
import dev.paj.towatchtonight.util.module.NetModule;
import retrofit2.Retrofit;

@Singleton
@Component (modules = {AppModule.class, NetModule.class})
public interface NetComponent {
    Retrofit provideRetrofit();
}
