package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.moviesWatchlist.MovieWatchlistFragment;
import dev.paj.towatchtonight.util.module.MovieWatchlistFragmentModule;
import dev.paj.towatchtonight.util.module.MoviesListAdapterModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;

@MoviesViewScope
@Component(dependencies = {DatabaseAccessComponent.class},
        modules = {MovieWatchlistFragmentModule.class, MoviesListAdapterModule.class})
public interface MovieWatchlistFragmentComponent {
    void inject(MovieWatchlistFragment movieWatchlistFragment);
}
