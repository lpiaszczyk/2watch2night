package dev.paj.towatchtonight;

import android.widget.FrameLayout;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import dev.paj.towatchtonight.ui.discoverMovies.DiscoverMoviesActivity;
import dev.paj.towatchtonight.ui.discoverMovies.DiscoverMoviesFragment;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class DiscoverMoviesActivityTests {

    @Test
    public void onCreate_FragmentIsLoaded() {
        DiscoverMoviesActivity activity = Robolectric.buildActivity(DiscoverMoviesActivity.class).create().visible().get();
        FrameLayout frameLayout = (FrameLayout)activity.findViewById(R.id.movie_discover_activity_frame_layout);
        Assert.assertNotNull("Layout for fragment not visible", frameLayout);
        DiscoverMoviesFragment fragment = (DiscoverMoviesFragment)activity.getSupportFragmentManager().getFragments().get(0);
        Assert.assertNotNull("MoviesViewFragment not present", fragment);
    }
}
