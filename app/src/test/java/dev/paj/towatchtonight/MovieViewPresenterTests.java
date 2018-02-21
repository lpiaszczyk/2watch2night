package dev.paj.towatchtonight;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import dev.paj.towatchtonight.data.model.MoviesListsCriteria;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieResults;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MoviesViewContract;
import dev.paj.towatchtonight.presenter.mainMoviesList.MoviesViewPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.Single;

import static dev.paj.towatchtonight.TestUtils.prepareSynchronousRxSchedulers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieViewPresenterTests {

    @BeforeClass
    public static void beforeClass() {
        prepareSynchronousRxSchedulers();
    }

    @Test
    public void initMoviesList_WhenSourceCallSucceed_CallsViewShowMethod() {
        MoviesViewContract.View view = mock(MoviesViewContract.View.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        ArrayList<Movie> moviesArray = setupMovieDataForTest(1);
        MovieResults movieResults = mock(MovieResults.class);
        when(movieResults.getResults()).thenReturn(moviesArray);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(remoteSource.getMoviesList(MoviesListsCriteria.POPULAR, 1)).thenReturn(Single.just(movieResults));

        MoviesViewPresenter presenter = new MoviesViewPresenter(view, remoteSource, networkUtils);
        presenter.initMoviesList();

        Mockito.verify(view).showMoviesList(moviesArray);
    }

    @Test
    public void initMoviesList_OnFailure_CallsShowErrorMethod() {
        MoviesViewContract.View view = mock(MoviesViewContract.View.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(remoteSource.getMoviesList(MoviesListsCriteria.POPULAR, 1)).thenReturn(Single.error(new Exception()));

        MoviesViewPresenter presenter = new MoviesViewPresenter(view, remoteSource, networkUtils);
        presenter.initMoviesList();

        Mockito.verify(view).showError(RemoteSourceError.SERVER_ERROR);
    }

    @Test
    public void loadNextPage_OnSuccess_CallsAddMoviesMethod() {
        MoviesViewContract.View view = Mockito.mock(MoviesViewContract.View.class);
        RemoteMovieSource remoteSource = Mockito.mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        ArrayList<Movie> moviesArray = setupMovieDataForTest(1);
        ArrayList<Movie> moviesArray2 = setupMovieDataForTest(2);
        MovieResults movieResults = mock(MovieResults.class);
        MovieResults movieResults2 = mock(MovieResults.class);

        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(movieResults.getResults()).thenReturn(moviesArray);
        when(movieResults2.getResults()).thenReturn(moviesArray2);
        when(remoteSource.getMoviesList(MoviesListsCriteria.POPULAR, 1)).thenReturn(Single.just(movieResults));
        when(remoteSource.getMoviesList(MoviesListsCriteria.POPULAR, 2)).thenReturn(Single.just(movieResults2));

        final MoviesViewPresenter presenter = new MoviesViewPresenter(view, remoteSource, networkUtils);

        presenter.initMoviesList();
        presenter.loadNextPage();

        Mockito.verify(view).addMoviesToList(moviesArray2);
    }

    @Test
    public void updateMoviesListCriteria_UpdatesView() {
        MoviesViewContract.View view = Mockito.mock(MoviesViewContract.View.class);
        RemoteMovieSource remoteSource = Mockito.mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        ArrayList<Movie> moviesArray = setupMovieDataForTest(1);
        MovieResults movieResults = mock(MovieResults.class);
        when(movieResults.getResults()).thenReturn(moviesArray);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(remoteSource.getMoviesList(any(MoviesListsCriteria.class), anyInt())).thenReturn(Single.just(movieResults));

        final MoviesViewPresenter presenter = new MoviesViewPresenter(view, remoteSource, networkUtils);

        presenter.updateListCriteria(MoviesListsCriteria.POPULAR);
        presenter.updateListCriteria(MoviesListsCriteria.TOPRATED);

        Mockito.verify(view, Mockito.times(2)).showMoviesList(moviesArray);
    }

    @Test
    public void whenNoInternetConnection_ErrorShown() {
        MoviesViewContract.View view = mock(MoviesViewContract.View.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(false);

        MoviesViewPresenter presenter = new MoviesViewPresenter(view, remoteSource, networkUtils);
        presenter.initMoviesList();

        Mockito.verify(view).showError(RemoteSourceError.NETWORK_UNAVAILABLE);
    }


    private ArrayList<Movie> setupMovieDataForTest(int arraySize) {
        ArrayList<Movie> movies = new ArrayList<>(arraySize);
        for(int i = 0; i < arraySize; i++) {
            Movie movie = mock(Movie.class);
            movies.add(movie);
        }

        return movies;
    }
}
