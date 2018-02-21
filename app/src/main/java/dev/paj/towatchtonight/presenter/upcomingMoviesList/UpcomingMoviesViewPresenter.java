package dev.paj.towatchtonight.presenter.upcomingMoviesList;

import android.support.v7.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.UpcomingMoviesViewContract;
import dev.paj.towatchtonight.util.navigator.StartMovieDetailsActivityNavigator;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class UpcomingMoviesViewPresenter implements UpcomingMoviesViewContract.Presenter {

    private UpcomingMoviesViewContract.View view;
    private RemoteMovieSource source;
    private int currentPageNumber;
    private NetworkUtils networkUtils;
    private Map<String, String> queryParams;

    @Inject
    public UpcomingMoviesViewPresenter(UpcomingMoviesViewContract.View view, RemoteMovieSource source, NetworkUtils networkUtils){
        this.view = view;
        this.source = source;
        this.networkUtils = networkUtils;
        setupQueryParams();
    }

    @Override
    public void initMoviesList() {
        if(networkUtils.isNetworkAvailable()) {
            resetPageNumber();
            source.discoverMovies(currentPageNumber, queryParams)
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
            source.discoverMovies(currentPageNumber, queryParams)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((moviesList) -> populateViewMoviesList(moviesList.getResults()),
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

    private void populateViewMoviesList(List<Movie> moviesList) {
        if(currentPageNumber == 1) {
            view.showMoviesList(moviesList);
        } else if (currentPageNumber > 1) {
            view.addMoviesToList(moviesList);
        } else {
            view.showError(RemoteSourceError.OTHER);
        }

    }

    private void setupQueryParams() {
        this.queryParams = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String startDate = format.format(calendar.getTime());
        calendar.add(Calendar.MONTH, 6);
        String endDate = format.format(calendar.getTime());
        queryParams.put("primary_release_date.gte", startDate);
        queryParams.put("primary_release_date.lte", endDate);
    }
}
