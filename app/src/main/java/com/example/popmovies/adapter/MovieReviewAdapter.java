package com.example.popmovies.adapter;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.DialogInterface;

import com.example.popmovies.R;
import com.example.popmovies.utilities.MovieJsonUtils;

/**
 * Created by fangxiangwang on 7/30/17.
 */

public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewAdapterViewHolder>{
    private String[] mMovieReviewData;
    private Context mContext;

    public MovieReviewAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        if (mMovieReviewData == null)
            return 0;
        return mMovieReviewData.length;
    }


    @Override
    public MovieReviewAdapter.MovieReviewAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_review_item, parent, shouldAttachToParentImmediately);
        return new MovieReviewAdapter.MovieReviewAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewAdapter.MovieReviewAdapterViewHolder holder, int position) {
        TextView reviewAuthorView = holder.mReviewAuthorView;
        final String reviewAuthor = "A review by " +
                MovieJsonUtils.getReviewAuthor(MovieJsonUtils.getJsonFromString(mMovieReviewData[position]));
        reviewAuthorView.setText(reviewAuthor);

        TextView reviewTextView = holder.mReviewTextView;
        final String reviewText = MovieJsonUtils.getReviewText(MovieJsonUtils.getJsonFromString(mMovieReviewData[position]));
        reviewTextView.setText(reviewText);
        //reference: https://stackoverflow.com/questions/15721044/how-to-add-popup-window-for-long-texts-android
        reviewTextView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle(reviewAuthor);
                builder.setMessage(reviewText);
                builder.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
    }

    public class MovieReviewAdapterViewHolder extends RecyclerView.ViewHolder {
        public final TextView mReviewAuthorView;
        public final TextView mReviewTextView;

        public MovieReviewAdapterViewHolder(View view) {
            super(view);
            mReviewAuthorView = (TextView) view.findViewById(R.id.review_author);
            mReviewTextView = (TextView) view.findViewById(R.id.review_text);
        }
    }

    public void setMoiveReviewData(String[] movieMovieData) {
        mMovieReviewData = movieMovieData;
        //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh
        notifyDataSetChanged();
    }
}
