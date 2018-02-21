package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.dependencyContract.MovieWatchlistActivityContract;
import dev.paj.towatchtonight.presenter.moviesWatchlist.MovieWatchlistActivityPresenter;

@Module
public class MovieWatchlistActivityModule {

    private final MovieWatchlistActivityContract.View view;

    public MovieWatchlistActivityModule(MovieWatchlistActivityContract.View view) {
        this.view = view;
    }

    @Provides
    @MoviesViewScope
    MovieWatchlistActivityContract.View provideMovieWatchlistContractView() {
        return view;
    }

    @Provides
    @MoviesViewScope
    MovieWatchlistActivityContract.Presenter provideMovieWatchlistContractPresenter() {
        return new MovieWatchlistActivityPresenter(view);
    }

}
