package dev.paj.towatchtonight;

import android.support.v4.app.Fragment;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import dev.paj.towatchtonight.presenter.discoverMovies.DiscoverMoviesActivityPresenter;
import dev.paj.towatchtonight.ui.discoverMovies.DiscoverMoviesActivity;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class DiscoverMoviesActivityPresenterTests {

    @Test
    public void loadInitView_LoadsMoviesViewFragment() {
        DiscoverMoviesActivity mockedActivity = Robolectric.buildActivity(DiscoverMoviesActivity.class).create().visible().get();
        DiscoverMoviesActivityPresenter presenter = new DiscoverMoviesActivityPresenter(mockedActivity);
        presenter.loadInitView(mockedActivity, R.id.movie_discover_activity_frame_layout, "test tag");
        Fragment fragment = mockedActivity.getSupportFragmentManager().findFragmentByTag("test tag");
        Assert.assertNotNull("MoviesViewFragment was not loaded", fragment);
    }
}
