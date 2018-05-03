package pl.michaldobrowolski.popularmoviesapp.utils;

import android.net.Uri;
import android.util.Log;

import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.TrailerListRes;

public class UtilityHelper {

    private static final String TAG = UtilityHelper.class.getSimpleName();

    public static class sUrlContainer{
        final static String BASE_YOUTUBE_MOVIE_QUERY = "https://www.youtube.com/watch?v=";
        final static String BASE_YOUTUBE_MOVIE_THUMBNAIL_URL = "https://img.youtube.com/vi/";
        final static String BASE_SUFFIX_URL = "/0.jpg";

        public static String getBaseYoutubeMovieQuery() {
            return BASE_YOUTUBE_MOVIE_QUERY;
        }

        public static String getBaseYoutubeMovieThumbnailUrl() {
            return BASE_YOUTUBE_MOVIE_THUMBNAIL_URL;
        }

        public static String getBaseSuffixUrl() {
            return BASE_SUFFIX_URL;
        }
    }

    /**
     * This method is using for getting the specific URL to poster image of a specific movie
     * @return String of valid URL of image based of poster_path
     */
    public String getMoviePosterUrl(Movie movie) {
        String url = "";
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("image.tmdb.org")
                .appendPath("t")
                .appendPath("p")
                .appendPath("w185") // Recommended size of movie poster
                .appendEncodedPath(movie.getPosterPath());

        try {
            url = builder.build().toString();

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return url;
    }

    /**
     * This method is using for generating a proper trailer URL of Youtube movie
     * @param trailerListRes - trailer's list
     * @return an URL of specific thumbnail of Youtube movie
     */
    public String getTrailerThumbnailUrl(TrailerListRes trailerListRes){
        return sUrlContainer.BASE_YOUTUBE_MOVIE_THUMBNAIL_URL + trailerListRes.getKey() + sUrlContainer.BASE_SUFFIX_URL;
    }
}
