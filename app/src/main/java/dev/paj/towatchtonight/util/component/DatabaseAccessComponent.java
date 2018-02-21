package dev.paj.towatchtonight.util.component;

import javax.inject.Singleton;

import dagger.Component;
import dev.paj.towatchtonight.data.source.local.DatabaseAccess;
import dev.paj.towatchtonight.util.module.DatabaseAccessModule;
import dev.paj.towatchtonight.util.module.WatchlistDatabaseHelperModule;

@Singleton
@Component(dependencies = {AppContextComponent.class}, modules = {DatabaseAccessModule.class, WatchlistDatabaseHelperModule.class})
public interface DatabaseAccessComponent {
    DatabaseAccess provideDatabaseAccess();
}
