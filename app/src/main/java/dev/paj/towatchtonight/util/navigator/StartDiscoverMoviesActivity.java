package dev.paj.towatchtonight.util.navigator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.ui.discoverMovies.DiscoverMoviesActivity;

public class StartDiscoverMoviesActivity implements Navigator {

    private AppCompatActivity callingContext;

    public StartDiscoverMoviesActivity(AppCompatActivity callingContext) {
        this.callingContext = callingContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(callingContext, DiscoverMoviesActivity.class);
        callingContext.startActivity(intent);
    }
}
