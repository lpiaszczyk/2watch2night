package dev.paj.towatchtonight.data.source.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class WatchlistDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "watchList.db";

    public WatchlistDatabaseHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        addMoviesTable(sqLiteDatabase);
        addGenresTable(sqLiteDatabase);
        addMoviesToGenresTable(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void addMoviesTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + WatchListContract.MovieEntry.TABLE_NAME + " ("
                + WatchListContract.MovieEntry._ID + " INTEGER PRIMARY KEY, "
                + WatchListContract.MovieEntry.COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + WatchListContract.MovieEntry.COLUMN_ORIGINAL_TITLE + " TEXT, "
                + WatchListContract.MovieEntry.COLUMN_TITLE + " TEXT, "
                + WatchListContract.MovieEntry.COLUMN_OVERVIEW + " TEXT, "
                + WatchListContract.MovieEntry.COLUMN_POSTER + " TEXT, "
                + WatchListContract.MovieEntry.COLUMN_RELEASE_DATE + " TEXT, "
                + WatchListContract.MovieEntry.COLUMN_RUNTIME + " TEXT, "
                + WatchListContract.MovieEntry.COLUMN_VOTE_AVG + " TEXT);";
        db.execSQL(sql);
    }

    private void addGenresTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + WatchListContract.GenreEntry.TABLE_NAME + " ("
                + WatchListContract.GenreEntry._ID + " INTEGER PRIMARY KEY, "
                + WatchListContract.GenreEntry.COLUMN_GENRE_ID + " TEXT UNIQUE NOT NULL, "
                + WatchListContract.GenreEntry.COLUMN_GENRE_DESC + " TEXT UNIQUE NOT NULL);";
        db.execSQL(sql);
    }

    private void addMoviesToGenresTable(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + WatchListContract.MovieToGenreEntry.TABLE_NAME + " ("
                + WatchListContract.MovieToGenreEntry._ID + " INTEGER PRIMARY KEY, "
                + WatchListContract.MovieToGenreEntry.COLUMN_GENRE_ID + " TEXT NOT NULL, "
                + WatchListContract.MovieToGenreEntry. COLUMN_MOVIE_ID + " TEXT NOT NULL, "
                + "FOREIGN KEY(" + WatchListContract.MovieToGenreEntry.COLUMN_GENRE_ID + ") REFERENCES "
                + WatchListContract.GenreEntry.TABLE_NAME + "(" + WatchListContract.GenreEntry._ID + "), "
                + "FOREIGN KEY(" + WatchListContract.MovieToGenreEntry.COLUMN_MOVIE_ID + ") REFERENCES "
                + WatchListContract.MovieEntry.TABLE_NAME + "(" + WatchListContract.MovieEntry._ID + "));";
        db.execSQL(sql);
    }
}
