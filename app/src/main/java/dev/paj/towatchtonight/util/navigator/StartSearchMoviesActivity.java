package dev.paj.towatchtonight.util.navigator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.ui.searchMovies.SearchMoviesActivity;

public class StartSearchMoviesActivity implements Navigator {

    private AppCompatActivity callingContext;

    public StartSearchMoviesActivity(AppCompatActivity callingContext) {
        this.callingContext = callingContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(callingContext, SearchMoviesActivity.class);
        callingContext.startActivity(intent);
    }
}
