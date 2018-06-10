package pl.michaldobrowolski.popularmoviesapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pl.michaldobrowolski.popularmoviesapp.data.TaskContract.FavouritesListEntry;

class TaskDbHelper extends SQLiteOpenHelper {

    // Set: DB name, DB version, constructor
    private static final String DATA_BASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 7; // TODO: increment this if structure of DB would be changed

    TaskDbHelper(Context context){
        super(context, DATA_BASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_FAVOURITES_LIST_TABLE = "CREATE TABLE " +
                FavouritesListEntry.TABLE_NAME + " (" +
                FavouritesListEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FavouritesListEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                FavouritesListEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL," +
                FavouritesListEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL," +
                FavouritesListEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL," +
                FavouritesListEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL," +
                FavouritesListEntry.COLUMN_MOVIE_VOTE_COUNT + " TEXT NOT NULL" +
                ");";

        db.execSQL(SQL_CREATE_FAVOURITES_LIST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // If DB exists: remove old one and create new empty one
        db.execSQL("DROP TABLE IF EXISTS " + FavouritesListEntry.TABLE_NAME);
        onCreate(db);
    }
}
