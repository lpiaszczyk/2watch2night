package dev.paj.towatchtonight.data.source.remote;

import java.util.Map;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.MoviesListsCriteria;
import dev.paj.towatchtonight.data.model.movie.GenresResults;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.data.model.movie.MovieResults;
import dev.paj.towatchtonight.data.model.movie.MovieReviewResults;
import dev.paj.towatchtonight.data.model.movie.MovieVideoResults;
import dev.paj.towatchtonight.util.network.MoviesClient;
import io.reactivex.Single;

public class RemoteMovieSource {

    private MoviesClient client;

    @Inject
    public RemoteMovieSource(MoviesClient client) {
        this.client = client;
    }

    public Single<MovieResults> getMoviesList(MoviesListsCriteria currentListCriteria, int pageNumber) {
            return client.getMoviesResults(currentListCriteria.getDescription(), pageNumber);
    }

    public Single<MovieDetails> getMovieDetails(String movieId) {
        return client.getMovieDetails(movieId);
    }

    public Single<MovieReviewResults> getMovieReviews(String movieId) {
        return client.getMovieReviews(movieId);
    }

    public Single<MovieVideoResults> getMovieVideos(String movieId) {
        return client.getMovieVideos(movieId);
    }

    public Single<MovieResults> discoverMovies(int pageNumber, Map<String, String> params) {
        return client.discoverMovies(pageNumber, params);
    }

    public Single<GenresResults> getFullGenresList() {
        return client.getFullGenresList();
    }

    public Single<MovieResults> searchMovies(int pageNumber, String query) {
        return client.searchMovies(query, pageNumber);
    }
}
