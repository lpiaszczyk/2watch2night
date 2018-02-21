package dev.paj.towatchtonight.data.source.local;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import javax.inject.Inject;

import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class DatabaseAccess {

    private WatchlistDatabaseHelper databaseHelper;

    @Inject
    public DatabaseAccess(WatchlistDatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    public Observable<Boolean> addMovieToWatchList(Movie movie, MovieDetails movieDetails){
        return Observable.fromCallable(() -> insertMovieToWatchlistDatabaseCallable(movie, movieDetails))
                .subscribeOn(Schedulers.io());
    }

    private boolean insertMovieToWatchlistDatabaseCallable(Movie movie, MovieDetails movieDetails) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.beginTransaction();

        long movieId = 0;
        ContentValues movieContentValues = new ContentValues();
        movieContentValues.put(WatchListContract.MovieEntry.COLUMN_MOVIE_ID, movie.getId());
        movieContentValues.put(WatchListContract.MovieEntry.COLUMN_ORIGINAL_TITLE, movie.getOriginalTitle());
        movieContentValues.put(WatchListContract.MovieEntry.COLUMN_TITLE, movie.getTitle());
        movieContentValues.put(WatchListContract.MovieEntry.COLUMN_OVERVIEW, movieDetails.getPlotOverview());
        movieContentValues.put(WatchListContract.MovieEntry.COLUMN_POSTER, movie.getPosterPath());
        movieContentValues.put(WatchListContract.MovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        movieContentValues.put(WatchListContract.MovieEntry.COLUMN_RUNTIME, movieDetails.getRuntime());
        movieContentValues.put(WatchListContract.MovieEntry.COLUMN_VOTE_AVG, movieDetails.getVoteAverage());

        try {
            movieId = db.insertOrThrow(WatchListContract.MovieEntry.TABLE_NAME, null, movieContentValues);
        } catch (SQLException e) {
            db.endTransaction();
            return false;
        }


        ContentValues genreContentValues = new ContentValues();
        Genre[] genres = movieDetails.getGenres();
        long[] genreIds = new long[genres.length];
        for(int i = 0; i < genres.length; i++) {
            genreContentValues.put(WatchListContract.GenreEntry.COLUMN_GENRE_ID, genres[i].getGenreId());
            genreContentValues.put(WatchListContract.GenreEntry.COLUMN_GENRE_DESC, genres[i].getGenreName());
            try {
                db.insertWithOnConflict(WatchListContract.GenreEntry.TABLE_NAME, null, genreContentValues, SQLiteDatabase.CONFLICT_IGNORE);
                genreIds[i] = Long.parseLong(genres[i].getGenreId());
            } catch (SQLException f) {
                db.endTransaction();
                return false;
            }
            genreContentValues.clear();
        }

        ContentValues movieToGenreContentValues = new ContentValues();
        for(int i = 0; i < genres.length; i++) {
            movieToGenreContentValues.put(WatchListContract.MovieToGenreEntry.COLUMN_MOVIE_ID, movieId);
            movieToGenreContentValues.put(WatchListContract.MovieToGenreEntry.COLUMN_GENRE_ID, genreIds[i]);
            try {
                db.insertWithOnConflict(WatchListContract.MovieToGenreEntry.TABLE_NAME, null, movieToGenreContentValues, SQLiteDatabase.CONFLICT_IGNORE);
            } catch (SQLException f) {
                db.endTransaction();
                return false;
            }

            movieToGenreContentValues.clear();
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();
        return true;
    }

    public Observable<Boolean> removeMovieFromDatabase(Movie movie) {
        return Observable.fromCallable(() -> deleteMovieFromWatchlistDatabase(movie))
                .subscribeOn(Schedulers.io());
    }

    private boolean deleteMovieFromWatchlistDatabase(Movie movie) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor = db.query(WatchListContract.MovieEntry.TABLE_NAME,
                new String[]{WatchListContract.MovieEntry._ID},
                WatchListContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[] {movie.getId()},
                null,null,null);
        cursor.moveToFirst();
        long id = cursor.getLong(cursor.getColumnIndex(WatchListContract.MovieEntry._ID));
        cursor.close();

        db.beginTransaction();

        int movieResult = db.delete(WatchListContract.MovieEntry.TABLE_NAME,
                WatchListContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[] {movie.getId()});
        if(movieResult > 0) {
            db.delete(WatchListContract.MovieToGenreEntry.TABLE_NAME, WatchListContract.MovieToGenreEntry.COLUMN_MOVIE_ID + " = ? ",
                    new String[]{Long.toString(id)});
        } else {
            return false;
        }

        db.setTransactionSuccessful();
        db.endTransaction();
        db.close();

        return true;
    }

    public Observable<Boolean> checkIfMovieIsPresentInWatchlist(Movie movie) {
        return Observable.fromCallable((() -> findSingleMovieIdInWatchlistDatabase(movie) > 0))
                .subscribeOn(Schedulers.io());
    }

    private long findSingleMovieIdInWatchlistDatabase(Movie movie) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                WatchListContract.MovieEntry.TABLE_NAME,
                new String[]{WatchListContract.MovieEntry._ID},
                WatchListContract.MovieEntry.COLUMN_MOVIE_ID + "=?",
                new String[] {movie.getId()},
                null,
                null,
                null
        );
        long result = 0;
        if(cursor.moveToFirst()) {
            result = cursor.getLong(cursor.getColumnIndex(WatchListContract.MovieEntry._ID));
        }
        cursor.close();

        return result;
    }

    public Single<ArrayList<Movie>> getMoviesFromWatchlist() {
        return Single.fromCallable(this::getMoviesListFromWatchlistDatabase)
                .subscribeOn(Schedulers.io());
    }

    private ArrayList<Movie> getMoviesListFromWatchlistDatabase() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                WatchListContract.MovieEntry.TABLE_NAME,
                new String[]{WatchListContract.MovieEntry.COLUMN_MOVIE_ID,
                        WatchListContract.MovieEntry.COLUMN_ORIGINAL_TITLE,
                        WatchListContract.MovieEntry.COLUMN_TITLE,
                        WatchListContract.MovieEntry.COLUMN_POSTER,
                        WatchListContract.MovieEntry.COLUMN_RELEASE_DATE},
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<Movie> results = new ArrayList<>();
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                Movie movie = new Movie(cursor.getString(cursor.getColumnIndex(WatchListContract.MovieEntry.COLUMN_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(WatchListContract.MovieEntry.COLUMN_ORIGINAL_TITLE)),
                        cursor.getString(cursor.getColumnIndex(WatchListContract.MovieEntry.COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(WatchListContract.MovieEntry.COLUMN_RELEASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(WatchListContract.MovieEntry.COLUMN_POSTER)));
                results.add(movie);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return results;
    }

    public Single<MovieDetails> getMovieDetailsFromWatchlist(String movieId) {
        return Single.fromCallable(() -> findSingleMovieDetailsInWatchlistDatabase(movieId))
                .subscribeOn(Schedulers.io());
    }

    private MovieDetails findSingleMovieDetailsInWatchlistDatabase(String tmdbId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                WatchListContract.MovieEntry.TABLE_NAME,
                new String[]{
                WatchListContract.MovieEntry._ID,
                WatchListContract.MovieEntry.COLUMN_OVERVIEW,
                WatchListContract.MovieEntry.COLUMN_RUNTIME,
                WatchListContract.MovieEntry.COLUMN_VOTE_AVG
                },
                WatchListContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[] {tmdbId},
                null,
                null,
                null
        );
        MovieDetails result = null;
        if(cursor.moveToFirst()) {
            long movieId = cursor.getLong(cursor.getColumnIndex(WatchListContract.MovieEntry._ID));
            Genre[] genres = getGenresArrayForMovieIdFromWatchlistDatabase(movieId);
            result = new MovieDetails(
                    cursor.getString(cursor.getColumnIndex(WatchListContract.MovieEntry.COLUMN_OVERVIEW)),
                    cursor.getString(cursor.getColumnIndex(WatchListContract.MovieEntry.COLUMN_RUNTIME)),
                    cursor.getString(cursor.getColumnIndex(WatchListContract.MovieEntry.COLUMN_VOTE_AVG)),
                    genres);
        }
        return result;
    }

    private Genre[] getGenresArrayForMovieIdFromWatchlistDatabase(long movieId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                WatchListContract.MovieToGenreEntry.TABLE_NAME,
                new String[]{
                        WatchListContract.MovieToGenreEntry.COLUMN_GENRE_ID
                },
                WatchListContract.MovieToGenreEntry.COLUMN_MOVIE_ID + " = ? ",
                new String[] {Long.toString(movieId)},
                null,
                null,
                null
        );
        ArrayList<Genre> result = new ArrayList<>();
        if(cursor.moveToFirst()) {
            while(!cursor.isAfterLast()) {
                result.add(getGenreNameFromWatchlistDatabase(cursor.getLong(cursor.getColumnIndex(WatchListContract.MovieToGenreEntry.COLUMN_GENRE_ID))));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return result.toArray(new Genre[0]);

    }

    private Genre getGenreNameFromWatchlistDatabase(long genreId) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(
                WatchListContract.GenreEntry.TABLE_NAME,
                new String[]{
                        WatchListContract.GenreEntry.COLUMN_GENRE_DESC
                },
                WatchListContract.GenreEntry.COLUMN_GENRE_ID + " = ? ",
                new String[] {Long.toString(genreId)},
                null,
                null,
                null
        );
        Genre result = null;
        if(cursor.moveToFirst()) {
            result = new Genre(Long.toString(genreId), cursor.getString(cursor.getColumnIndex(WatchListContract.GenreEntry.COLUMN_GENRE_DESC)));
        }
        return result;
    }

}
