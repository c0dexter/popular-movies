package pl.michaldobrowolski.popularmoviesapp.api.service;

import android.util.Log;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {


    private static final String TAG = ApiClient.class.getClass().getClass().getSimpleName();

    public static Retrofit getClient() {
        final String apiKey = ""; //TODO: remove API Key before makeing a push

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                HttpUrl httpUrl = original.url();

                HttpUrl newHttpUrl = httpUrl.newBuilder().addQueryParameter("api_key", apiKey).build();

                Request.Builder requestBuilder = original.newBuilder().url(newHttpUrl);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        }).build();

        String BASE_URL = "http://api.themoviedb.org/";
        Retrofit retrofit = new Retrofit.Builder().client(client)
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d(TAG, "Retrofit client has been built successfully.");
        return retrofit;
    }

}


