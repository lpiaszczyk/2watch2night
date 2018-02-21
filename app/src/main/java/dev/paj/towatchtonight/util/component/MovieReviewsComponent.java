package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsReviewsFragment;
import dev.paj.towatchtonight.util.module.MovieReviewsModule;
import dev.paj.towatchtonight.util.module.MoviesClientModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;
import dev.paj.towatchtonight.util.module.NetworkUtilsModule;
import dev.paj.towatchtonight.util.module.RemoteMovieSourceModule;

@MoviesViewScope
@Component(dependencies = {NetComponent.class, AppContextComponent.class,},
        modules = {MovieReviewsModule.class, RemoteMovieSourceModule.class, MoviesClientModule.class, NetworkUtilsModule.class})
public interface MovieReviewsComponent {
    void inject(MovieDetailsReviewsFragment fragment);
}
