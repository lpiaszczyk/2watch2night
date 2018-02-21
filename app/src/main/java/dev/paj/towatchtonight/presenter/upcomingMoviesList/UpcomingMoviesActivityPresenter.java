package dev.paj.towatchtonight.presenter.upcomingMoviesList;

import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.dependencyContract.UpcomingMoviesActivityContract;
import dev.paj.towatchtonight.ui.upcomingMoviesList.UpcomingMoviesViewFragment;
import dev.paj.towatchtonight.util.navigator.LoadFragmentIntoActivityNavigator;

public class UpcomingMoviesActivityPresenter implements UpcomingMoviesActivityContract.Presenter {
    UpcomingMoviesActivityContract.View view;

    public UpcomingMoviesActivityPresenter(UpcomingMoviesActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void initUpcomingList(AppCompatActivity caller, int containerId, String tag) {
        new LoadFragmentIntoActivityNavigator(new UpcomingMoviesViewFragment(),
                caller, containerId, tag).navigate();
    }
}
