package dev.paj.towatchtonight;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsMovieCardPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.Single;

import static dev.paj.towatchtonight.TestUtils.prepareSynchronousRxSchedulers;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieDetailsMovieCardPresenterTests {

    @BeforeClass
    public static void beforeClass() {
        prepareSynchronousRxSchedulers();
    }

    @Test
    public void getMovieDetailsData_MovieAddedToWatchlist_CallsViewShowMethod() {
        MovieDetailsContract.MovieCardView view = mock(MovieDetailsContract.MovieCardView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        MovieDetails movieDetailsReturn = mock(MovieDetails.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(remoteSource.getMovieDetails(anyString())).thenReturn(Single.just(movieDetailsReturn));

        MovieDetailsMovieCardPresenter presenter = new MovieDetailsMovieCardPresenter(view, remoteSource, networkUtils);
        presenter.getMovieDetailsData("1");

        Mockito.verify(view).populateMovieDetailsCard(movieDetailsReturn);
    }

    @Test
    public void getMovieDetailsData_WhenSourceCallFailed_CallsViewShowErrorMethod() {
        MovieDetailsContract.MovieCardView view = mock(MovieDetailsContract.MovieCardView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(remoteSource.getMovieDetails(anyString())).thenReturn(Single.error(new Exception()));

        MovieDetailsMovieCardPresenter presenter = new MovieDetailsMovieCardPresenter(view, remoteSource, networkUtils);
        presenter.getMovieDetailsData("1");

        Mockito.verify(view).showMovieDetailsError(RemoteSourceError.SERVER_ERROR);
    }

    @Test
    public void whenNoInternetConnection_ErrorShown() {
        MovieDetailsContract.MovieCardView view = mock(MovieDetailsContract.MovieCardView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(false);

        MovieDetailsMovieCardPresenter presenter = new MovieDetailsMovieCardPresenter(view, remoteSource, networkUtils);
        presenter.getMovieDetailsData("1");

        Mockito.verify(view).showMovieDetailsError(RemoteSourceError.NETWORK_UNAVAILABLE);
    }
}
