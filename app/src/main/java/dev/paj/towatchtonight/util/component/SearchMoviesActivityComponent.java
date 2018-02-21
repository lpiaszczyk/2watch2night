package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.searchMovies.SearchMoviesActivity;
import dev.paj.towatchtonight.util.module.MainActivityScope;
import dev.paj.towatchtonight.util.module.SearchMoviesActivityModule;

@MainActivityScope
@Component(modules = {SearchMoviesActivityModule.class})
public interface SearchMoviesActivityComponent {
    void inject(SearchMoviesActivity view);
}
