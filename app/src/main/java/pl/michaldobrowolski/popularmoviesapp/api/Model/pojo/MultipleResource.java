package pl.michaldobrowolski.popularmoviesapp.api.Model.pojo;

import android.net.Uri;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MultipleResource {

    @SerializedName("page")
    public Integer page;
    @SerializedName("total_results")
    public Integer totalResults;
    @SerializedName("total_pages")
    public Integer totalPages;
    @SerializedName("results")
    public List<Movie> resultMovieItems = null;


    public static class Movie {
        private final static String TAG = Movie.class.getSimpleName();

        @SerializedName("vote_count")
        public Integer voteCount;
        @SerializedName("id")
        public Integer id;
        @SerializedName("video")
        public Boolean video;
        @SerializedName("vote_average")
        public Double voteAverage;
        @SerializedName("title")
        public String title;
        @SerializedName("popularity")
        public Double popularity;
        @SerializedName("poster_path")
        public String posterPath;
        @SerializedName("original_language")
        public String originalLanguage;
        @SerializedName("original_title")
        public String originalTitle;
        @SerializedName("genre_ids")
        public List<Integer> genreIds = null;
        @SerializedName("backdrop_path")
        public String backdropPath;
        @SerializedName("adult")
        public Boolean adult;
        @SerializedName("overview")
        public String overview;
        @SerializedName("release_date")
        public String releaseDate;

        // Constructor for test, only poster path //TODO: make a full constructor
        public Movie(String posterImageUrl) {
            this.posterPath = posterImageUrl;
        }


        /**
         * This method is using for getting the specific URL to poster image of a specific movie
         *
         * @param posterSize    - size of image
         * @param urlPosterPath - related path to poster image from json
         * @return String of valid URL of image based of poster_path
         */
        public static String getMoviePosterUrl(String posterSize, String urlPosterPath) {
            String url = "";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath(posterSize)
                    .appendEncodedPath(urlPosterPath);

            try {
                url = builder.build().toString();

            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
            return url;

        }
    }
}