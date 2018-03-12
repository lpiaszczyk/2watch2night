package dev.paj.towatchtonight;

import com.annimon.stream.Optional;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.data.source.local.DatabaseAccess;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsActivityContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsActivityPresenter;
import io.reactivex.Observable;

import static dev.paj.towatchtonight.TestUtils.prepareSynchronousRxSchedulers;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MovieDetailsActivityPresenterTests {

    @Mock
    private MovieDetailsActivityContract.View view;
    @Mock
    private DatabaseAccess databaseAccess;

    @Rule
    public ExpectedException thrown= ExpectedException.none();

    @BeforeClass
    public static void beforeClass() {
        prepareSynchronousRxSchedulers();
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkIfMoviePresentOnWatchlist_WhenMoviePresentOnWatchlist_ViewGetsResult() {
        when(databaseAccess.checkIfMovieIsPresentInWatchlist(any())).thenReturn(Observable.just(true));
        MovieDetailsActivityPresenter presenter = new MovieDetailsActivityPresenter(view, databaseAccess);

        presenter.checkIfMovieIsPresentInWatchlist(mock(Movie.class));
        verify(view).markMovieAsPresentOnWatchlist(true);
    }

    @Test
    public void checkIfMoviePresentOnWatchlist_WhenMovieNotPresentOnWatchlist_ViewGetsResult() {
        when(databaseAccess.checkIfMovieIsPresentInWatchlist(any())).thenReturn(Observable.just(false));
        MovieDetailsActivityPresenter presenter = new MovieDetailsActivityPresenter(view, databaseAccess);

        presenter.checkIfMovieIsPresentInWatchlist(mock(Movie.class));
        verify(view).markMovieAsPresentOnWatchlist(false);
    }

    @Test
    public void checkIfMoviePresentOnWatchlist_WhenMovieParameterIsNull_MethodThrowsException() {
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Movie cannot be null");
        MovieDetailsActivityPresenter presenter = new MovieDetailsActivityPresenter(view, databaseAccess);
        presenter.checkIfMovieIsPresentInWatchlist(null);
    }

    @Test
    public void addMovieToWatchlist_WhenAddSucceed_ShouldNotifyViewWithResult() {
        when(databaseAccess.addMovieToWatchList(any(), any())).thenReturn(Observable.just(Optional.of(true)));
        MovieDetailsActivityPresenter presenter = new MovieDetailsActivityPresenter(view, databaseAccess);

        presenter.addMovieToWatchlist(mock(Movie.class), mock(MovieDetails.class));
        verify(view).onMovieAddToWatchlistSuccess();
    }

    @Test
    public void addMovieToWatchlist_WhenAddFailed_ShouldCallViewShowError() {
        when(databaseAccess.addMovieToWatchList(any(), any())).thenReturn(Observable.just(Optional.of(false)));
        MovieDetailsActivityPresenter presenter = new MovieDetailsActivityPresenter(view, databaseAccess);

        presenter.addMovieToWatchlist(mock(Movie.class), mock(MovieDetails.class));
        verify(view).showAddToWatchlistError();
    }

    @Test
    public void removeMovieFromWatchlist_WhenRemoveSucceed_ShouldNotifyViewWithResult() {
        when(databaseAccess.removeMovieFromDatabase(any())).thenReturn(Observable.just(Optional.of(true)));
        MovieDetailsActivityPresenter presenter = new MovieDetailsActivityPresenter(view, databaseAccess);

        presenter.removeMovieFromWatchlist(mock(Movie.class));
        verify(view).onMovieRemoveFromWatchlistSuccess();
    }

    @Test
    public void removeMovieFromWatchlist_WhenRemoveFailed_ShouldCallViewShowError() {
        when(databaseAccess.removeMovieFromDatabase(any())).thenReturn(Observable.just(Optional.of(false)));
        MovieDetailsActivityPresenter presenter = new MovieDetailsActivityPresenter(view, databaseAccess);

        presenter.removeMovieFromWatchlist(mock(Movie.class));
        verify(view).showRemoveFromWatchlistError();
    }
}
