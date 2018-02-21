package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.mainMoviesList.MainActivity;
import dev.paj.towatchtonight.util.module.MainActivityModule;
import dev.paj.towatchtonight.util.module.MainActivityScope;

@MainActivityScope
@Component(modules = {MainActivityModule.class})
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
