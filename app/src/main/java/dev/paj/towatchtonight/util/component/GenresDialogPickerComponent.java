package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.discoverMovies.GenresPickerDialog;
import dev.paj.towatchtonight.util.module.GenresDialogPickerModule;
import dev.paj.towatchtonight.util.module.MoviesClientModule;
import dev.paj.towatchtonight.util.module.MoviesListAdapterModule;
import dev.paj.towatchtonight.util.module.MoviesViewScope;
import dev.paj.towatchtonight.util.module.NetworkUtilsModule;
import dev.paj.towatchtonight.util.module.RemoteMovieSourceModule;

@MoviesViewScope
@Component(dependencies = {NetComponent.class, AppContextComponent.class,},
        modules = {GenresDialogPickerModule.class, MoviesListAdapterModule.class,
                RemoteMovieSourceModule.class, MoviesClientModule.class,
                NetworkUtilsModule.class})
public interface GenresDialogPickerComponent {
    void inject(GenresPickerDialog view);
}
