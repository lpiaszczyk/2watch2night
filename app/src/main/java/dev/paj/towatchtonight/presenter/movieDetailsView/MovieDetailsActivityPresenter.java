package dev.paj.towatchtonight.presenter.movieDetailsView;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.data.source.local.DatabaseAccess;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsActivityContract;
import io.reactivex.android.schedulers.AndroidSchedulers;

public class MovieDetailsActivityPresenter implements MovieDetailsActivityContract.Presenter {

    private MovieDetailsActivityContract.View view;
    private DatabaseAccess databaseAccess;

    @Inject
    public MovieDetailsActivityPresenter(MovieDetailsActivityContract.View view, DatabaseAccess databaseAccess) {
        this.view = view;
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void addMovieToWatchlist(Movie movie, MovieDetails movieDetails) {
        databaseAccess.addMovieToWatchList(movie, movieDetails)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    if(result) {
                        view.onMovieAddToWatchlistSuccess();
                    } else {
                        view.showAddToWatchlistError();
                    }
                }, (e) -> view.showAddToWatchlistError());
    }

    @Override
    public void removeMovieFromWatchlist(Movie movie) {
        databaseAccess.removeMovieFromDatabase(movie)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> {
                    if(result) {
                        view.onMovieRemoveFromWatchlistSuccess();
                    } else {
                        view.showRemoveFromWatchlistError();
                    }
                }, (e) -> view.showRemoveFromWatchlistError());
    }

    @Override
    public void checkIfMovieIsPresentInWatchlist(Movie movie) throws IllegalArgumentException {
        if(movie == null){
            throw new IllegalArgumentException("Movie cannot be null");
        }
        databaseAccess.checkIfMovieIsPresentInWatchlist(movie)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((result) -> view.markMovieAsPresentOnWatchlist(result));
    }
}
