package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.upcomingMoviesList.UpcomingMoviesViewFragment;
import dev.paj.towatchtonight.util.module.EndlessScollListenerModule;
import dev.paj.towatchtonight.util.module.MoviesClientModule;
import dev.paj.towatchtonight.util.module.MoviesListAdapterModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;
import dev.paj.towatchtonight.util.module.NetworkUtilsModule;
import dev.paj.towatchtonight.util.module.RemoteMovieSourceModule;
import dev.paj.towatchtonight.util.module.UpcomingMoviesViewModule;

@MoviesViewScope
@Component(dependencies = {NetComponent.class, AppContextComponent.class,},
        modules = {UpcomingMoviesViewModule.class, MoviesListAdapterModule.class,
                EndlessScollListenerModule.class, RemoteMovieSourceModule.class,
                MoviesClientModule.class, NetworkUtilsModule.class})
public interface UpcomingMoviesViewComponent {
    void inject(UpcomingMoviesViewFragment fragment);
}
