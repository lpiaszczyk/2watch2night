package dev.paj.towatchtonight.util.module;

import android.support.v4.app.Fragment;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesListAdapter;

@Module
public class MoviesListAdapterModule {
    private Fragment view;

    public MoviesListAdapterModule(Fragment view) {
        this.view = view;
    }

    @Provides
    public MoviesListAdapter provideMoviesListAdapter() {
        return new MoviesListAdapter(view);
    }
}
