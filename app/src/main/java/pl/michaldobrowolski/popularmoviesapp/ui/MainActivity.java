package pl.michaldobrowolski.popularmoviesapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import pl.michaldobrowolski.popularmoviesapp.api.Model.pojo.MultipleResource;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiFactor;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiInterface;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.Adapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.michaldobrowolski.popularmoviesapp.api.Model.pojo.MultipleResource.Movie.getMoviePosterUrl;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getClass().getSimpleName();

    ApiInterface apiInterface;
    //ApiClient apiClient;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<MultipleResource.Movie> mMovieItems;
    private RecyclerView.LayoutManager mLayoutManager;
    private Call call;
    String API_KEY = ""; //TODO: REMOVE it before commit
    //TODO: MOVE this to Shared Preferences

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        apiInterface = ApiFactor.getClient().create(ApiInterface.class);
        //apiClient = ApiFactor.getClient().create(ApiClient.class);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        searchOption(2);
        call.enqueue(new Callback<MultipleResource>() {
            @Override
            public void onResponse(@NonNull Call<MultipleResource> call, @NonNull Response<MultipleResource> response) {
                Log.d("LOG: Response Code: ", response.code() + "");

                MultipleResource resource = response.body();
                Integer text = resource.page;
                Integer total = resource.totalResults;
                Integer totalPages = resource.totalPages;
                List<MultipleResource.Movie> moviesList = resource.resultMovieItems;
                mMovieItems = new ArrayList<>();
                mAdapter = new Adapter(mMovieItems, getApplicationContext());
                mRecyclerView.setAdapter(mAdapter);

                for (MultipleResource.Movie movie : moviesList) {
                    MultipleResource.Movie movieResut = new MultipleResource.Movie(getMoviePosterUrl("w185", movie.posterPath));
                    mMovieItems.add(movieResut);
                }
            }

            @Override
            public void onFailure(Call<MultipleResource> call, Throwable t) {
                call.cancel();
            }
        });
    }

    /**
     * This method set the proper endpoint of search based on taken options
     * @param searchOption - [int] 0 for MOST POPULAR movies, 1 - for TOP RATED movies
     */
    public void searchOption(int searchOption) {

        switch (searchOption) {
            case 0:
                call = apiInterface.mostPopularMovies(API_KEY);
                return;
            case 1:
                call = apiInterface.topRatedMovies(API_KEY);
                return;
            default:
                call = apiInterface.mostPopularMovies(API_KEY);
        }
    }

}





