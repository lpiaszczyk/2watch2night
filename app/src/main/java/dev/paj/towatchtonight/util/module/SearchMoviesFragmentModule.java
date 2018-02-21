package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.SearchMoviesFragmentContract;
import dev.paj.towatchtonight.presenter.searchMovies.SearchMoviesFragmentPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class SearchMoviesFragmentModule {

    private final SearchMoviesFragmentContract.View view;

    public SearchMoviesFragmentModule(SearchMoviesFragmentContract.View view) {
        this.view = view;
    }

    @Provides
    SearchMoviesFragmentContract.View provideSearchMoviesFragmentContractView() {
        return view;
    }

    @Provides
    SearchMoviesFragmentContract.Presenter provideSearchMoviesFragmentContractPresenter(RemoteMovieSource source, NetworkUtils networkUtils) {
        return new SearchMoviesFragmentPresenter(view, source, networkUtils);
    }
}
