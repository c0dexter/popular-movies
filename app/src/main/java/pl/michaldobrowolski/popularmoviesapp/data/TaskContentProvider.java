package pl.michaldobrowolski.popularmoviesapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Objects;

import static pl.michaldobrowolski.popularmoviesapp.data.TaskContract.FavouritesListEntry.TABLE_NAME;

public class TaskContentProvider extends ContentProvider {

    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private TaskDbHelper mTaskDbHelper;

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_FAVOURITES, TASKS);
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_FAVOURITES + "/#", TASK_WITH_ID);

        return uriMatcher;
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mTaskDbHelper = new TaskDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        final SQLiteDatabase db = mTaskDbHelper.getReadableDatabase();

        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch (match) {
            case TASKS:
                cursor = db.query(TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            case TASK_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = TaskContract.FavouritesListEntry.COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{id};

                cursor = db.query(TABLE_NAME,
                        projection,
                        mSelection,
                        mSelectionArgs,
                        null,
                        null,
                        sortOrder);
                break;
            default:
                throw new UnsupportedOperationException("Error! Unknown uri: " + uri);
        }
        cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case TASKS:
                return "vnd.android.cursor.dir" + "/" +
                        TaskContract.AUTHORITY + "/" + TaskContract.PATH_FAVOURITES;
            case TASK_WITH_ID:
                return "vnd.android.cursor.item" + "/" +
                        TaskContract.AUTHORITY + "/" + TaskContract.PATH_FAVOURITES;
            default:
                throw new UnsupportedOperationException("Error! Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;

        switch (match) {
            case TASKS:
                long id = database.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    returnUri = ContentUris.withAppendedId(TaskContract.FavouritesListEntry.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Error! Unknown uri: " + uri);
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = mTaskDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        int movieDeleted;
        switch (match) {
            case TASK_WITH_ID:
                String id = uri.getPathSegments().get(1);
                String mSelection = TaskContract.FavouritesListEntry.COLUMN_MOVIE_ID + "=?";
                String[] mSelectionArgs = new String[]{id};
                movieDeleted = db.delete(TABLE_NAME, mSelection, mSelectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Error! Unknown uri: " + uri);
        }
        if (movieDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return movieDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

}
