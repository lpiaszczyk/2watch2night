package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.GenresDialogPickerContract;
import dev.paj.towatchtonight.presenter.discoverMovies.GenresDialogPickerPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class GenresDialogPickerModule {

    private final GenresDialogPickerContract.View view;

    public GenresDialogPickerModule(GenresDialogPickerContract.View view) {
        this.view = view;
    }

    @Provides
    GenresDialogPickerContract.View provideGenresDialogPickerView() {
        return view;
    }

    @Provides
    GenresDialogPickerContract.Presenter provideGenresDialogPickerPresenter(RemoteMovieSource source, NetworkUtils networkUtils) {
        return new GenresDialogPickerPresenter(view, source, networkUtils);
    }
}
