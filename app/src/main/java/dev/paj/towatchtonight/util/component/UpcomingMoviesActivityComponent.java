package dev.paj.towatchtonight.util.component;

import dagger.Component;
import dev.paj.towatchtonight.ui.upcomingMoviesList.UpcomingMoviesActivity;
import dev.paj.towatchtonight.util.module.UpcomingMoviesActivityModule;

@Component(modules = {UpcomingMoviesActivityModule.class})
public interface UpcomingMoviesActivityComponent {
    void inject(UpcomingMoviesActivity activity);
}
