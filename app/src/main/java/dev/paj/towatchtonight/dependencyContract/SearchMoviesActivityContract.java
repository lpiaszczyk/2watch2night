package dev.paj.towatchtonight.dependencyContract;

import android.support.v7.app.AppCompatActivity;

public interface SearchMoviesActivityContract {
    interface View{

    }
    interface Presenter {
        void loadInitView(AppCompatActivity caller, int containerId, String tag);
    }
}
