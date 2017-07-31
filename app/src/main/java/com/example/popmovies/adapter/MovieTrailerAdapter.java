package com.example.popmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.popmovies.R;
import com.example.popmovies.utilities.MovieJsonUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by fangxiangwang on 7/30/17.
 */

public class MovieTrailerAdapter extends RecyclerView.Adapter<MovieTrailerAdapter.MovieTrailerAdapterViewHolder>{
    private String[] mMovieTrailerData;
    private Context mContext;

    public MovieTrailerAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public int getItemCount() {
        if (mMovieTrailerData == null)
            return 0;
        return mMovieTrailerData.length;
    }


    @Override
    public MovieTrailerAdapter.MovieTrailerAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_trailer_item, parent, shouldAttachToParentImmediately);
        return new MovieTrailerAdapter.MovieTrailerAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieTrailerAdapter.MovieTrailerAdapterViewHolder holder, int position) {
        ImageView movieTrailerPreviewView = holder.mMovieTrailerPreviewView;
        String thumbnailUrl = MovieJsonUtils.getThumbnailUrl(MovieJsonUtils.getJsonFromString(mMovieTrailerData[position]));
        if (thumbnailUrl != null) {
            Picasso.with(movieTrailerPreviewView.getContext())
                    .load(thumbnailUrl)
                    .into(movieTrailerPreviewView);
        }
        final String trailerUrl = MovieJsonUtils.getTrailerUrl(MovieJsonUtils.getJsonFromString(mMovieTrailerData[position]));
        movieTrailerPreviewView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(trailerUrl));
                mContext.startActivity(intent);
            }});

        ImageView playView = holder.mPlayIconView;
        playView.setImageResource(R.drawable.ic_play_circle);
    }

    public class MovieTrailerAdapterViewHolder extends RecyclerView.ViewHolder {
        public final ImageView mMovieTrailerPreviewView;
        public final ImageView mPlayIconView;

        public MovieTrailerAdapterViewHolder(View view) {
            super(view);
            mMovieTrailerPreviewView = (ImageView) view.findViewById(R.id.play_preview);
            mPlayIconView = (ImageView) view.findViewById(R.id.play_icon);
        }
    }

    public void setMoiveTrailerData(String[] movieTrailerData) {
        mMovieTrailerData = movieTrailerData;
        //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh
        notifyDataSetChanged();
    }
}
