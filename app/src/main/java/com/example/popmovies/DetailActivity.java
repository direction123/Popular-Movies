package com.example.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import com.example.popmovies.utilities.MovieJsonUtils;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {
    private String baseImageUrl = "http://image.tmdb.org/t/p/w185/";
    private TextView mMovieTitle;
    private TextView movieReleaseDate;
    private ImageView mMoviePoster;
    private TextView mMovieVoteAverage;
    private TextView mMoviePlotSynopsis;
    private String mMovieData;
    private JSONObject mMovieJSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieTitle = (TextView) findViewById(R.id.movie_title);
        movieReleaseDate = (TextView) findViewById(R.id.movie_release_date);
        mMoviePoster = (ImageView) findViewById(R.id.movie_poster);
        mMovieVoteAverage = (TextView) findViewById(R.id.movie_vote_average);
        mMoviePlotSynopsis = (TextView) findViewById(R.id.movie_plot_synopsis);


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

                String plotSynopsis = MovieJsonUtils.getPlotSynopsis(mMovieJSON);
                mMoviePlotSynopsis.setText(plotSynopsis);
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.detail_name);
    }
}
