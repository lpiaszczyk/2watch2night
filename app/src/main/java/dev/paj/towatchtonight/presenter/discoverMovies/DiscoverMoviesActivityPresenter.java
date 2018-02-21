package dev.paj.towatchtonight.presenter.discoverMovies;

import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dev.paj.towatchtonight.dependencyContract.DiscoverMoviesActivityContract;
import dev.paj.towatchtonight.ui.discoverMovies.DiscoverMoviesFragment;
import dev.paj.towatchtonight.util.navigator.LoadFragmentIntoActivityNavigator;

public class DiscoverMoviesActivityPresenter implements DiscoverMoviesActivityContract.Presenter {

    private DiscoverMoviesActivityContract.View view;

    @Inject
    public DiscoverMoviesActivityPresenter(DiscoverMoviesActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void loadInitView(AppCompatActivity caller, int containerId, String tag) {
        new LoadFragmentIntoActivityNavigator(new DiscoverMoviesFragment(),
                caller, containerId, tag).navigate();
    }
}
