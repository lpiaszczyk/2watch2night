package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsVideosPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class MovieVideosModule {

    private final MovieDetailsContract.MovieVideosView movieVideosView;

    public MovieVideosModule(MovieDetailsContract.MovieVideosView movieVideosView) {
        this.movieVideosView = movieVideosView;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsContract.MovieVideosView provideMovieVideosView() {
        return movieVideosView;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsContract.MovieVideosPresenter provideMovieVideosPresenter(RemoteMovieSource source, NetworkUtils networkUtils) {
        return new MovieDetailsVideosPresenter(movieVideosView, source, networkUtils);
    }
}