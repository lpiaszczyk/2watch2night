package dev.paj.towatchtonight.util.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.App;

@Module
public class AppContextModule {
    private App app;
    public AppContextModule(App app) {
        this.app = app;
    }

    @Provides
    Context provideCotext() {
        return app.getApplicationContext();
    }
}
