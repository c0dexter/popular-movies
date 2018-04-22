package pl.michaldobrowolski.popularmoviesapp.data;


import android.provider.BaseColumns;

public class FavouritesListContract  {

    public static final class FavouritesListEntry implements BaseColumns{
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_MOVIE_TITLE = "movieTitle";
        public static final String COLUMN_MOVIE_ID = "movieID";

    }

}


