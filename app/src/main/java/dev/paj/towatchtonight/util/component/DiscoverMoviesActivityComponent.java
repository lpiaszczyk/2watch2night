package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.discoverMovies.DiscoverMoviesActivity;
import dev.paj.towatchtonight.util.module.DiscoverMoviesActivityModule;

@Component(modules = DiscoverMoviesActivityModule.class)
public interface DiscoverMoviesActivityComponent {
    void inject(DiscoverMoviesActivity view);
}
