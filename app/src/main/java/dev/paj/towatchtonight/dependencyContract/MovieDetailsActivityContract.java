package dev.paj.towatchtonight.dependencyContract;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;

public interface MovieDetailsActivityContract {
    interface View {
        void addCurrentMovieToDatabase();
        void onMovieAddToWatchlistSuccess();
        void showAddToWatchlistError();
        void removeCurrentMovieFromWatchlist();
        void showRemoveFromWatchlistError();
        void onMovieRemoveFromWatchlistSuccess();
        void markMovieAsPresentOnWatchlist(boolean result);
    }

    interface Presenter {
        void addMovieToWatchlist(Movie movie, MovieDetails movieDetails);
        void removeMovieFromWatchlist(Movie movie);
        void checkIfMovieIsPresentInWatchlist(Movie movie);
    }
}
