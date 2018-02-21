package dev.paj.towatchtonight.data.source.local;

import android.provider.BaseColumns;

public class WatchListContract {

    public static final class MovieEntry implements BaseColumns {

        public static final String TABLE_NAME = "moviesWatchlist";
        public static final String COLUMN_MOVIE_ID = "tmdbId";
        public static final String COLUMN_ORIGINAL_TITLE = "originalTitle";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_POSTER = "posterPath";
        public static final String COLUMN_OVERVIEW = "plotOverview";
        public static final String COLUMN_RUNTIME = "runtime";
        public static final String COLUMN_VOTE_AVG = "voteAverage";

    }

    public static final class GenreEntry implements BaseColumns {

        public static final String TABLE_NAME = "genres";
        public static final String COLUMN_GENRE_ID = "movieDbId";
        public static final String COLUMN_GENRE_DESC = "description";
    }

    public static final class MovieToGenreEntry implements BaseColumns {

        public static final String TABLE_NAME = "movieToGenre";
        public static final String COLUMN_GENRE_ID = "genreId";
        public static final String COLUMN_MOVIE_ID = "movieId";

    }

}
