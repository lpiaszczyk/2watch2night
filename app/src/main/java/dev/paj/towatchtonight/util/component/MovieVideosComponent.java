package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsVideosFragment;
import dev.paj.towatchtonight.util.module.MovieVideosModule;
import dev.paj.towatchtonight.util.module.MoviesClientModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;
import dev.paj.towatchtonight.util.module.NetworkUtilsModule;
import dev.paj.towatchtonight.util.module.RemoteMovieSourceModule;

@MoviesViewScope
@Component(dependencies = {NetComponent.class, AppContextComponent.class,},
        modules = {MovieVideosModule.class, RemoteMovieSourceModule.class, MoviesClientModule.class, NetworkUtilsModule.class})
public interface MovieVideosComponent {
    void inject(MovieDetailsVideosFragment fragment);
}
