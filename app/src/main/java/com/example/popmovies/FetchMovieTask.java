package com.example.popmovies;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popmovies.utilities.NetworkUtils;
import com.example.popmovies.utilities.MovieListJsonUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;

/**
 * Created by fangxiangwang on 7/16/17.
 */

public class FetchMovieTask extends AsyncTask<Integer, Void, String[]> {
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageDisplay;

    public FetchMovieTask (RecyclerView recyclerView, MovieAdapter movieAdapter,
                           ProgressBar loadingIndicator, TextView errorMessageDisplay ) {
        mRecyclerView = recyclerView;
        mMovieAdapter = movieAdapter;

        mLoadingIndicator = loadingIndicator;
        mErrorMessageDisplay = errorMessageDisplay;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mLoadingIndicator.setVisibility(View.VISIBLE);
    }

    @Override
    protected String[] doInBackground(Integer... params) {
        if (params.length == 0) {
            return null;
        }

        Integer sortParam = params[0];
        URL moviePopRequestUrl = NetworkUtils.buildUrl(sortParam);
        try {
            String jsonMovieResponse = NetworkUtils.getResponseFromHttpUrl(moviePopRequestUrl);
            String[] singleMovieJsonStrings = MovieListJsonUtils.getMovieListJsonStrings(jsonMovieResponse);
            return singleMovieJsonStrings;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void showMovieDataView () {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessageView () {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onPostExecute(String[] movieData) {
        mLoadingIndicator.setVisibility(View.INVISIBLE);
        if (movieData != null) {
            showMovieDataView ();
            mMovieAdapter.setMoiveData(movieData);
        } else {
            //show error if there is no internet connection
            showErrorMessageView();
        }
    }
}
