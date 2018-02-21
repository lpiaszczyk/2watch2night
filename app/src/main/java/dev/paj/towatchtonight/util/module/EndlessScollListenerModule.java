package dev.paj.towatchtonight.util.module;

import dagger.Module;
import dagger.Provides;
import dev.paj.towatchtonight.ui.endlessScroll.EndlessScrollList;
import dev.paj.towatchtonight.ui.endlessScroll.EndlessScrollListener;

@Module
public class EndlessScollListenerModule {
    private EndlessScrollList list;

    public EndlessScollListenerModule(EndlessScrollList list) {
        this.list = list;
    }

    @Provides
    public EndlessScrollListener provideEndlessScrollListener() {
        return new EndlessScrollListener(list);
    }
}
