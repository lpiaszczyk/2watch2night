package dev.paj.towatchtonight.presenter.moviesWatchlist;

import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.dependencyContract.MovieWatchlistActivityContract;
import dev.paj.towatchtonight.ui.moviesWatchlist.MovieWatchlistFragment;
import dev.paj.towatchtonight.util.navigator.LoadFragmentIntoActivityNavigator;

public class MovieWatchlistActivityPresenter implements MovieWatchlistActivityContract.Presenter {

    private MovieWatchlistActivityContract.View view;

    public MovieWatchlistActivityPresenter(MovieWatchlistActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void loadWatchlistFragment(AppCompatActivity caller, int containerId, String tag) {
        new LoadFragmentIntoActivityNavigator(new MovieWatchlistFragment(),
                caller, containerId, tag).navigate();
    }

}
