package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsReviewsPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class MovieReviewsModule {

    private final MovieDetailsContract.MovieReviewsView movieReviewsView;

    public MovieReviewsModule(MovieDetailsContract.MovieReviewsView movieCardView) {
        this.movieReviewsView = movieCardView;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsContract.MovieReviewsView provideMovieReviewsView() {
        return movieReviewsView;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsContract.MovieReviewsPresenter provideMovieReviewsPresenter(RemoteMovieSource source, NetworkUtils networkUtils) {
        return new MovieDetailsReviewsPresenter(movieReviewsView, source, networkUtils);
    }
}
