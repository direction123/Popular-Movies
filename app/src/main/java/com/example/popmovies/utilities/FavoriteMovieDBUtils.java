package com.example.popmovies.utilities;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.example.popmovies.data.FavoriteMovieContract;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fangxiangwang on 7/30/17.
 */

public class FavoriteMovieDBUtils {
    public static String[] getMovieListJsonStrings(Context context) {
        ContentResolver movieContentResolver = context.getContentResolver();
        final String[] MAIN_FORECAST_PROJECTION = {
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID,
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_RATING,
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE,
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS,
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE,
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POST
        };
        Cursor cursor = movieContentResolver.query(
                FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                MAIN_FORECAST_PROJECTION,
                null,
                null,
                null);
        String[] movieListJSONString;
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return null;
        }
        //Reference: https://stackoverflow.com/questions/25722585/convert-sqlite-to-json
        List<String> rowStringList = new ArrayList<>();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ ) {
                if( cursor.getColumnName(i) != null ) {
                    try {
                        if( cursor.getString(i) != null ) {
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        } else {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    } catch( Exception e ) {
                        e.printStackTrace();
                    }
                }
            }
            rowStringList.add(rowObject.toString());
            cursor.moveToNext();
        }
        cursor.close();
        String[] rowStringArray = new String[rowStringList.size()];
        movieListJSONString = rowStringList.toArray(rowStringArray);
        return movieListJSONString;
    }
}
