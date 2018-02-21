package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.dependencyContract.MainActivityContract;
import dev.paj.towatchtonight.presenter.mainMoviesList.MainActivityPresenter;

@Module
public class MainActivityModule {

    private final MainActivityContract.View view;

    public MainActivityModule(MainActivityContract.View view) {
        this.view = view;
    }

    @Provides
    @MainActivityScope
    MainActivityContract.View provideMainScreenContractView() {
        return view;
    }

    @Provides
    @MainActivityScope
    MainActivityContract.Presenter provideMainScreenContractPresenter() {
        return new MainActivityPresenter(view);
    }

}
