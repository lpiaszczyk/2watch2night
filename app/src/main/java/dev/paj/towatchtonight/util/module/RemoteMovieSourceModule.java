package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.util.network.MoviesClient;

@Module
@MoviesViewScope
public class RemoteMovieSourceModule {

    @Provides
    RemoteMovieSource provideRemoteMovieSource(MoviesClient client) {
        return new RemoteMovieSource(client);
    }
}
