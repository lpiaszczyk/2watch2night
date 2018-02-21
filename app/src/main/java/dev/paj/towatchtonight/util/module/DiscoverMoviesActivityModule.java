package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.dependencyContract.DiscoverMoviesActivityContract;
import dev.paj.towatchtonight.presenter.discoverMovies.DiscoverMoviesActivityPresenter;

@Module
public class DiscoverMoviesActivityModule {

    private final DiscoverMoviesActivityContract.View view;

    public DiscoverMoviesActivityModule(DiscoverMoviesActivityContract.View view) {
        this.view = view;
    }

    @Provides
    DiscoverMoviesActivityContract.View provideDiscoverMoviesActivityView() {
        return this.view;
    }

    @Provides
    DiscoverMoviesActivityContract.Presenter provideMainScreenContractPresenter() {
        return new DiscoverMoviesActivityPresenter(view);
    }
}
