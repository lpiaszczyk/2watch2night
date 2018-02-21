package dev.paj.towatchtonight.dependencyContract;

import android.support.v7.app.AppCompatActivity;

public interface UpcomingMoviesActivityContract {

    interface View {

    }

    interface Presenter {
        void initUpcomingList(AppCompatActivity caller, int containerId, String tag);
    }
}
