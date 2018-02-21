package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsCardFragment;
import dev.paj.towatchtonight.util.module.MovieDetailsModule;
import dev.paj.towatchtonight.util.module.MoviesClientModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;
import dev.paj.towatchtonight.util.module.NetworkUtilsModule;
import dev.paj.towatchtonight.util.module.RemoteMovieSourceModule;

@MoviesViewScope
@Component(dependencies = {NetComponent.class, AppContextComponent.class,},
        modules = {MovieDetailsModule.class, RemoteMovieSourceModule.class, MoviesClientModule.class, NetworkUtilsModule.class})
public interface MovieDetailsComponent {
    void inject(MovieDetailsCardFragment fragment);
}
