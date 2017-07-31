package com.example.popmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

/**
 * Created by fangxiangwang on 7/28/17.
 */

public class MovieProvider extends ContentProvider {
    public static final int CODE_FACORITE_MOVIE = 100;
    public static final int CODE_FACORITE_MOVIE_WITH_ID = 101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDbHelper mOpenHelper;

    public static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = FavoriteMovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, FavoriteMovieContract.PATH_FAVORITES, CODE_FACORITE_MOVIE);
        matcher.addURI(authority, FavoriteMovieContract.PATH_FAVORITES + "/#", CODE_FACORITE_MOVIE_WITH_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case CODE_FACORITE_MOVIE_WITH_ID: {
                String normalizedUtcDateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedUtcDateString};
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments,
                        null,
                        null,
                        null);

                break;
            }
            case CODE_FACORITE_MOVIE: {
                cursor = mOpenHelper.getReadableDatabase().query(
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder);

                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);

        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int numRowsDeleted;
        switch (sUriMatcher.match(uri)) {

            case CODE_FACORITE_MOVIE_WITH_ID:
                String normalizedUtcDateString = uri.getLastPathSegment();
                String[] selectionArguments = new String[]{normalizedUtcDateString};
                numRowsDeleted = mOpenHelper.getWritableDatabase().delete(
                        FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + " = ? ",
                        selectionArguments);

                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        /* If we actually deleted any rows, notify that a change has occurred to this URI */
        if (numRowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numRowsDeleted;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        int uriType = sUriMatcher.match(uri);

        SQLiteDatabase sqlDB = mOpenHelper.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case CODE_FACORITE_MOVIE:
                id = sqlDB.insert(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME,
                        null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + "/" + id);
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("We are not implementing getType in Sunshine.");
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update in Sunshine");
    }
}
