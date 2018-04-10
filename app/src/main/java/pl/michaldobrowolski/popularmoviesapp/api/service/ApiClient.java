package pl.michaldobrowolski.popularmoviesapp.api.service;

import pl.michaldobrowolski.popularmoviesapp.api.Model.pojo.MultipleResource;
import retrofit2.Call;

public class ApiClient {
    private String apiKey = "";
    private ApiInterface apiInterface;
    private Call<MultipleResource> call;

    // TODO: I would like to use this class for executing search methods without populating API KEY

    public Call searchMostPopularMovies(){

        call = apiInterface.mostPopularMovies(this.apiKey);
        return call;
   }

   public Call searchTopRatedMovies(){

        call = apiInterface.topRatedMovies(this.apiKey);
        return call;
   }

}


