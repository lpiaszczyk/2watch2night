package dev.paj.towatchtonight.presenter.movieDetailsView;

import java.util.List;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.movie.MovieReview;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsReviewsPresenter implements MovieDetailsContract.MovieReviewsPresenter {

    private MovieDetailsContract.MovieReviewsView movieReviewsView;
    private RemoteMovieSource remoteSource;
    private NetworkUtils networkUtils;

    @Inject
    public MovieDetailsReviewsPresenter(MovieDetailsContract.MovieReviewsView movieReviewsView, RemoteMovieSource remoteSource, NetworkUtils networkUtils) {
        this.movieReviewsView = movieReviewsView;
        this.remoteSource = remoteSource;
        this.networkUtils = networkUtils;
    }

    @Override
    public void getMovieReviewsData(String movieId) {
        if(networkUtils.isNetworkAvailable()) {
            remoteSource.getMovieReviews(movieId)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((movieReviews) -> {
                                List<MovieReview> results = movieReviews.getResults();
                                if(!results.isEmpty()){
                                    movieReviewsView.populateMovieReviewsCard(movieReviews.getResults());
                                }
                                else {
                                    movieReviewsView.showEmptyListMessage();
                                }
                            },
                                (e) -> movieReviewsView.showMovieReviewsError(RemoteSourceError.SERVER_ERROR));
        } else {
            movieReviewsView.showMovieReviewsError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }
    }
}
