package com.example.popmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
/**
 * Created by fangxiangwang on 7/28/17.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    private static final int DATABASE_VERSION = 4;

    public MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_FAVORITE_MOVIE_TABLE =

                "CREATE TABLE " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME + " (" +

                        FavoriteMovieContract.FavoriteMovieEntry._ID               + " INTEGER PRIMARY KEY AUTOINCREMENT, " +

                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID       + " INTEGER NOT NULL, "                 +

                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POST + " STRING,"                  +

                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE   + " STRING, "                    +

                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS   + " STRING, "                    +

                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE   + " STRING, "                    +

                        FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_RATING  + " STRING, "                    +

                        " UNIQUE (" + FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + FavoriteMovieContract.FavoriteMovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
