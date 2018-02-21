package dev.paj.towatchtonight;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import dev.paj.towatchtonight.data.model.movie.MovieVideo;
import dev.paj.towatchtonight.data.model.movie.MovieVideoResults;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsVideosPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.Single;

import static dev.paj.towatchtonight.TestUtils.prepareSynchronousRxSchedulers;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieDetailsVideosPresenterTests {

    @BeforeClass
    public static void beforeClass() {
        prepareSynchronousRxSchedulers();
    }

    @Test
    public void getMovieVideosData_WhenSourceCallSucceed_CallsViewShowMethod() {
        MovieDetailsContract.MovieVideosView view = mock(MovieDetailsContract.MovieVideosView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        MovieVideoResults results = mock(MovieVideoResults.class);
        ArrayList<MovieVideo> videos = setupMovieVideosForTest(5);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(results.getResults()).thenReturn(videos);
        when(remoteSource.getMovieVideos(anyString())).thenReturn(Single.just(results));

        MovieDetailsVideosPresenter presenter = new MovieDetailsVideosPresenter(view, remoteSource, networkUtils);
        presenter.getMovieVideosData("1");

        Mockito.verify(view).populateMovieVideosCard(videos);
    }

    @Test
    public void getMovieVideosData_WhenSourceCallFailed_CallsViewShowErrorMethod() {
        MovieDetailsContract.MovieVideosView view = mock(MovieDetailsContract.MovieVideosView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(remoteSource.getMovieVideos(anyString())).thenReturn(Single.error(new Exception()));

        MovieDetailsVideosPresenter presenter = new MovieDetailsVideosPresenter(view, remoteSource, networkUtils);
        presenter.getMovieVideosData("1");

        Mockito.verify(view).showMovieVideosError(RemoteSourceError.SERVER_ERROR);
    }

    @Test
    public void whenNoInternetConnection_ErrorShown() {
        MovieDetailsContract.MovieVideosView view = mock(MovieDetailsContract.MovieVideosView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(false);

        MovieDetailsVideosPresenter presenter = new MovieDetailsVideosPresenter(view, remoteSource, networkUtils);
        presenter.getMovieVideosData("1");

        Mockito.verify(view).showMovieVideosError(RemoteSourceError.NETWORK_UNAVAILABLE);
    }

    private ArrayList<MovieVideo> setupMovieVideosForTest(int arraySize) {
        ArrayList<MovieVideo> videos = new ArrayList<>(arraySize);
        for(int i = 0; i < arraySize; i++) {
            MovieVideo video = mock(MovieVideo.class);
            when(video.getSite()).thenReturn("YouTube");
            when(video.getType()).thenReturn("Trailer");
            videos.add(video);
        }

        return videos;
    }
}
