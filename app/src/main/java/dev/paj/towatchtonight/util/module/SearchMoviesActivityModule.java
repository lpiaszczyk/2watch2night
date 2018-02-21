package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.dependencyContract.SearchMoviesActivityContract;
import dev.paj.towatchtonight.presenter.searchMovies.SearchMoviesActivityPresenter;

@Module
public class SearchMoviesActivityModule {

    private final SearchMoviesActivityContract.View view;

    public SearchMoviesActivityModule(SearchMoviesActivityContract.View view) {
        this.view = view;
    }

    @Provides
    @MainActivityScope
    SearchMoviesActivityContract.View provideSearchMoviesActivityContractView() {
        return view;
    }

    @Provides
    @MainActivityScope
    SearchMoviesActivityContract.Presenter provideSearchMoviesActivityContractPresenter() {
        return new SearchMoviesActivityPresenter(view);
    }

}
