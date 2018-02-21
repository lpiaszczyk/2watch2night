package dev.paj.towatchtonight.presenter.mainMoviesList;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.MoviesListsCriteria;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MoviesViewContract;
import dev.paj.towatchtonight.util.navigator.StartMovieDetailsActivityNavigator;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MoviesViewPresenter implements MoviesViewContract.Presenter {

    private MoviesViewContract.View view;
    private RemoteMovieSource source;
    private int currentPageNumber;
    private MoviesListsCriteria currentListCriteria;
    private NetworkUtils networkUtils;
    private String TAG;

    @Inject
    public MoviesViewPresenter(MoviesViewContract.View view, RemoteMovieSource source, NetworkUtils networkUtils){
        this.view = view;
        this.source = source;
        this.networkUtils = networkUtils;
        currentListCriteria = MoviesListsCriteria.POPULAR;
        TAG = getClass().getSimpleName();
    }

    @Override
    public void initMoviesList() {
        if(networkUtils.isNetworkAvailable()) {
            resetPageNumber();
            source.getMoviesList(currentListCriteria, currentPageNumber)
            .subscribeOn(Schedulers.newThread())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe((moviesList) -> populateViewMoviesList(moviesList.getResults()),
                    (e) -> view.showError(RemoteSourceError.SERVER_ERROR));
        } else {
            view.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }
    }

    @Override
    public void loadNextPage() {
        if(networkUtils.isNetworkAvailable()) {
            currentPageNumber++;
            source.getMoviesList(currentListCriteria, currentPageNumber)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((moviesList) -> populateViewMoviesList(moviesList.getResults()),
                            (e) -> view.showError(RemoteSourceError.SERVER_ERROR));
        } else {
            view.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }

    }

    @Override
    public void updateListCriteria(MoviesListsCriteria criteria) {
        this.currentListCriteria = criteria;
        resetPageNumber();
        initMoviesList();
    }

    @Override
    public void startMovieDetailsFragment(Movie movie, AppCompatActivity activity, boolean offlineMode) {
        new StartMovieDetailsActivityNavigator(activity, movie, offlineMode).navigate();
    }

    private void resetPageNumber() {
        this.currentPageNumber = 1;
    }

    private void populateViewMoviesList(List<Movie> moviesList) {
        //Log.i(TAG, "fetched movies list from remote source");
        if(currentPageNumber == 1) {
            view.showMoviesList(moviesList);
        } else if (currentPageNumber > 1) {
            view.addMoviesToList(moviesList);
        } else {
            //Log.e(TAG, "Page number has unsupported value: " + currentPageNumber);
            view.showError(RemoteSourceError.OTHER);
        }

    }
}
