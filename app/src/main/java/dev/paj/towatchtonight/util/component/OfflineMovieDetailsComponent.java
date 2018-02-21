package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.moviesWatchlist.OfflineMovieDetailsCardFragment;
import dev.paj.towatchtonight.util.module.MoviesViewScope;
import dev.paj.towatchtonight.util.module.OfflineMovieDetailsModule;

@MoviesViewScope
@Component (dependencies = {DatabaseAccessComponent.class},
        modules = {OfflineMovieDetailsModule.class})
public interface OfflineMovieDetailsComponent {
    void inject(OfflineMovieDetailsCardFragment fragment);
}
