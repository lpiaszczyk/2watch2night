package dev.paj.towatchtonight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsActivityPresenter;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsActivity;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class MovieDetailsActivityTests {

    @Mock
    MovieDetailsActivityPresenter presenterMock;
    @InjectMocks
    MovieDetailsActivity activityWithMockPresenter;

    @Test
    public void onCreate_MovieCardsAreLoaded() {
        final Context context = RuntimeEnvironment.application;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(context.getString(R.string.movie_parcelable_name), TestUtils.getSampleMovieData());
        intent.putExtras(bundle);

        MovieDetailsActivity activity = Robolectric.buildActivity(MovieDetailsActivity.class, intent)
                .create()
                .visible()
                .get();

        String [] fragmentTitles = new String[] {
                context.getString(R.string.movie_details_card_details),
                context.getString(R.string.movie_details_card_videos),
                context.getString(R.string.movie_details_card_reviews)
        };

        TabLayout tabLayout = (TabLayout)activity.findViewById(R.id.movie_details_tab_layout);
        Assert.assertEquals(tabLayout.getTabCount(), 3);

        for(int i = 0; i < fragmentTitles.length; i++) {
            Assert.assertEquals(tabLayout.getTabAt(i).getText(), fragmentTitles[i]);
        }
    }

    @Test
    public void WhenOnMovieDetailsLoadedCalled_PresenterChecksIfMovieIsInWatchlist() {
        MockitoAnnotations.initMocks(this);
        activityWithMockPresenter.onMovieDetailsLoaded(Mockito.any(MovieDetails.class));
        Mockito.verify(presenterMock).checkIfMovieIsPresentInWatchlist(Mockito.any());
    }
}
