package dev.paj.towatchtonight.dependencyContract;

import android.support.v7.app.AppCompatActivity;

import java.util.List;

import dev.paj.towatchtonight.data.model.MoviesListsCriteria;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;

public interface MoviesViewContract {
    interface View {
        void showError(RemoteSourceError errorType);
        void hideError();
        void showMoviesList(List<Movie> movies);
        void addMoviesToList(List<Movie> movies);
    }

    interface Presenter {
        void initMoviesList();
        void loadNextPage();
        void updateListCriteria(MoviesListsCriteria criteria);
        void startMovieDetailsFragment(Movie movie, AppCompatActivity activity, boolean offlineMode);
    }
}
