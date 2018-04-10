package pl.michaldobrowolski.popularmoviesapp.api.service;

import android.util.Log;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiFactor {

    // #1 Zmienić nazwe api client na APIFactory
    // #2 Nie zmieniać metod w APIFactory
    // #3 Stworzyć nową klasę ApiClient
    // #4 Klasa ApiClient ma 2 metody, które robią to samo co API Interface ale nie wymagają podania klucza
    // ---------
    // Później używam tej nowej klasy i jej metod

    private static final String TAG = ApiFactor.class.getClass().getClass().getSimpleName();

    public static Retrofit getClient() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        String BASE_URL = "http://api.themoviedb.org/";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        Log.d(TAG, "Retrofit client has been built successfully.");
        return retrofit;
    }

}


