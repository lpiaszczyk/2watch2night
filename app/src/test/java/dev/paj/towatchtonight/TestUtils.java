package dev.paj.towatchtonight;

import org.mockito.Mockito;

import java.util.ArrayList;

import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import io.reactivex.Scheduler;
import io.reactivex.android.plugins.RxAndroidPlugins;
import io.reactivex.internal.schedulers.ExecutorScheduler;
import io.reactivex.plugins.RxJavaPlugins;

class TestUtils {
    static void prepareSynchronousRxSchedulers() {
        Scheduler immediate = new Scheduler() {
            @Override
            public Worker createWorker() {
                return new ExecutorScheduler.ExecutorWorker(Runnable::run);
            }
        };
        RxJavaPlugins.setInitIoSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitComputationSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitNewThreadSchedulerHandler(scheduler -> immediate);
        RxJavaPlugins.setInitSingleSchedulerHandler(scheduler -> immediate);
        RxAndroidPlugins.setInitMainThreadSchedulerHandler(scheduler -> immediate);
    }

    static Movie getSampleMovieData() {
        return new Movie("1", "asd", "asd", "asd", "asd");
    }

    static ArrayList<Movie> setupMovieDataForTest(int arraySize) {
        ArrayList<Movie> movies = new ArrayList<>(arraySize);
        for(int i = 0; i < arraySize; i++) {
            Movie movie = Mockito.mock(Movie.class);
            Mockito.when(movie.getId()).thenReturn(String.valueOf(arraySize));
            Mockito.when(movie.getOriginalTitle()).thenReturn("Movie number " + String.valueOf(arraySize));
            Mockito.when(movie.getPosterPath()).thenReturn("/path");
            Mockito.when(movie.getReleaseDate()).thenReturn("date-date-date");
            movies.add(movie);
        }

        return movies;
    }

    static MovieDetails getSampleMovieDetailsData() {
        return new MovieDetails("plot plot", "123", "3.40", new Genre[]{new Genre("1", "name")});
    }
}