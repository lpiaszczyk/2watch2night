package dev.paj.towatchtonight.util.module;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.util.network.NetworkUtils;

@Module
public class NetworkUtilsModule {
    @Provides
    NetworkUtils provideNetworkUtils(Context context) {
        return new NetworkUtils(context);
    }
}
