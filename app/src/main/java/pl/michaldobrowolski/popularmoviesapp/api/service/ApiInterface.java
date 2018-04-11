package pl.michaldobrowolski.popularmoviesapp.api.service;

import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.MultipleResource;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    //******************************************************************************************//
    //                                       INSTRUCTIONS                                       //
    //******************************************************************************************//
    // API Key:                 ------------------------------------
    // BASE_URL:                https://api.themoviedb.org/3/movie/
    // Endpoint TOP_RATED:      /top_rated
    // Endpoint MOST_POPULAR:   /popular
    //
    //------------------SEARCHING------------------------------------------
    // Example URL TR:          https://api.themoviedb.org/3/movie/top_rated?api_key={API_KEY}
    // Example URL MP:          https://api.themoviedb.org/3/movie/popular?api_key={API_KEY}
    //
    //------------------PHOTOS---------------------------------------------
    // Suggested size:          w185
    // Example Poster Path:     http://image.tmdb.org/t/p/w185//jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg

    @GET("3/movie/top_rated")
    Call<MultipleResource> topRatedMovies();

    @GET("3/movie/popular")
    Call<MultipleResource> mostPopularMovies();
}
