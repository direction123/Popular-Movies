package com.example.popmovies.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.view.View.OnClickListener;

import com.example.popmovies.R;
import com.squareup.picasso.Picasso;
import com.example.popmovies.utilities.MovieJsonUtils;

/**
 * Created by fangxiangwang on 7/13/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder>{
    private String[] mMovieData;
    private String baseImageUrl = "http://image.tmdb.org/t/p/w185/";
    private final MovieAdapterOnClickHandler mClickHandler;

    public interface MovieAdapterOnClickHandler {
        void onClick(String singleMovie);
    }

    public MovieAdapter(MovieAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @Override
    public int getItemCount() {
        if (mMovieData == null)
            return 0;
        return mMovieData.length;
    }


    @Override
    public MovieAdapter.MovieAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(R.layout.movie_item, parent, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieAdapterViewHolder holder, int position) {
        String postUrl = MovieJsonUtils.getPostUrl(MovieJsonUtils.getJsonFromString(mMovieData[position]));
        ImageView mImageView = holder.mMovieView;
        String fullUrl = baseImageUrl + postUrl;
        Picasso.with(mImageView.getContext())
                .load(fullUrl)
                .into(mImageView);

    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements OnClickListener{
        public final ImageView mMovieView;

        public MovieAdapterViewHolder(View view) {
            super(view);
            mMovieView = (ImageView) view.findViewById(R.id.movie_poster_thumb);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String singleMovie = mMovieData[adapterPosition];
            mClickHandler.onClick(singleMovie);
        }
    }

    public void setMoiveData(String[] movieData) {
        mMovieData = movieData;
        //Notifies the attached observers that the underlying data has been changed and any View reflecting the data set should refresh
        notifyDataSetChanged();
    }
}
