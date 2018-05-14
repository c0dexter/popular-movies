package pl.michaldobrowolski.popularmoviesapp.api.service;


import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.MovieListRes;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Review;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.ReviewList;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Trailer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("3/movie/top_rated")
    Call<MovieListRes> getTopRatedMovies();

    @GET("3/movie/popular")
    Call<MovieListRes> getMostPopularMovies();

    /**
     * movie_id is a dynamic parameter
     * it should be populated by specific movie's ID when those functions are called
     */
    @GET("3/movie/{movie_id}/videos")
    Call<Trailer> getMovieTrailers(@Path("movie_id") int id);

    @GET("3/movie/{movie_id}/reviews")
    Call<Review> getMovieReviews(@Path("movie_id") int id);
}
