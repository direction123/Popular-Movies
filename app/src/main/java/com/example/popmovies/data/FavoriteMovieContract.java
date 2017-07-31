package com.example.popmovies.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by fangxiangwang on 7/28/17.
 */

public class FavoriteMovieContract {
    public static final String CONTENT_AUTHORITY = "com.example.popmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_FAVORITES = "favorites";

    public static final class FavoriteMovieEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVORITES)
                .build();

        public static final String TABLE_NAME = "favorites";

        public static final String COLUMN_MOVIE_ID = "id";

        public static final String COLUMN_TITLE = "title";

        public static final String COLUMN_POST = "poster_path";

        public static final String COLUMN_SYNOPSIS = "overview";

        public static final String COLUMN_USER_RATING = "vote_average";

        public static final String COLUMN_RELEASE_DATE = "release_date";
    }
}
