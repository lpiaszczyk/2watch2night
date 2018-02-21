package dev.paj.towatchtonight;

import android.support.v4.app.Fragment;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import dev.paj.towatchtonight.presenter.mainMoviesList.MainActivityPresenter;
import dev.paj.towatchtonight.ui.mainMoviesList.MainActivity;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class MainActivityPresenterTests {

    @Test
    public void loadInitView_LoadsMoviesViewFragment() {
        MainActivity mockedActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        MainActivityPresenter presenter = new MainActivityPresenter(mockedActivity);
        presenter.loadInitView(mockedActivity, R.id.main_activity_frame_layout, "test tag");
        Fragment fragment = mockedActivity.getSupportFragmentManager().findFragmentByTag("test tag");
        Assert.assertNotNull("MoviesViewFragment was not loaded", fragment);
    }
}
