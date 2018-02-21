package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.MoviesViewContract;
import dev.paj.towatchtonight.presenter.mainMoviesList.MoviesViewPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class MoviesViewModule {

    private final MoviesViewContract.View view;

    public MoviesViewModule(MoviesViewContract.View view) {
        this.view = view;
    }

    @Provides
    @MoviesViewScope
    MoviesViewContract.View provideMainScreenContractView() {
        return view;
    }

    @Provides
    @MoviesViewScope
    MoviesViewContract.Presenter provideMainScreenContractPresenter(RemoteMovieSource source, NetworkUtils networkUtils) {
        return new MoviesViewPresenter(view, source, networkUtils);
    }
}
