package dev.paj.towatchtonight;

import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowToast;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesViewFragment;

import static org.robolectric.shadows.ShadowToast.getTextOfLatestToast;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class MoviesViewFragmentTests {

    @Test
    public void showMoviesList_MoviesListIsShown() {
        MoviesViewFragment fragment = new MoviesViewFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        ArrayList<Movie> moviesArray = TestUtils.setupMovieDataForTest(1);
        fragment.showMoviesList(moviesArray);
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(R.id.rv_movies_view_list);

        Assert.assertNotNull(recyclerView);
        Assert.assertNotNull(recyclerView.getAdapter());
    }

    @Test
    public void addMoviesToList_ExtendsExistingMoviesList() {
        MoviesViewFragment fragment = new MoviesViewFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        fragment.showMoviesList(TestUtils.setupMovieDataForTest(1));
        fragment.addMoviesToList(TestUtils.setupMovieDataForTest(1));

        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(R.id.rv_movies_view_list);
        int itemCount = recyclerView.getAdapter().getItemCount();
        Assert.assertEquals("Second array was not added" ,itemCount, 2);
    }

    @Test
    public void whenMoviesListEmpty_ErrorsAreShownInView() {
        MoviesViewFragment fragment = new MoviesViewFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        TextView errorView = (TextView)fragment.getView().findViewById(R.id.tv_movies_view_error);

        fragment.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_network));

        fragment.showError(RemoteSourceError.OTHER);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_other));

        fragment.showError(RemoteSourceError.SERVER_ERROR);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_server));
    }

    @Test
    public void whenMoviesListNotEmpty_ErrorsAreShownInToast() {
        MoviesViewFragment fragment = new MoviesViewFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        ArrayList<Movie> moviesArray = TestUtils.setupMovieDataForTest(1);

        fragment.showMoviesList(moviesArray);

        fragment.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        Assert.assertEquals(getTextOfLatestToast(), RuntimeEnvironment.application.getString(R.string.remote_error_network));

        fragment.showError(RemoteSourceError.OTHER);
        Assert.assertEquals(getTextOfLatestToast(), RuntimeEnvironment.application.getString(R.string.remote_error_other));

        fragment.showError(RemoteSourceError.SERVER_ERROR);
        Assert.assertEquals(getTextOfLatestToast(), RuntimeEnvironment.application.getString(R.string.remote_error_server));
    }

    @Test
    public void whenStartMovieDetailsIndexOutOfBound_ErrorToastIsShown() {
        MoviesViewFragment fragment = new MoviesViewFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        ArrayList<Movie> moviesArray = TestUtils.setupMovieDataForTest(1);

        fragment.showMoviesList(moviesArray);

        fragment.startDetailsView(1);
        String messageShown = ShadowToast.getTextOfLatestToast();
        String messageExpected = fragment.getString(R.string.movie_details_start_error);

        Assert.assertEquals("Toast shown is different than expected", messageShown, messageExpected);
    }

}
