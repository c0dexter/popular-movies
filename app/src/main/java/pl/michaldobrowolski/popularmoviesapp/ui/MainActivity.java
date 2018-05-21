package pl.michaldobrowolski.popularmoviesapp.ui;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
import pl.michaldobrowolski.popularmoviesapp.data.TaskContract;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.FavouriteAdapter;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.MovieAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler, LoaderManager.LoaderCallbacks<Cursor> {
    private static final int ID_FAV_MOVIE_LOADER = 100;
    private final String TAG = this.getClass().getSimpleName();
    ApiInterface apiInterface;
    @BindView(R.id.app_bar)
    Toolbar myToolbar;
    @BindView(R.id.recyclerViewMainActivity)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingPanel;
    private RecyclerView.Adapter mAdapter;
    private List<Movie> mMovieItems;
    private RecyclerView.LayoutManager mLayoutManager;
    private Call call;
    private FavouriteAdapter favouriteAdapter;
    private LinearLayoutManager linearLayoutManager;

    public static final String[] TABLE_FAVOURITE_MOVIE = {
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_TITLE,
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_POSTER_PATH,
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_RELEASE_DATE,
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_VOTE_COUNT
    };

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
            mAdapter = new MovieAdapter(mMovieItems, MainActivity.this);
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

    public void favouriteMoveis() {
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        favouriteAdapter = new FavouriteAdapter(this);
        mRecyclerView.setAdapter(favouriteAdapter);
        showLoadingPanel();
        getSupportLoaderManager().initLoader(ID_FAV_MOVIE_LOADER, null, this);
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
                return true;
            case R.id.top_rated:
                // Call sorting method by top rated movies
                topRatedMovies();
                Toast.makeText(this, "Sorting by top rated", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.favourites:
                favouriteMoveis();
                Toast.makeText(this, "Favourite movies list", Toast.LENGTH_SHORT).show();
                return true;
            default:
                // unknown error
                Toast.makeText(this, "Some Error Bro! :(", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(int clickedItemIndex) {
        Intent intent = new Intent(this, MovieDetailsActivity.class);
        intent.putExtra("MOVIE", mMovieItems.get(clickedItemIndex));
        startActivity(intent);
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // In the activity could be used many loaders, so ID is using for identifying a correct loader
        switch (id) {
            case ID_FAV_MOVIE_LOADER:

                Uri queryUri = TaskContract.CONTENT_URI;

                return new CursorLoader(this,
                        queryUri,
                        TABLE_FAVOURITE_MOVIE, //TODO: ask about this parameter. I would like to get everything, so could be a NULL value. Am I right?
                        null,
                        null,
                        null);
            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        favouriteAdapter.swapCursor(data);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        if (data.getCount() != 0) { //TODO: why there I get an error with null value?
            showFavMovies();
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        favouriteAdapter.swapCursor(null);
    }


    private void showFavMovies() {
        loadingPanel.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);

    }

    private void showLoadingPanel() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        loadingPanel.setVisibility(View.VISIBLE);
    }
}