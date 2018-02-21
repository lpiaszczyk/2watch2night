package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.local.DatabaseAccess;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.presenter.moviesWatchlist.OfflineMovieDetailsMovieCardPresenter;

@Module
public class OfflineMovieDetailsModule {

    private final MovieDetailsContract.MovieCardView movieCardView;

    public OfflineMovieDetailsModule(MovieDetailsContract.MovieCardView movieCardView) {
        this.movieCardView = movieCardView;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsContract.MovieCardView provideMovieDetailsView() {
        return movieCardView;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsContract.MovieCardPresenter provideMovieDetailsPresenter(DatabaseAccess databaseAccess) {
        return new OfflineMovieDetailsMovieCardPresenter(movieCardView, databaseAccess);
    }
}
