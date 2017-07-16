package com.example.popmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
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

    private String mMovieData;
    private JSONObject mMovieJSON;

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
            }
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.detail_name);
    }
}
