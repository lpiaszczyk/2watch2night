package dev.paj.towatchtonight.dependencyContract;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.local.LocalSourceError;

public interface MovieWatchlistFragmentContract {

    interface View {
        void showError(LocalSourceError error);
        void hideError();
        void showMoviesList(List<Movie> movies);
    }

    interface Presenter {
        void initMoviesList();
        void startMovieDetailsFragment(Movie movie, AppCompatActivity activity, boolean offlineMode);
    }
}
