package com.example.popmovies.utilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fangxiangwang on 7/14/17.
 */

public final class OpenMovieJsonUtils {
    public static String[] getSingleMovieJsonStrings (String movieJsonStr) throws JSONException {
        final String MOVIE_LIST = "results";

        String[] singleMovieJSONString;

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);

        singleMovieJSONString = new String[movieArray.length()];
        for (int i = 0; i < movieArray.length(); i++) {
            singleMovieJSONString[i] = movieArray.getJSONObject(i).toString();
        }

        return singleMovieJSONString;
    }
}
