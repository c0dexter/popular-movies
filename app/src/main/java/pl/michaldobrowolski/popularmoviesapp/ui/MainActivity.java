package pl.michaldobrowolski.popularmoviesapp.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.MultipleResource;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiClient;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiInterface;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.Adapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.michaldobrowolski.popularmoviesapp.api.model.pojo.MultipleResource.Movie.getMoviePosterUrl;

public class MainActivity extends AppCompatActivity {
    private final String TAG = this.getClass().getClass().getSimpleName();

    ApiInterface apiInterface;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<MultipleResource.Movie> mMovieItems;
    private RecyclerView.LayoutManager mLayoutManager;
    private Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(myToolbar);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerViewMainActivity);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);

        searchOption(0);
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
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    /**
     * This method set the proper endpoint of search based on taken options
     *
     * @param searchOption - [int] 0 for MOST POPULAR movies, 1 - for TOP RATED movies
     */
    public void searchOption(int searchOption) {

        switch (searchOption) {
            case 0:
                call = apiInterface.mostPopularMovies();
                return;
            case 1:
                call = apiInterface.topRatedMovies();
                return;
            default:
                call = apiInterface.mostPopularMovies();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
