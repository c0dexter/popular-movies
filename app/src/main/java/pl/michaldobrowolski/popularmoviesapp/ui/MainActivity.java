package pl.michaldobrowolski.popularmoviesapp.ui;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
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

    public static final String[] TABLE_FAVOURITE_MOVIE = {
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_TITLE,
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_POSTER_PATH,
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_RELEASE_DATE,
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_VOTE_AVERAGE,
            TaskContract.FavouritesListEntry.COLUMN_MOVIE_VOTE_COUNT
    };

    // DB columns indexes
    public static final int COLUMN_MOVIE_TITLE = 0;
    public static final int COLUMN_MOVIE_POSTER_PATH = 1;
    public static final int COLUMN_MOVIE_RELEASE_DATE = 2;
    public static final int COLUMN_MOVIE_VOTE_AVERAGE = 3;
    public static final int COLUMN_MOVIE_VOTE_COUNT = 4;
    private static final int ID_FAV_MOVIE_LOADER = 300;
    private static final String STATE_VIEW_MODE_KEY = "stateViewModeKey";
    private static final String FAVOURITE_MOVIES = "favourite movies";
    private static final String MOST_POPULAR_MOVIES = "popular movies";
    private static final String TOP_RATED_MOVIES = "top movies";
    private static final String RECYCLER_LAYOUT_GRID_TYPE = "grid_layout";
    private static final String RECYCLER_LAYOUT_LINEAR_TYPE = "linear_layout";
    private final String TAG = this.getClass().getSimpleName();

    // Items mapping
    ApiInterface apiInterface;
    @BindView(R.id.app_bar)
    Toolbar myToolbar;
    @BindView(R.id.recyclerViewMainActivity)
    RecyclerView mRecyclerView;
    @BindView(R.id.loading_indicator)
    ProgressBar loadingPanel;
    private RecyclerView.Adapter mAdapter;
    private List<Movie> mMovieItems;
    private RecyclerView.LayoutManager mGridLayoutManager;
    private Call call;
    private FavouriteAdapter favouriteAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private String stateViewModeValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setSupportActionBar(myToolbar);
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // Choose and remember a proper view for displaying data scope
        if (savedInstanceState != null) {
            stateViewModeValue = savedInstanceState.getString(STATE_VIEW_MODE_KEY);
            Parcelable savedRecyclerLayoutGridTypeState = savedInstanceState.getParcelable(RECYCLER_LAYOUT_GRID_TYPE);
            Parcelable savedRecyclerLayoutLinearTypeState = savedInstanceState.getParcelable(RECYCLER_LAYOUT_LINEAR_TYPE);
            if (savedRecyclerLayoutLinearTypeState != null) {
                mLinearLayoutManager.onRestoreInstanceState(savedRecyclerLayoutLinearTypeState);
            }
            if (savedRecyclerLayoutGridTypeState != null) {
                mGridLayoutManager.onRestoreInstanceState(savedRecyclerLayoutGridTypeState);
            }

            switch (stateViewModeValue) {
                case TOP_RATED_MOVIES:
                    topRatedMovies();
                    break;
                case FAVOURITE_MOVIES:
                    favouriteMovies();
                    break;
                default:
                    mostPopularMovies();
            }
        } else {
            topRatedMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_VIEW_MODE_KEY, stateViewModeValue);
        outState.putParcelable(RECYCLER_LAYOUT_GRID_TYPE,
                mGridLayoutManager.onSaveInstanceState());
        outState.putParcelable(RECYCLER_LAYOUT_LINEAR_TYPE,
                mLinearLayoutManager.onSaveInstanceState());
    }

    public void mostPopularMovies() {
        mRecyclerView.setLayoutManager(mGridLayoutManager);
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

    public void topRatedMovies() {
        mRecyclerView.setLayoutManager(mGridLayoutManager);
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

    public void favouriteMovies() {
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        favouriteAdapter = new FavouriteAdapter(this);
        mRecyclerView.setAdapter(favouriteAdapter);
        showLoadingPanel();
        getSupportLoaderManager().initLoader(ID_FAV_MOVIE_LOADER, null, this);

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
                stateViewModeValue = MOST_POPULAR_MOVIES;
                mostPopularMovies();
                Toast.makeText(this, R.string.sorting_most_popular, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.top_rated:
                // Call sorting method by top rated movies
                stateViewModeValue = TOP_RATED_MOVIES;
                topRatedMovies();
                Toast.makeText(this, R.string.sorting_top_rated, Toast.LENGTH_SHORT).show();
                return true;
            case R.id.favourites:
                // Showing movies from fav list (from DB)
                stateViewModeValue = FAVOURITE_MOVIES;
                favouriteMovies();
                Toast.makeText(this, R.string.showing_fav_movie_list_msg, Toast.LENGTH_SHORT).show();
                return true;
            default:
                // unknown error
                Toast.makeText(this, R.string.presenting_movies_default_error, Toast.LENGTH_SHORT).show();
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
                Uri queryUri = TaskContract.FavouritesListEntry.CONTENT_URI;

                return new CursorLoader(this,
                        queryUri,
                        TABLE_FAVOURITE_MOVIE,
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
        if (data.getCount() != 0)
            showFavMovies();
        else {
            Toast toast = Toast.makeText(this, R.string.no_fav_movies_msg, Toast.LENGTH_SHORT);
            toast.show();
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