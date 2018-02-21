package dev.paj.towatchtonight.util.navigator;

import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

public class LoadFragmentIntoActivityNavigator implements Navigator {

    private Fragment fragmentToLoad;
    private AppCompatActivity caller;
    private int viewId;
    private String tag;

    @Override
    public void navigate() {
        caller.getSupportFragmentManager()
                .beginTransaction()
                .add(viewId, fragmentToLoad, tag)
                .commit();
    }

    public LoadFragmentIntoActivityNavigator(@NonNull Fragment fragmentToLoad, @NonNull AppCompatActivity caller,
                                             @IdRes int containerId, @Nullable String tag) {
        this.fragmentToLoad = fragmentToLoad;
        this.caller = caller;
        this.viewId = containerId;
        this.tag = tag;
    }
}
