package dev.paj.towatchtonight;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import dev.paj.towatchtonight.data.model.movie.MovieReview;
import dev.paj.towatchtonight.data.model.movie.MovieReviewResults;
import dev.paj.towatchtonight.data.source.remote.RemoteMovieSource;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsReviewsPresenter;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import io.reactivex.Single;

import static dev.paj.towatchtonight.TestUtils.prepareSynchronousRxSchedulers;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieDetailsReviewsPresenterTests {

    @BeforeClass
    public static void beforeClass() {
        prepareSynchronousRxSchedulers();
    }

    @Test
    public void getMovieReviewsData_WhenSourceCallSucceed_CallsViewShowMethod() {
        MovieDetailsContract.MovieReviewsView view = mock(MovieDetailsContract.MovieReviewsView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        MovieReviewResults results = mock(MovieReviewResults.class);
        ArrayList<MovieReview> reviews = setupMovieReviewsForTest(5);
        when(results.getResults()).thenReturn(reviews);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(remoteSource.getMovieReviews(anyString())).thenReturn(Single.just(results));

        MovieDetailsReviewsPresenter presenter = new MovieDetailsReviewsPresenter(view, remoteSource, networkUtils);
        presenter.getMovieReviewsData("1");

        Mockito.verify(view).populateMovieReviewsCard(reviews);
    }

    @Test
    public void getMovieReviewsData_WhenSourceCallFailed_CallsViewShowErrorMethod() {
        MovieDetailsContract.MovieReviewsView view = mock(MovieDetailsContract.MovieReviewsView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(true);
        when(remoteSource.getMovieReviews(anyString())).thenReturn(Single.error(new Exception()));

        MovieDetailsReviewsPresenter presenter = new MovieDetailsReviewsPresenter(view, remoteSource, networkUtils);
        presenter.getMovieReviewsData("1");

        Mockito.verify(view).showMovieReviewsError(RemoteSourceError.SERVER_ERROR);
    }

    @Test
    public void whenNoInternetConnection_ErrorShown() {
        MovieDetailsContract.MovieReviewsView view = mock(MovieDetailsContract.MovieReviewsView.class);
        RemoteMovieSource remoteSource = mock(RemoteMovieSource.class);
        NetworkUtils networkUtils = mock(NetworkUtils.class);
        when(networkUtils.isNetworkAvailable()).thenReturn(false);

        MovieDetailsReviewsPresenter presenter = new MovieDetailsReviewsPresenter(view, remoteSource, networkUtils);
        presenter.getMovieReviewsData("1");

        Mockito.verify(view).showMovieReviewsError(RemoteSourceError.NETWORK_UNAVAILABLE);
    }

    private ArrayList<MovieReview> setupMovieReviewsForTest(int arraySize) {
        ArrayList<MovieReview> reviews = new ArrayList<>(arraySize);
        for(int i = 0; i < arraySize; i++) {
            MovieReview review = mock(MovieReview.class);
            reviews.add(review);
        }

        return reviews;
    }
}
