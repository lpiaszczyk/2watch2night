package dev.paj.towatchtonight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.presenter.searchMovies.SearchMoviesFragmentPresenter;
import dev.paj.towatchtonight.ui.searchMovies.SearchMoviesFragment;

import static org.robolectric.shadows.ShadowToast.getTextOfLatestToast;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class SearchMoviesFragmentTests {

    @Mock
    SearchMoviesFragmentPresenter presenterMock;
    @InjectMocks
    SearchMoviesFragment freagmentWithMockPresenter;

    @Test
    public void whenMoviesListEmpty_ErrorsAreShownInView() {
        SearchMoviesFragment fragment = new SearchMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        TextView errorView = (TextView)fragment.getView().findViewById(R.id.tv_search_movies_view_error);

        fragment.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_network));

        fragment.showError(RemoteSourceError.OTHER);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_other));

        fragment.showError(RemoteSourceError.SERVER_ERROR);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_server));
    }

    @Test
    public void whenMoviesListNotEmpty_ErrorsAreShownInToast() {
        SearchMoviesFragment fragment = new SearchMoviesFragment();
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
    public void showMoviesList_MoviesListIsShown() {
        SearchMoviesFragment fragment = new SearchMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        ArrayList<Movie> moviesArray = TestUtils.setupMovieDataForTest(1);
        fragment.showMoviesList(moviesArray);
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(R.id.rv_movies_search_list);

        Assert.assertNotNull(recyclerView);
        Assert.assertNotNull(recyclerView.getAdapter());
    }

    @Test
    public void addMoviesToList_ExtendsExistingMoviesList() {
        SearchMoviesFragment fragment = new SearchMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        fragment.showMoviesList(TestUtils.setupMovieDataForTest(1));
        fragment.addMoviesToList(TestUtils.setupMovieDataForTest(1));

        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(R.id.rv_movies_search_list);
        int itemCount = recyclerView.getAdapter().getItemCount();
        Assert.assertEquals("Second array was not added" ,itemCount, 2);
    }

    @Test
    public void WhenClearQueryClicked_QueryBoxIsCleared() {
        SearchMoviesFragment fragment = new SearchMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        EditText queryBox = (EditText)fragment.getView().findViewById(R.id.search_movies_query_text);
        Button clearButton = (Button)fragment.getView().findViewById(R.id.search_movies_query_clear);

        queryBox.setText("test query text");
        clearButton.callOnClick();

        Assert.assertEquals(queryBox.getText().toString(), "");
    }

    @Test
    public void WhenSearchClickedWithoutQueryText_ErrorShown() {
        final Context context = RuntimeEnvironment.application;
        SearchMoviesFragment fragment = new SearchMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        Button searchButton = (Button)fragment.getView().findViewById(R.id.search_movies_search);

        searchButton.callOnClick();

        Assert.assertEquals(getTextOfLatestToast(), context.getString(R.string.search_query_empty_error));
    }
}
