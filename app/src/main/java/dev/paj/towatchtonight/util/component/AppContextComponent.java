package dev.paj.towatchtonight.util.component;

import android.content.Context;

import dagger.Component;
import dev.paj.towatchtonight.util.module.AppContextModule;

@Component(modules = AppContextModule.class)
public interface AppContextComponent {
    Context provideAppContext();
}
