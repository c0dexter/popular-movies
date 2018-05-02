package pl.michaldobrowolski.popularmoviesapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.MovieListRes;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiClient;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiInterface;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.MovieAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {
    private final String TAG = this.getClass().getSimpleName();

    ApiInterface apiInterface;
    @BindView(R.id.app_bar)
    Toolbar myToolbar;
    @BindView(R.id.recyclerViewMainActivity)
    RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private List<Movie> mMovieItems;
    private RecyclerView.LayoutManager mLayoutManager;
    private Call call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mLayoutManager = new GridLayoutManager(this, 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // Load by default most popular movies
        mostPopularMovies();
    }

    public void mostPopularMovies() {
        call = apiInterface.getMostPopularMovies();
        call.enqueue(new Callback<MovieListRes>() {
            @Override
            public void onResponse(@NonNull Call<MovieListRes> call, @NonNull Response<MovieListRes> response) {
                Log.d("LOG: Response Code: ", response.code() + "");
                fetchingData(response);
            }

            @Override
            public void onFailure(@NonNull Call<MovieListRes> call, @NonNull Throwable t) {
                call.cancel();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void fetchingData(@NonNull Response<MovieListRes> response) {
        if (response.isSuccessful()) {

            mMovieItems = Objects.requireNonNull(response.body()).resultMovieItems;
            mAdapter = new MovieAdapter(mMovieItems, MainActivity.this); // MainActivity.this::onClick
            mRecyclerView.setAdapter(mAdapter);

        } else {
            Toast.makeText(MainActivity.this, "Error. Fetching data failed :(", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Response code: " + response.code());
        }
    }

    public void topRatedMovies() {
        call = apiInterface.getTopRatedMovies();
        call.enqueue(new Callback<MovieListRes>() {
            @Override
            public void onResponse(@NonNull Call<MovieListRes> call, @NonNull Response<MovieListRes> response) {
                Log.d("LOG: Response Code: ", response.code() + "");
                fetchingData(response);
            }

            @Override
            public void onFailure(@NonNull Call<MovieListRes> call, @NonNull Throwable t) {
                call.cancel();
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.most_popular:
                // Call sorting method by most popular
                mostPopularMovies();
                Toast.makeText(this, "Sorting by most popular", Toast.LENGTH_SHORT).show();
                break;
            case R.id.top_rated:
                // Call sorting method by top rated movies
                topRatedMovies();
                Toast.makeText(this, "Sorting by top rated", Toast.LENGTH_SHORT).show();
                break;
            default:
                // unknown error
                Toast.makeText(this, "Some Error Bro! :(", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int clickedItemIndex) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("MOVIE", mMovieItems.get(clickedItemIndex));
        startActivity(intent);
    }
}
