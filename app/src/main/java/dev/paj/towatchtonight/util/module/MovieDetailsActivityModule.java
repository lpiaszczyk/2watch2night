package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.local.DatabaseAccess;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsActivityContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsActivityPresenter;

@Module
public class MovieDetailsActivityModule {

    private final MovieDetailsActivityContract.View view;

    public MovieDetailsActivityModule(MovieDetailsActivityContract.View view) {
        this.view = view;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsActivityContract.View provideMainScreenContractView() {
        return view;
    }

    @Provides
    @MoviesViewScope
    MovieDetailsActivityContract.Presenter provideMainScreenContractPresenter(DatabaseAccess databaseAccess) {
        return new MovieDetailsActivityPresenter(view, databaseAccess);
    }

}
