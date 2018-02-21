package dev.paj.towatchtonight.util.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.local.WatchlistDatabaseHelper;

@Module
public class WatchlistDatabaseHelperModule {

    @Singleton
    @Provides
    WatchlistDatabaseHelper provideDatabaseHelper(Context context) {
        return new WatchlistDatabaseHelper(context);
    }
}
