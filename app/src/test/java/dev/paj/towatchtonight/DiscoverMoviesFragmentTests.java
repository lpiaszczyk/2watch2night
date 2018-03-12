package dev.paj.towatchtonight;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;
import java.util.List;

import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.ui.discoverMovies.DiscoverMoviesFragment;
import dev.paj.towatchtonight.ui.discoverMovies.SelectVoteAverageDialog;

import static org.robolectric.shadows.ShadowToast.getTextOfLatestToast;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class DiscoverMoviesFragmentTests {

    @Test
    public void whenMoviesListEmpty_ErrorsAreShownInView() {
        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        TextView errorView = (TextView)fragment.getView().findViewById(R.id.tv_discover_movies_view_error);

        fragment.showError(RemoteSourceError.NETWORK_UNAVAILABLE);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_network));

        fragment.showError(RemoteSourceError.OTHER);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_other));

        fragment.showError(RemoteSourceError.SERVER_ERROR);
        Assert.assertEquals(errorView.getText(), RuntimeEnvironment.application.getString(R.string.remote_error_server));
    }

    @Test
    public void whenMoviesListNotEmpty_ErrorsAreShownInToast() {
        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
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
        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        ArrayList<Movie> moviesArray = TestUtils.setupMovieDataForTest(1);
        fragment.showMoviesList(moviesArray);
        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(R.id.rv_movies_discover_list);

        Assert.assertNotNull(recyclerView);
        Assert.assertNotNull(recyclerView.getAdapter());
    }

    @Test
    public void addMoviesToList_ExtendsExistingMoviesList() {
        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);

        fragment.showMoviesList(TestUtils.setupMovieDataForTest(1));
        fragment.addMoviesToList(TestUtils.setupMovieDataForTest(1));

        RecyclerView recyclerView = (RecyclerView)fragment.getView().findViewById(R.id.rv_movies_discover_list);
        int itemCount = recyclerView.getAdapter().getItemCount();
        Assert.assertEquals("Second array was not added" ,itemCount, 2);
    }

    @Test
    public void WhenOnVoteAverageSelected_VoteAverageViewUpdated() {
        final Context context = RuntimeEnvironment.application;

        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        TextView voteAverageTextView = (TextView)fragment.getView().findViewById(R.id.discover_movies_vote_average_picker);

        SelectVoteAverageDialog voteAverageDialogMock = Mockito.mock(SelectVoteAverageDialog.class);
        Mockito.when(voteAverageDialogMock.getVoteFrom()).thenReturn(1);
        Mockito.when(voteAverageDialogMock.getVoteTo()).thenReturn(10);

        fragment.onVoteAverageSelected(voteAverageDialogMock);
        Assert.assertEquals(voteAverageTextView.getText().toString(),
                context.getString(R.string.dialog_select_vote_range_values, 1, 10));
    }

    @Test
    public void WhenOnGenreSelected_GenresViewUpdated() {
        final Context context = RuntimeEnvironment.application;

        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        TextView genresView = (TextView)fragment.getView().findViewById(R.id.discover_movies_genres_picker);

        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre("1", "name");
        genres.add(genre);

        fragment.onGenreSelected(genres);
        Assert.assertEquals(genresView.getText().toString(),
                context.getResources().getQuantityString(R.plurals.genres, genres.size(), genres.size()));
    }

    @Test
    public void WhenOnGenreSelectedWithEmptyList_GenresViewUpdated() {
        final Context context = RuntimeEnvironment.application;

        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        TextView genresView = (TextView)fragment.getView().findViewById(R.id.discover_movies_genres_picker);

        fragment.onGenreSelected(new ArrayList<>());
        Assert.assertEquals(genresView.getText().toString(),
                context.getText(R.string.discover_filters_tap_to_select_genres));
    }

    @Test
    public void WhenGenresSelectionClearClicked_GenresViewUpdated() {
        final Context context = RuntimeEnvironment.application;

        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        TextView genresView = (TextView)fragment.getView().findViewById(R.id.discover_movies_genres_picker);
        Button clearButton = (Button)fragment.getView().findViewById(R.id.discover_movies_genres_clear);

        genresView.setText("Test text");

        clearButton.callOnClick();

        Assert.assertEquals(genresView.getText().toString(),
                context.getText(R.string.discover_filters_tap_to_select_genres));
    }

    @Test
    public void WhenVoteSelectionClearClicked_VotesViewUpdated() {
        final Context context = RuntimeEnvironment.application;

        DiscoverMoviesFragment fragment = new DiscoverMoviesFragment();
        SupportFragmentTestUtil.startVisibleFragment(fragment);
        TextView votesView = (TextView)fragment.getView().findViewById(R.id.discover_movies_vote_average_picker);
        Button clearButton = (Button)fragment.getView().findViewById(R.id.discover_movies_vote_average_clear);

        votesView.setText("Test text");

        clearButton.callOnClick();

        Assert.assertEquals(votesView.getText().toString(),
                context.getText(R.string.discover_filters_tap_to_select_vote_average));
    }
}
