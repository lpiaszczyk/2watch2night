package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.dependencyContract.UpcomingMoviesActivityContract;
import dev.paj.towatchtonight.presenter.upcomingMoviesList.UpcomingMoviesActivityPresenter;

@Module
public class UpcomingMoviesActivityModule {

    private final UpcomingMoviesActivityContract.View view;

    public UpcomingMoviesActivityModule(UpcomingMoviesActivityContract.View view) {
        this.view = view;
    }

    @Provides
    UpcomingMoviesActivityContract.View provideUpcomingMoviesActivityContractView() {
        return view;
    }

    @Provides
    UpcomingMoviesActivityContract.Presenter provideUpcomingMoviesActivityContractPresenter() {
        return new UpcomingMoviesActivityPresenter(view);
    }

}
