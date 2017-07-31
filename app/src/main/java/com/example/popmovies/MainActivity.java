package com.example.popmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.popmovies.adapter.MovieAdapter;
import com.example.popmovies.async.FetchMovieTask;
import com.example.popmovies.utilities.FavoriteMovieDBUtils;


public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler{
    private RecyclerView mRecyclerView;
    private MovieAdapter mMovieAdapter;

    private MenuItem mMenuItem;

    private ProgressBar mLoadingIndicator;

    private TextView mErrorMessageDisplay;

    private TextView mFavoriteEmptyMessageDisplay;

    private String[] mMovies;
    private String MOVIE_LIST;

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

        mFavoriteEmptyMessageDisplay = (TextView) findViewById(R.id.favorite_empty_message_display);

        if(savedInstanceState != null){
            CharSequence[] cs = savedInstanceState.getCharSequenceArray(MOVIE_LIST);
            if(cs != null) {
                int length = cs.length;
                mMovies = new String[length];
                for (int i = 0; i < length; i++) {
                    mMovies[i] = cs[i].toString();
                }
                mMovieAdapter.setMoiveData(mMovies);
            }
        } else {
            loadMovieData(R.string.sort_popular);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (mMenuItem != null) {
            int id = mMenuItem.getItemId();
            if (id == R.id.favorites) {
                mMovieAdapter.setMoiveData(null);
                loadFavoriteMovieData();
            }
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            CharSequence[] cs = savedInstanceState.getCharSequenceArray(MOVIE_LIST);
            if(cs != null) {
                int length = cs.length;
                mMovies = new String[length];
                for (int i = 0; i < length; i++) {
                    mMovies[i] = cs[i].toString();
                }
                mMovieAdapter.setMoiveData(mMovies);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMovies = mMovieAdapter.getMovieDate();
        outState.putCharSequenceArray(MOVIE_LIST, mMovies);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.sort_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        this.mMenuItem = item;
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
        if (id == R.id.favorites) {
            mMovieAdapter.setMoiveData(null);
            loadFavoriteMovieData();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(String singleMovie) {
        Class destinationClass = DetailActivity.class;
        Intent intentToStartDetailActivity = new Intent(this, destinationClass);
        intentToStartDetailActivity.putExtra(Intent.EXTRA_TEXT, singleMovie);
        startActivity(intentToStartDetailActivity);
    }


    private void loadMovieData(Integer sortParam) {
        new FetchMovieTask(mRecyclerView, mMovieAdapter, mLoadingIndicator, mErrorMessageDisplay).execute(sortParam);
    }

    private void loadFavoriteMovieData() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        String[] movieListJSONString = FavoriteMovieDBUtils.getMovieListJsonStrings(this);
        if (movieListJSONString == null) {
            mRecyclerView.setVisibility(View.INVISIBLE);
            mFavoriteEmptyMessageDisplay.setVisibility(View.VISIBLE);
        } else {
            mMovieAdapter.setMoiveData(movieListJSONString);
            mRecyclerView.setVisibility(View.VISIBLE);
            mFavoriteEmptyMessageDisplay.setVisibility(View.INVISIBLE);
        }
        return;
    }
}
