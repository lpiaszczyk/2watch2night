package dev.paj.towatchtonight.presenter.mainMoviesList;

import android.support.v7.app.AppCompatActivity;

import javax.inject.Inject;

import dev.paj.towatchtonight.dependencyContract.MainActivityContract;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesViewFragment;
import dev.paj.towatchtonight.util.navigator.LoadFragmentIntoActivityNavigator;

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View view;

    @Inject
    public MainActivityPresenter(MainActivityContract.View view){
        this.view = view;
    }


    @Override
    public void loadInitView(AppCompatActivity caller, int containerId, String tag) {
        new LoadFragmentIntoActivityNavigator(new MoviesViewFragment(),
                caller, containerId, tag).navigate();
    }
}
