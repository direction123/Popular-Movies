package com.example.popmovies.async;

import android.os.AsyncTask;

import com.example.popmovies.adapter.MovieTrailerAdapter;
import com.example.popmovies.utilities.MovieJsonUtils;
import com.example.popmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by fangxiangwang on 7/30/17.
 */

public class FetchMovieTrailerTask extends AsyncTask<String, Void, String[]> {
    private MovieTrailerAdapter mMovieTrailerAdapter;

    public FetchMovieTrailerTask (MovieTrailerAdapter movieTrailerAdapter) {
        mMovieTrailerAdapter = movieTrailerAdapter;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String[] doInBackground(String... params) {
        if (params.length == 0) {
            return null;
        }

        String movieId = params[0];
        URL moviePopRequestUrl = NetworkUtils.buildTrailerUrl(movieId);
        try {
            String jsonMovieTrailerResponse = NetworkUtils.getResponseFromHttpUrl(moviePopRequestUrl);
            String[] movieTrailerListJsonStrings = MovieJsonUtils.getMovieListJsonStrings(jsonMovieTrailerResponse);
            return movieTrailerListJsonStrings;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(String[] movieData) {
        if (movieData != null) {
            mMovieTrailerAdapter.setMoiveTrailerData(movieData);
        }
    }

}
