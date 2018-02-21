package dev.paj.towatchtonight.presenter.moviesWatchlist;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.source.local.DatabaseAccess;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class OfflineMovieDetailsMovieCardPresenter implements MovieDetailsContract.MovieCardPresenter {

    private MovieDetailsContract.MovieCardView movieCardView;
    private DatabaseAccess databaseAccess;

    @Inject
    public OfflineMovieDetailsMovieCardPresenter(MovieDetailsContract.MovieCardView movieCardView, DatabaseAccess databaseAccess) {
        this.movieCardView = movieCardView;
        this.databaseAccess = databaseAccess;
    }

    @Override
    public void getMovieDetailsData(String movieId) {
        databaseAccess.getMovieDetailsFromWatchlist(movieId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((movieDetails) -> movieCardView.populateMovieDetailsCard(movieDetails));
    }
}
