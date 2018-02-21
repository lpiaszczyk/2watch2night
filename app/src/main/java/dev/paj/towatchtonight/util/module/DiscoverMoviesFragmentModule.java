package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.DiscoverMoviesFragmentContract;
import dev.paj.towatchtonight.presenter.discoverMovies.DiscoverMoviesFragmentPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class DiscoverMoviesFragmentModule {

    private final DiscoverMoviesFragmentContract.View view;

    public DiscoverMoviesFragmentModule(DiscoverMoviesFragmentContract.View view) {
        this.view = view;
    }

    @Provides
    DiscoverMoviesFragmentContract.View provideDiscoverMoviesContractView() {
        return view;
    }

    @Provides
    DiscoverMoviesFragmentContract.Presenter provideDiscoverMoviesContractPresenter(RemoteMovieSource source, NetworkUtils networkUtils) {
        return new DiscoverMoviesFragmentPresenter(view, source, networkUtils);
    }
}
