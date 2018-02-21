package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.moviesWatchlist.MovieWatchlistActivity;
import dev.paj.towatchtonight.util.module.MovieWatchlistActivityModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;

@MoviesViewScope
@Component(
        modules = {MovieWatchlistActivityModule.class})
public interface MovieWatchlistActivityComponent {
    void inject(MovieWatchlistActivity activity);
}
