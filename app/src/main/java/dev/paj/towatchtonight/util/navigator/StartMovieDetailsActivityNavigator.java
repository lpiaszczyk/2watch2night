package dev.paj.towatchtonight.util.navigator;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsActivity;
import dev.paj.towatchtonight.ui.moviesWatchlist.MovieWatchlistDetailsActivity;

public class StartMovieDetailsActivityNavigator implements Navigator {

    private AppCompatActivity callingContext;
    private Movie movieToShow;
    private boolean offlineMode;

    public StartMovieDetailsActivityNavigator(AppCompatActivity callingContext, Movie movieToShow, boolean offlineMode) {
        this.callingContext = callingContext;
        this.movieToShow = movieToShow;
        this.offlineMode = offlineMode;
    }

    @Override
    public void navigate() {
        Class toOpen = offlineMode ? MovieWatchlistDetailsActivity.class : MovieDetailsActivity.class;
        Intent intent = new Intent(callingContext, toOpen);
        Bundle bundle = new Bundle();
        bundle.putParcelable(callingContext.getString(R.string.movie_parcelable_name), movieToShow);
        intent.putExtras(bundle);
        callingContext.startActivity(intent);
    }
}
