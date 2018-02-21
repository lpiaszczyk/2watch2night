package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesViewFragment;
import dev.paj.towatchtonight.util.module.EndlessScollListenerModule;
import dev.paj.towatchtonight.util.module.MoviesClientModule;
import dev.paj.towatchtonight.util.module.MoviesListAdapterModule;
import dev.paj.towatchtonight.util.module.MoviesViewModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;
import dev.paj.towatchtonight.util.module.NetworkUtilsModule;
import dev.paj.towatchtonight.util.module.RemoteMovieSourceModule;

@MoviesViewScope
@Component(dependencies = {NetComponent.class, AppContextComponent.class,},
        modules = {MoviesViewModule.class, MoviesListAdapterModule.class,
                EndlessScollListenerModule.class, RemoteMovieSourceModule.class,
                MoviesClientModule.class, NetworkUtilsModule.class})
public interface MoviesViewComponent {
    void inject(MoviesViewFragment fragment);
}
