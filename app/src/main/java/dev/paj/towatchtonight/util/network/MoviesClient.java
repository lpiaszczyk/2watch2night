package dev.paj.towatchtonight.util.network;

import java.util.Map;

import dev.paj.towatchtonight.data.model.movie.GenresResults;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.data.model.movie.MovieResults;
import dev.paj.towatchtonight.data.model.movie.MovieReviewResults;
import dev.paj.towatchtonight.data.model.movie.MovieVideoResults;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface MoviesClient {
    @GET("movie/{id}")
    Single<MovieDetails> getMovieDetails(@Path("id") String id);

    @GET("movie/{id}/reviews")
    Single<MovieReviewResults> getMovieReviews(@Path("id") String id);

    @GET("movie/{id}/videos")
    Single<MovieVideoResults> getMovieVideos(@Path("id") String id);

    @GET("movie/{criteria}")
    Single<MovieResults> getMoviesResults(@Path("criteria") String criteria, @Query("page") int pageNumber);

    @GET("discover/movie")
    Single<MovieResults> discoverMovies(@Query("page") int pageNumber, @QueryMap Map<String, String> params);

    @GET("genre/movie/list")
    Single<GenresResults> getFullGenresList();

    @GET("search/movie")
    Single<MovieResults> searchMovies(@Query("query") String query, @Query("page") int pageNumber);
}
