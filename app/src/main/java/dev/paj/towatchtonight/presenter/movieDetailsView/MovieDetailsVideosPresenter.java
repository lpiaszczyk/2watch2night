package dev.paj.towatchtonight.presenter.movieDetailsView;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.movie.MovieVideoResults;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MovieDetailsVideosPresenter implements MovieDetailsContract.MovieVideosPresenter {

    private MovieDetailsContract.MovieVideosView movieVideosView;
    private RemoteMovieSource remoteSource;
    private NetworkUtils networkUtils;

    @Inject
    public MovieDetailsVideosPresenter(MovieDetailsContract.MovieVideosView movieVideosView, RemoteMovieSource remoteSource, NetworkUtils networkUtils) {
        this.movieVideosView = movieVideosView;
        this.remoteSource = remoteSource;
        this.networkUtils = networkUtils;
    }

    @Override
    public void getMovieVideosData(String movieId) {
        if(networkUtils.isNetworkAvailable()) {
            remoteSource.getMovieVideos(movieId)
                    .subscribeOn(Schedulers.newThread())
                    .map(MovieVideoResults::getResults)
                    .flattenAsObservable(videos -> videos)
                    .filter(movieVideo -> movieVideo.getSite()
                            .equals("YouTube"))
                    .sorted((m1, m2) -> m1.getType()
                            .compareTo(m2.getType()))
                    .toList()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe((movieVideos) -> {
                                if(!movieVideos.isEmpty()){
                                    movieVideosView.populateMovieVideosCard(movieVideos);
                                } else {
                                    movieVideosView.showEmptyListMessage();
                                }
                            },
                            (e) -> movieVideosView.showMovieVideosError(RemoteSourceError.SERVER_ERROR));
        } else {
            movieVideosView.showMovieVideosError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }
    }
}
