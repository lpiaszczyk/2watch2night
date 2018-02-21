package dev.paj.towatchtonight.util.navigator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.ui.moviesWatchlist.MovieWatchlistActivity;

public class StartMovieWatchlistActivity implements Navigator {

    private AppCompatActivity callingContext;

    public StartMovieWatchlistActivity(AppCompatActivity callingContext) {
        this.callingContext = callingContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(callingContext, MovieWatchlistActivity.class);
        callingContext.startActivity(intent);
    }
}
