package dev.paj.towatchtonight.presenter.moviesWatchlist;

import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.local.DatabaseAccess;
import dev.paj.towatchtonight.data.source.local.LocalSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieWatchlistFragmentContract;
import dev.paj.towatchtonight.util.navigator.StartMovieDetailsActivityNavigator;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MovieWatchlistFragmentPresenter implements MovieWatchlistFragmentContract.Presenter {

    private MovieWatchlistFragmentContract.View view;
    private DatabaseAccess databaseAccess;

    @Inject
    public MovieWatchlistFragmentPresenter(MovieWatchlistFragmentContract.View view, DatabaseAccess databaseAccess) {
        this.view = view;
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void initMoviesList() {
        databaseAccess.getMoviesFromWatchlist()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((moviesList) -> view.showMoviesList(moviesList),
                        (e) -> view.showError(LocalSourceError.ERROR_GET_MOVIES));
    }

    @Override
    public void startMovieDetailsFragment(Movie movie, AppCompatActivity activity, boolean offlineMode) {
        new StartMovieDetailsActivityNavigator(activity, movie, offlineMode).navigate();
    }
}
