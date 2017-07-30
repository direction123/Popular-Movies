package com.example.popmovies.utilities;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by fangxiangwang on 7/14/17.
 */

public final class MovieJsonUtils {
    final static String MOVIE_ID_TITLE = "id";
    final static String TITLE_TITLE = "title";
    final static String RELEASE_DATE_TITLE = "release_date";
    final static String POST_TITLE = "poster_path";
    final static String VOTE_AVERAGE_TITLE = "vote_average";
    final static String PLOT_SYNOPSIS_TITLE = "overview";
    final static String TRAILER_KEY_TITLE = "key";
    final static String TRAILER_SITE_TITLE = "site";
    final static String TRAILER_YOUTUBE_SITE = "YouTube";
    final static String TRAILER_PLAY_URL_BASE = "https://www.youtube.com/watch?v=";

    public static String[] getMovieListJsonStrings (String movieJsonStr) throws JSONException {
        final String MOVIE_LIST = "results";

        String[] movieListJSONString;

        JSONObject movieJson = new JSONObject(movieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);

        movieListJSONString = new String[movieArray.length()];
        for (int i = 0; i < movieArray.length(); i++) {
            movieListJSONString[i] = movieArray.getJSONObject(i).toString();
        }
        return movieListJSONString;
    }

    public static JSONObject getJsonFromString (String jsonStr) {
        JSONObject movieJson = null;
        try {
            movieJson = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieJson;
    }

    public static int getMoiveID(JSONObject movieJson) {
        int id = -1;
        if (movieJson != null) {
            try {
                id = movieJson.getInt(MOVIE_ID_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    public static String getTitle(JSONObject movieJson) {
        String title = null;
        if (movieJson != null) {
            try {
                title = movieJson.getString(TITLE_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return title;
    }

    public static String getReleaseDate(JSONObject movieJson) {
        String releaseDate = null;
        if (movieJson != null) {
            try {
                releaseDate = movieJson.getString(RELEASE_DATE_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return releaseDate;
    }

    public static String getPostUrl(JSONObject movieJson) {
        String postUrl = null;
        if (movieJson != null) {
            try {
                postUrl = movieJson.getString(POST_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return postUrl;
    }

    public static String getVoteAverage(JSONObject movieJson) {
        String voteAverage = null;
        if (movieJson != null) {
            try {
                voteAverage = movieJson.getString(VOTE_AVERAGE_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return voteAverage;
    }

    public static String getPlotSynopsis(JSONObject movieJson) {
        String plotSynopsis = null;
        if (movieJson != null) {
            try {
                plotSynopsis = movieJson.getString(PLOT_SYNOPSIS_TITLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return plotSynopsis;
    }

    public static String getThumbnailUrl(JSONObject movieTrailerJson) {
        String thumbnailUrl = null;
        if (movieTrailerJson != null) {
            try {
                String trailerKey = movieTrailerJson.getString(TRAILER_KEY_TITLE);
                String trailerSite = movieTrailerJson.getString(TRAILER_SITE_TITLE);
                if (trailerSite.equals(TRAILER_YOUTUBE_SITE)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("http://img.youtube.com/vi/");
                    sb.append(trailerKey);
                    sb.append("/0.jpg");
                    thumbnailUrl = sb.toString();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return thumbnailUrl;
    }

    public static String getTrailerUrl(JSONObject movieTrailerJson) {
        String trailerlUrl = null;
        if (movieTrailerJson != null) {
            try {
                String trailerKey = movieTrailerJson.getString(TRAILER_KEY_TITLE);

                StringBuilder sb = new StringBuilder();
                sb.append(TRAILER_PLAY_URL_BASE);
                sb.append(trailerKey);
                trailerlUrl = sb.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return trailerlUrl;
    }
}
