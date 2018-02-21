package dev.paj.towatchtonight;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.model.movie.MovieResults;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.DiscoverMoviesFragmentContract;
import dev.paj.towatchtonight.presenter.discoverMovies.DiscoverMoviesFragmentPresenter;
import dev.paj.towatchtonight.util.network.DiscoverMoviesParamsMapUtil;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.Single;

import static dev.paj.towatchtonight.TestUtils.prepareSynchronousRxSchedulers;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DiscoverMoviesFragmentPresenterTests {

    @Mock
    DiscoverMoviesFragmentContract.View view;
    @Mock
    RemoteMovieSource source;
    @Mock
    NetworkUtils networkUtils;

    @BeforeClass
    public static void beforeClass() {
        prepareSynchronousRxSchedulers();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void WhenInitMoviesListWithQueryParamsCalled_ViewIsUpdated() {
        DiscoverMoviesFragmentPresenter presenter = new DiscoverMoviesFragmentPresenter(view, source, networkUtils);

        Date dateFrom = new Date();
        Date dateTo = new Date();

        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre("1", "name");
        genres.add(genre);

        int voteFrom = 0;
        int voteTo = 10;
        Map<String, String> params = DiscoverMoviesParamsMapUtil.create(dateFrom, dateTo, voteFrom, voteTo, genres);

        MovieResults testResults = Mockito.mock(MovieResults.class);

        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        Mockito.when(source.discoverMovies(anyInt(), eq(params))).thenReturn(Single.just(testResults));

        presenter.initMoviesList(dateFrom, dateTo, voteFrom, voteTo, genres);

        verify(view).showMoviesList(anyList());
    }

    @Test
    public void WhenLoadNextPageIsCalled_TheSameParamsAreUsed() {
        DiscoverMoviesFragmentPresenter presenter = new DiscoverMoviesFragmentPresenter(view, source, networkUtils);

        Date dateFrom = new Date();
        Date dateTo = new Date();

        List<Genre> genres = new ArrayList<>();
        Genre genre = new Genre("1", "name");
        genres.add(genre);

        int voteFrom = 0;
        int voteTo = 10;
        Map<String, String> params = DiscoverMoviesParamsMapUtil.create(dateFrom, dateTo, voteFrom, voteTo, genres);

        MovieResults testResults = Mockito.mock(MovieResults.class);

        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        Mockito.when(source.discoverMovies(anyInt(), eq(params))).thenReturn(Single.just(testResults));

        presenter.initMoviesList(dateFrom, dateTo, voteFrom, voteTo, genres);

        presenter.loadNextPage();
        verify(source).discoverMovies(eq(2), eq(params));
        verify(view).addMoviesToList(anyList());
    }
}
