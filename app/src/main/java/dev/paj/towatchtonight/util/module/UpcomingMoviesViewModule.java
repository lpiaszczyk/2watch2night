package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.UpcomingMoviesViewContract;
import dev.paj.towatchtonight.presenter.upcomingMoviesList.UpcomingMoviesViewPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class UpcomingMoviesViewModule {

    private final UpcomingMoviesViewContract.View view;

    public UpcomingMoviesViewModule(UpcomingMoviesViewContract.View view) {
        this.view = view;
    }

    @Provides
    @MoviesViewScope
    UpcomingMoviesViewContract.View provideUpcomingMoviesView() {
        return view;
    }

    @Provides
    @MoviesViewScope
    UpcomingMoviesViewContract.Presenter provideUpcomingMoviesPresenter(RemoteMovieSource source, NetworkUtils networkUtils) {
        return new UpcomingMoviesViewPresenter(view, source, networkUtils);
    }
}
