package dev.paj.towatchtonight.dependencyContract;

import java.util.List;

import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;

public interface GenresDialogPickerContract {
    interface View {
        void populateGenresList(List<Genre> genres);
        void showError(RemoteSourceError error);
        void hideError();
    }

    interface Presenter {
        void initGenresList();
    }
}
