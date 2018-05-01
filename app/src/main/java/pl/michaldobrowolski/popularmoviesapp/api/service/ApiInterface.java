package pl.michaldobrowolski.popularmoviesapp.api.service;

import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.MovieListRes;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("3/movie/top_rated")
    Call<MovieListRes> topRatedMovies();

    @GET("3/movie/popular")
    Call<MovieListRes> mostPopularMovies();

    // TODO: GET for trailers
    @GET("3/movie/{movie_id}/videos")
    Call<MovieListRes> movieTrailers();

    // TODO: GET for opinions
}
