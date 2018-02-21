package dev.paj.towatchtonight.presenter.discoverMovies;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.GenresDialogPickerContract;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class GenresDialogPickerPresenter implements GenresDialogPickerContract.Presenter {

    private GenresDialogPickerContract.View view;
    private RemoteMovieSource source;
    private NetworkUtils networkUtils;

    @Inject
    public GenresDialogPickerPresenter(GenresDialogPickerContract.View view, RemoteMovieSource source, NetworkUtils networkUtils) {
        this.view = view;
        this.source = source;
        this.networkUtils = networkUtils;
    }

    @Override
    public void initGenresList() {
        if(networkUtils.isNetworkAvailable()){
            source.getFullGenresList()
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(genresResults -> view.populateGenresList(genresResults.getResults()),
                            e -> view.showError(RemoteSourceError.SERVER_ERROR));
        } else {
            view.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        }

    }
}
