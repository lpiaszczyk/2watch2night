package dev.paj.towatchtonight.dependencyContract;

import android.support.v7.app.AppCompatActivity;

public interface MovieWatchlistActivityContract {
    interface View {

    }

    interface Presenter {
        void loadWatchlistFragment(AppCompatActivity caller, int containerId, String tag);
    }
}
