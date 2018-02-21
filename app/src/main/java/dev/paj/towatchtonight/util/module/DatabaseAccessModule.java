package dev.paj.towatchtonight.util.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.data.source.local.WatchlistDatabaseHelper;
import dev.paj.towatchtonight.data.source.local.DatabaseAccess;

@Module
public class DatabaseAccessModule {

    @Singleton
    @Provides
    DatabaseAccess provideDatabaseHelper(WatchlistDatabaseHelper databaseHelper) {
        return new DatabaseAccess(databaseHelper);
    }

}
