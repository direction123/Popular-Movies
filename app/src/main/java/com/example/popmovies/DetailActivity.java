package com.example.popmovies;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import com.example.popmovies.data.FavoriteMovieContract;
import com.example.popmovies.utilities.MovieJsonUtils;
import com.squareup.picasso.Picasso;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {
    private String baseImageUrl = "http://image.tmdb.org/t/p/w185/";
    @BindView(R.id.movie_title) TextView mMovieTitle;
    @BindView(R.id.movie_release_date) TextView movieReleaseDate;
    @BindView(R.id.movie_poster) ImageView mMoviePoster;
    @BindView(R.id.movie_vote_average) TextView mMovieVoteAverage;
    @BindView(R.id.movie_plot_synopsis) TextView mMoviePlotSynopsis;
    @BindView(R.id.movie_favorites) Button mMovieFavorite;
    @BindView(R.id.recyclerview_movie_trailers)
    RecyclerView mTrailerRecyclerView;

    private String mMovieData;
    private JSONObject mMovieJSON;
    private Uri mUri;

    private MovieTrailerAdapter mMovieTrailerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
                mMovieData = intentThatStartedThisActivity.getStringExtra(Intent.EXTRA_TEXT);
                mMovieJSON = MovieJsonUtils.getJsonFromString(mMovieData);

                mMovieTitle.setText(MovieJsonUtils.getTitle(mMovieJSON));

                movieReleaseDate.setText(MovieJsonUtils.getReleaseDate(mMovieJSON));

                String postUrl = MovieJsonUtils.getPostUrl(mMovieJSON);
                String fullUrl = baseImageUrl + postUrl;
                Picasso.with(mMoviePoster.getContext())
                        .load(fullUrl)
                        .into(mMoviePoster);

                String voteAverage = MovieJsonUtils.getVoteAverage(mMovieJSON) + "/10";
                mMovieVoteAverage.setText(voteAverage);

                mMoviePlotSynopsis.setText(MovieJsonUtils.getPlotSynopsis(mMovieJSON));

                mUri = buildUri(mMovieJSON);
                if (isFavorite(mUri)) {
                    mMovieFavorite.setText(R.string.button_unfavorite);
                } else {
                    mMovieFavorite.setText(R.string.button_favorite);
                }

                LinearLayoutManager layoutManager =
                        new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
                mTrailerRecyclerView.setLayoutManager(layoutManager);
                mTrailerRecyclerView.setHasFixedSize(true);
                mMovieTrailerAdapter = new MovieTrailerAdapter(this);
                mTrailerRecyclerView.setAdapter(mMovieTrailerAdapter);

                String movieID = String.valueOf(MovieJsonUtils.getMoiveID(mMovieJSON));
                loadMovieTrailer(movieID);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.detail_name);
    }

    public void toggleFavorites (View v) {
        Button button = (Button) v;
        String text = button.getText().toString();
        ContentResolver movieContentResolver = getContentResolver();
        if (text.equals(getString(R.string.button_favorite))) {
            ContentValues favoriteMovieValues = buildContentValues (mMovieJSON);
            movieContentResolver.insert(
                    FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI,
                    favoriteMovieValues
            );
            button.setText(getString(R.string.button_unfavorite));
        } else if (text.equals(getString(R.string.button_unfavorite))) {
            Uri singleUri =  buildUri(mMovieJSON);
            movieContentResolver.delete(
                    singleUri,
                    null,
                    null);
            button.setText(getString(R.string.button_favorite));
        }
    }

    private ContentValues buildContentValues (JSONObject mMovieJSON) {
        ContentValues movieValues = new ContentValues();
        String movieID = String.valueOf(MovieJsonUtils.getMoiveID(mMovieJSON));
        movieValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID, movieID);
        String post = MovieJsonUtils.getPostUrl(mMovieJSON);
        movieValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_POST, post);
        String releaseDate = MovieJsonUtils.getReleaseDate(mMovieJSON);
        movieValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_RELEASE_DATE, releaseDate);
        String synopsis = MovieJsonUtils.getPlotSynopsis(mMovieJSON);
        movieValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_SYNOPSIS, synopsis);
        String title = MovieJsonUtils.getTitle(mMovieJSON);
        movieValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_TITLE, title);
        String userRating = MovieJsonUtils.getVoteAverage(mMovieJSON);
        movieValues.put(FavoriteMovieContract.FavoriteMovieEntry.COLUMN_USER_RATING, userRating);
        return movieValues;
    }

    private Uri buildUri (JSONObject mMovieJSON) {
        Uri CONTENT_URI  = FavoriteMovieContract.FavoriteMovieEntry.CONTENT_URI;
        long movieID = MovieJsonUtils.getMoiveID(mMovieJSON);
        Uri singleUri = ContentUris.withAppendedId(CONTENT_URI, movieID);
        return singleUri;
    }

    private boolean isFavorite (Uri singleUri) {
        ContentResolver movieContentResolver = getContentResolver();
        final String[] MAIN_FORECAST_PROJECTION = {
                FavoriteMovieContract.FavoriteMovieEntry.COLUMN_MOVIE_ID
        };
        Cursor cursor = movieContentResolver.query(singleUri, MAIN_FORECAST_PROJECTION, null, null, null);
        if (!(cursor.moveToFirst()) || cursor.getCount() ==0){
            return false;
        }
        return true;
    }

    private void loadMovieTrailer(String movieId) {
        new FetchMovieTrailerTask(mMovieTrailerAdapter).execute(movieId);
    }
}
