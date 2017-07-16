package com.example.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

import com.example.popmovies.utilities.NetworkUtils;
import com.example.popmovies.utilities.OpenMovieJsonUtils;

import org.json.JSONException;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_movies);
        GridLayoutManager layoutManager;
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this, 2);
        }
        else{
            layoutManager = new GridLayoutManager(this, 4);
        }

        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setHasFixedSize(true);

        mMovieAdapter = new MovieAdapter(this);
        mRecyclerView.setAdapter(mMovieAdapter);

        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator);

        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_display);

        loadMovieData(R.string.sort_popular);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sort_popular) {
            mMovieAdapter.setMoiveData(null);
            loadMovieData(R.string.sort_popular);
            return true;
        }
        if (id == R.id.sort_top_rated) {
            mMovieAdapter.setMoiveData(null);
            loadMovieData(R.string.sort_top_rated);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String singleMovie) {
        Context context = this;
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(context, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, singleMovie);
        startActivity(intentToStartDetailActivity);
    }


    private void loadMovieData(Integer sortParam) {
        new FetchMovieTask().execute(sortParam);
    }

    private void showMovieDataView () {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
    }

    private void showErrorMessageView () {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    public class FetchMovieTask extends AsyncTask<Integer, Void, String[]> {
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
                String[] singleMovieJsonStrings = OpenMovieJsonUtils.getSingleMovieJsonStrings(jsonMovieResponse);
                return singleMovieJsonStrings;
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
}
