package dev.paj.towatchtonight.presenter.discoverMovies;

import android.support.v7.app.AppCompatActivity;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.DiscoverMoviesFragmentContract;
import dev.paj.towatchtonight.util.navigator.StartMovieDetailsActivityNavigator;
import dev.paj.towatchtonight.util.network.DiscoverMoviesParamsMapUtil;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class DiscoverMoviesFragmentPresenter implements DiscoverMoviesFragmentContract.Presenter {

    private DiscoverMoviesFragmentContract.View view;
    private RemoteMovieSource source;
    private int currentPageNumber;
    private NetworkUtils networkUtils;
    private Map<String, String> params;

    @Inject
    public DiscoverMoviesFragmentPresenter(DiscoverMoviesFragmentContract.View view, RemoteMovieSource source, NetworkUtils networkUtils) {
        this.view = view;
        this.source = source;
        this.networkUtils = networkUtils;
    }


    @Override
    public void initMoviesList(Date dateFrom, Date dateTo, int voteFrom, int voteTo, List<Genre> genres) {
        if (networkUtils.isNetworkAvailable()){
            resetPageNumber();
            params = DiscoverMoviesParamsMapUtil.create(dateFrom, dateTo, voteFrom, voteTo, genres);

            source.discoverMovies(currentPageNumber, params).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((moviesList) -> view.showMoviesList(moviesList.getResults()),
                            (e) -> view.showError(RemoteSourceError.SERVER_ERROR));
        } else {
            view.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }

    }

    @Override
    public void loadNextPage() {
        if(networkUtils.isNetworkAvailable()) {
            currentPageNumber++;
            source.discoverMovies(currentPageNumber, params).subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((moviesList) -> view.addMoviesToList(moviesList.getResults()),
                            (e) -> view.showError(RemoteSourceError.SERVER_ERROR));
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
