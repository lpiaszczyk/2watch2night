package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.local.DatabaseAccess;
import dev.paj.towatchtonight.dependencyContract.MovieWatchlistFragmentContract;
import dev.paj.towatchtonight.presenter.moviesWatchlist.MovieWatchlistFragmentPresenter;

@Module
public class MovieWatchlistFragmentModule {

    private final MovieWatchlistFragmentContract.View view;

    public MovieWatchlistFragmentModule(MovieWatchlistFragmentContract.View view) {
        this.view = view;
    }

    @Provides
    @MoviesViewScope
    MovieWatchlistFragmentContract.View MovieWatchlistFragmentView() {
        return view;
    }

    @Provides
    @MoviesViewScope
    MovieWatchlistFragmentContract.Presenter MovieWatchlistFragmentPresenter(DatabaseAccess databaseAccess) {
        return new MovieWatchlistFragmentPresenter(view, databaseAccess);
    }

}
