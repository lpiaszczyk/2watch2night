package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsMovieCardPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class MovieDetailsModule {

    private final MovieDetailsContract.MovieCardView movieCardView;

    public MovieDetailsModule(MovieDetailsContract.MovieCardView movieCardView) {
        this.movieCardView = movieCardView;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsContract.MovieCardView provideMovieDetailsView() {
        return movieCardView;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsContract.MovieCardPresenter provideMovieDetailsPresenter(RemoteMovieSource source, NetworkUtils networkUtils) {
        return new MovieDetailsMovieCardPresenter(movieCardView, source, networkUtils);
    }
}
