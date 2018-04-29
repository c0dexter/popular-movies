package pl.michaldobrowolski.popularmoviesapp.data;


import android.net.Uri;
import android.provider.BaseColumns;

public class TaskContract {


    public static final String AUTHORITY = "pl.michaldobrowolski.popularmoviesapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVOURITES = "favourites";

    public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_FAVOURITES).build();

    public static final class FavouritesListEntry implements BaseColumns{
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_ID = "movieID";
        public static final String COLUMN_MOVIE_POSTER_PATH = "moviePosterPath";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "movieReleaseDate";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE = "movieVoteAverage";
        public static final String COLUMN_MOVIE_VOTE_COUNT = "movieVoteCount";
    }

}


