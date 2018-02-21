package dev.paj.towatchtonight.presenter.searchMovies;

import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.SearchMoviesFragmentContract;
import dev.paj.towatchtonight.util.navigator.StartMovieDetailsActivityNavigator;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchMoviesFragmentPresenter implements SearchMoviesFragmentContract.Presenter {

    private SearchMoviesFragmentContract.View view;
    private RemoteMovieSource source;
    private int currentPageNumber;
    private NetworkUtils networkUtils;

    private String currentSearchQuery = "";

    @Inject
    public SearchMoviesFragmentPresenter(SearchMoviesFragmentContract.View view, RemoteMovieSource source, NetworkUtils networkUtils) {
        this.view = view;
        this.source = source;
        this.networkUtils = networkUtils;
    }

    @Override
    public void initMoviesList(String searchQuery) {
        currentSearchQuery = searchQuery;
        resetPageNumber();
        if(networkUtils.isNetworkAvailable()) {
            source.searchMovies(currentPageNumber, currentSearchQuery)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(results -> view.showMoviesList(results.getResults()),
                            e -> view.showError(RemoteSourceError.SERVER_ERROR));
        } else {
            view.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }
    }

    @Override
    public void loadNextPage() {
        if(networkUtils.isNetworkAvailable()) {
            currentPageNumber++;
            source.searchMovies(currentPageNumber, currentSearchQuery)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(results -> view.addMoviesToList(results.getResults()),
                            e -> view.showError(RemoteSourceError.SERVER_ERROR));
        } else {
            view.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }
    }

    @Override
    public void startMovieDetailsFragment(Movie movie, AppCompatActivity activity, boolean offlineMode) {
        new StartMovieDetailsActivityNavigator(activity, movie, offlineMode).navigate();
    }

    private void resetPageNumber() {
        this.currentPageNumber = 1;
    }
}
