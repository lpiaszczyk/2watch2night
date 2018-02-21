package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.util.network.MoviesClient;
import retrofit2.Retrofit;

@Module
public class MoviesClientModule {

    @Provides
    @MoviesViewScope
    public MoviesClient provideMoviesClient(Retrofit retrofit) {
        return retrofit.create(MoviesClient.class);
    }
}
