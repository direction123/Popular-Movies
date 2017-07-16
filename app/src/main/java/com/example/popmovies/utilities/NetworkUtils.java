package com.example.popmovies.utilities;

/**
 * Created by fangxiangwang on 7/14/17.
 */

import android.net.Uri;

import com.example.popmovies.BuildConfig;
import com.example.popmovies.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by fangxiangwang on 7/14/17.
 */

public final class NetworkUtils {
    private static final String POP_MOVIE_URL = "http://api.themoviedb.org/3/movie/popular";

    private static final String TOP_RATED_MOVIE_URL = "http://api.themoviedb.org/3/movie/top_rated";

    private static final String API_KEY_PARAM = "api_key";

    //add your own themoviedb api key here
    private static final String apiKey = BuildConfig.THE_MOVIE_DB_API_TOKEN;

    public static URL buildUrl (Integer options) {
        String baseUrl = null;
        switch (options) {
            case R.string.sort_popular:
                baseUrl = POP_MOVIE_URL;
                break;
            case R.string.sort_top_rated:
                baseUrl = TOP_RATED_MOVIE_URL;
                break;
        }
        Uri builtUri = Uri.parse(baseUrl).buildUpon()
                .appendQueryParameter(API_KEY_PARAM, apiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl (URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}


