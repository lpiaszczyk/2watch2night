package dev.paj.towatchtonight.presenter.searchMovies;

import android.support.v7.app.AppCompatActivity;

import dev.paj.towatchtonight.dependencyContract.SearchMoviesActivityContract;
import dev.paj.towatchtonight.ui.searchMovies.SearchMoviesFragment;
import dev.paj.towatchtonight.util.navigator.LoadFragmentIntoActivityNavigator;

public class SearchMoviesActivityPresenter implements SearchMoviesActivityContract.Presenter{

    private SearchMoviesActivityContract.View view;

    public SearchMoviesActivityPresenter(SearchMoviesActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void loadInitView(AppCompatActivity caller, int containerId, String tag) {
        new LoadFragmentIntoActivityNavigator(new SearchMoviesFragment(),
                caller, containerId, tag).navigate();
    }
}
