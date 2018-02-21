package dev.paj.towatchtonight.presenter.movieDetailsView;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


public class MovieDetailsMovieCardPresenter implements MovieDetailsContract.MovieCardPresenter {
    private MovieDetailsContract.MovieCardView movieCardView;
    private RemoteMovieSource remoteSource;
    private NetworkUtils networkUtils;

    @Inject
    public MovieDetailsMovieCardPresenter(MovieDetailsContract.MovieCardView movieCardView, RemoteMovieSource remoteMovieSource, NetworkUtils networkUtils) {
        this.movieCardView = movieCardView;
        this.remoteSource = remoteMovieSource;
        this.networkUtils = networkUtils;
    }


    @Override
    public void getMovieDetailsData(String movieId) {
        if(networkUtils.isNetworkAvailable()) {
            remoteSource.getMovieDetails(movieId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((movieDetails) -> movieCardView.populateMovieDetailsCard(movieDetails),
                            (e) -> movieCardView.showMovieDetailsError(RemoteSourceError.SERVER_ERROR));
        } else {
            movieCardView.showMovieDetailsError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }

    }
}
