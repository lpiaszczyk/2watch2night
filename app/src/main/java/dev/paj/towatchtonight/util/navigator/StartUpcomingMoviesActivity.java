package dev.paj.towatchtonight.util.navigator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.ui.upcomingMoviesList.UpcomingMoviesActivity;

public class StartUpcomingMoviesActivity implements Navigator {

    private AppCompatActivity callingContext;

    public StartUpcomingMoviesActivity(AppCompatActivity callingContext) {
        this.callingContext = callingContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(callingContext, UpcomingMoviesActivity.class);
        callingContext.startActivity(intent);
    }
}
