package dev.paj.towatchtonight;

import android.widget.FrameLayout;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import dev.paj.towatchtonight.ui.mainMoviesList.MainActivity;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesViewFragment;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class MainActivityTests {

    @Test
    public void onCreate_FragmentIsLoaded() {
        MainActivity mainActivity = Robolectric.buildActivity(MainActivity.class).create().visible().get();
        FrameLayout frameLayout = (FrameLayout)mainActivity.findViewById(R.id.main_activity_frame_layout);
        Assert.assertNotNull("Layout for fragment not visible", frameLayout);
        MoviesViewFragment fragment = (MoviesViewFragment)mainActivity.getSupportFragmentManager().getFragments().get(0);
        Assert.assertNotNull("MoviesViewFragment not present", fragment);
    }
}
