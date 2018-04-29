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

    // Const values to using URI Matcher [UriMatcher - Step: 1]
    public static final int TASKS = 100;
    public static final int TASK_WITH_ID = 101;
    // Declare a static variable for Uri matcher that I construct in step #2 [UriMatcher - Step: 3]
    private static final UriMatcher sUriMacher = buildUriMatcher();
    private TaskDbHelper mTaskDbHelper;

    // Define a static method "buildUriMatcher" (based on int match values) [UriMatcher - Step: 2]
    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        // Directory (All)
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_FAVOURITES, TASKS);
        // Single item (by using a specific ID -> "/#"
        uriMatcher.addURI(TaskContract.AUTHORITY, TaskContract.PATH_FAVOURITES + "/#", TASKS);

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
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final SQLiteDatabase database = mTaskDbHelper.getWritableDatabase();
        int match = sUriMacher.match(uri);

        Uri returnUri;

        switch (match) {
            case TASKS:
                long id = database.insert(TABLE_NAME, null, values);
                if (id > 0) {
                    // Success
                    returnUri = ContentUris.withAppendedId(TaskContract.CONTENT_URI, id);
                } else {
                    throw new android.database.SQLException("Failed to insert into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Objects.requireNonNull(getContext()).getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
