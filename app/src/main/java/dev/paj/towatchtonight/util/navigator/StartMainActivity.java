package dev.paj.towatchtonight.util.navigator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.ui.mainMoviesList.MainActivity;

public class StartMainActivity implements Navigator {

    private AppCompatActivity callingContext;

    public StartMainActivity(AppCompatActivity callingContext) {
        this.callingContext = callingContext;
    }

    @Override
    public void navigate() {
        Intent intent = new Intent(callingContext, MainActivity.class);
        callingContext.startActivity(intent);
    }
}
