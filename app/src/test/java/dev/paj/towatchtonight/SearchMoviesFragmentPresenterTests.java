package dev.paj.towatchtonight;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import dev.paj.towatchtonight.data.model.movie.MovieResults;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.dependencyContract.SearchMoviesFragmentContract;
import dev.paj.towatchtonight.presenter.searchMovies.SearchMoviesFragmentPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.Single;

import static dev.paj.towatchtonight.TestUtils.prepareSynchronousRxSchedulers;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SearchMoviesFragmentPresenterTests {

    @Mock
    SearchMoviesFragmentContract.View view;
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
    public void WhenInitMoviesListSucceed_ViewShowMoviesCalled() {
        SearchMoviesFragmentPresenter presenter = new SearchMoviesFragmentPresenter(view, source, networkUtils);

        MovieResults testResults = Mockito.mock(MovieResults.class);

        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        Mockito.when(source.searchMovies(anyInt(), anyString())).thenReturn(Single.just(testResults));

        presenter.initMoviesList("test");

        verify(view).showMoviesList(anyList());
    }

    @Test
    public void WhenLoadNextPageSucceed_TheSameQueryIsUsed() {
        SearchMoviesFragmentPresenter presenter = new SearchMoviesFragmentPresenter(view, source, networkUtils);

        MovieResults testResults = Mockito.mock(MovieResults.class);

        String query = "test query";

        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        Mockito.when(source.searchMovies(anyInt(), anyString())).thenReturn(Single.just(testResults));

        presenter.initMoviesList(query);
        presenter.loadNextPage();

        verify(source).searchMovies(eq(2), eq(query));
        verify(view).addMoviesToList(anyList());
    }
}
