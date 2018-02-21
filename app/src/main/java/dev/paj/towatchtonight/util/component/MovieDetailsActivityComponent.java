package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsActivity;
import dev.paj.towatchtonight.ui.moviesWatchlist.MovieWatchlistDetailsActivity;
import dev.paj.towatchtonight.util.module.MovieDetailsActivityModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;

@MoviesViewScope
@Component(dependencies = {DatabaseAccessComponent.class},
        modules = {MovieDetailsActivityModule.class})
public interface MovieDetailsActivityComponent {
    void inject(MovieDetailsActivity activity);
    void inject(MovieWatchlistDetailsActivity activity);
}
