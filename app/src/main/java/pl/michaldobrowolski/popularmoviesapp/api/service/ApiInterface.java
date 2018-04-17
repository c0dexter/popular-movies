package pl.michaldobrowolski.popularmoviesapp.api.service;

import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.MultipleResource;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("3/movie/top_rated")
    Call<MultipleResource> topRatedMovies();

    @GET("3/movie/popular")
    Call<MultipleResource> mostPopularMovies();
}
