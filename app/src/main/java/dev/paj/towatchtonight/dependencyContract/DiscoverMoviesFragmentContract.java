package dev.paj.towatchtonight.dependencyContract;

import android.support.v7.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;

public interface DiscoverMoviesFragmentContract {
    interface View {
        void showError(RemoteSourceError errorType);
        void hideError();
        void showMoviesList(List<Movie> movies);
        void addMoviesToList(List<Movie> movies);
    }

    interface Presenter {
        void initMoviesList(Date dateFrom, Date dateTo, int voteFrom, int voteTo, List<Genre> genres);
        void loadNextPage();
        void startMovieDetailsFragment(Movie movie, AppCompatActivity activity, boolean offlineMode);
    }
}
