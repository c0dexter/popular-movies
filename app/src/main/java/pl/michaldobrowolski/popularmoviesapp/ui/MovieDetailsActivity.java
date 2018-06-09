package pl.michaldobrowolski.popularmoviesapp.ui;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Scroller;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Review;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.ReviewList;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Trailer;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.TrailerListRes;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiClient;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiInterface;
import pl.michaldobrowolski.popularmoviesapp.data.TaskContract;
import pl.michaldobrowolski.popularmoviesapp.data.TaskContract.FavouritesListEntry;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.ReviewAdapter;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.ReviewAdapter.ReviewAdapterOnClickHandler;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.TrailerAdapter;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.TrailerAdapter.TrailerAdapterOnClickHandler;
import pl.michaldobrowolski.popularmoviesapp.utils.UtilityHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static pl.michaldobrowolski.popularmoviesapp.data.TaskContract.BASE_CONTENT_URI;

public class MovieDetailsActivity extends AppCompatActivity implements ReviewAdapterOnClickHandler, TrailerAdapterOnClickHandler {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String RECYCLER_LAYOUT_GRID_TYPE = "grid_layout";
    private static final String RECYCLER_LAYOUT_LINEAR_TYPE = "linear_layout";

    // Map UI elements by using ButterKnife library
    @BindView(R.id.image_movie_poster)
    ImageView moviePosterIv;
    @BindView(R.id.text_movie_title)
    TextView movieTitleTv;
    @BindView(R.id.text_movie_description)
    TextView movieDescTv;
    @BindView(R.id.text_release_date)
    TextView movieReleaseDateTv;
    @BindView(R.id.text_average_votes_result)
    TextView movieAverageRateTv;
    @BindView(R.id.favouriteIcon)
    ImageButton favouriteBtn;
    @BindView(R.id.recycler_view_trailers)
    RecyclerView trailersRv;
    @BindView(R.id.recycler_view_reviews)
    RecyclerView reviewsRv;

    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private List<TrailerListRes> mTrailerList;
    private List<ReviewList> mReviewList;
    private int mMovieId;
    private boolean mIsFavourite;
    private ApiInterface mApiInterface;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private Call mCall;
    private double mAverageVotes;
    private String movieTitle;
    private UtilityHelper mUtilityHelper = new UtilityHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ActionBar actionBar = getSupportActionBar();
        ButterKnife.bind(this);
        // Set action bar back button to look like un up button
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        // Get Movie object form intent. Need this for getting object properties
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MOVIE");

        // Save info about movie (title, ID, average votes) into global variables
        mMovieId = movie.getId();
        movieTitle = movie.getTitle();
        mAverageVotes = movie.getVoteAverage();

        getTrailerObjects();
        getReviewObjects();
        mGridLayoutManager = new GridLayoutManager(this, 2);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutGridTypeState = savedInstanceState.getParcelable(RECYCLER_LAYOUT_GRID_TYPE);
            Parcelable savedRecyclerLayoutLinearTypeState = savedInstanceState.getParcelable(RECYCLER_LAYOUT_LINEAR_TYPE);
            if (savedRecyclerLayoutLinearTypeState != null) {
                mLinearLayoutManager.onRestoreInstanceState(savedRecyclerLayoutLinearTypeState);
            }
            if (savedRecyclerLayoutGridTypeState != null) {
                mGridLayoutManager.onRestoreInstanceState(savedRecyclerLayoutGridTypeState);
            }
        }

        // Set different layout managers for TRAILERS RV and REVIEWS RV
        trailersRv.setLayoutManager(mLinearLayoutManager);
        trailersRv.setItemAnimator(new DefaultItemAnimator());
        reviewsRv.setLayoutManager(mGridLayoutManager);
        reviewsRv.setItemAnimator(new DefaultItemAnimator());

        // Populate UI elements by using data form a Movie Object
        supportPostponeEnterTransition();
        populateDetailsUi(movie);

        new ContentProviderAsyncTask().execute();
        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("StaticFieldLeak")
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... voids) {
                        if (mIsFavourite) {
                            int position = deleteFavouriteState();
                            if (position == 1) {
                                mIsFavourite = false;
                            }
                        } else {
                            insertingIntoDataBase(movie);
                            mIsFavourite = true;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        super.onPostExecute(aVoid);
                        changeFavIconColor();
                        showFavActionStatusMessage(mIsFavourite);
                    }
                }.execute();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECYCLER_LAYOUT_GRID_TYPE,
                mGridLayoutManager.onSaveInstanceState());
        outState.putParcelable(RECYCLER_LAYOUT_LINEAR_TYPE,
                mLinearLayoutManager.onSaveInstanceState());
    }

    private void populateDetailsUi(Movie movie) {
        Picasso.get().load(mUtilityHelper.getMoviePosterUrl(movie)).into(moviePosterIv);
        movieTitleTv.setText(movie.getTitle());
        movieDescTv.setText(movie.getOverview());
        movieReleaseDateTv.setText(movie.getReleaseDate());
        movieAverageRateTv.setText(String.valueOf(movie.getVoteAverage()));
    }

    // Back button implementation
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    //-------------------------- START HANDLING TRAILERS AND REVIEWS --------------------------//
    private void getTrailerObjects() {

        mCall = mApiInterface.getMovieTrailers(mMovieId);
        mCall.enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(@NonNull Call<Trailer> call, @NonNull Response<Trailer> response) {
                fetchingDataForTrailer(response);
            }

            @Override
            public void onFailure(@NonNull Call<Trailer> call, Throwable t) {
                Log.e(TAG, "Cannot get trailer objects!", t);
                Toast.makeText(MovieDetailsActivity.this, "An error has occurred. Cannot get trailers :(", Toast.LENGTH_SHORT);
            }
        });
    }

    private void getReviewObjects() {

        mCall = mApiInterface.getMovieReviews(mMovieId);
        mCall.enqueue(new Callback<Review>() {
            @Override
            public void onResponse(@NonNull Call<Review> call, @NonNull Response<Review> response) {
                fetchingDataForReview(response);
            }

            @Override
            public void onFailure(@NonNull Call<Review> call, Throwable t) {
                Log.e(TAG, "Cannot get ReviewList objects!", t);
                Toast.makeText(MovieDetailsActivity.this, "An error has occurred. Cannot get reviews :(", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchingDataForReview(Response<Review> response) {
        if (response.isSuccessful()) {
            mReviewList = Objects.requireNonNull(response.body()).getResults();
            mReviewAdapter = new ReviewAdapter(mReviewList, MovieDetailsActivity.this);
            reviewsRv.setAdapter(mReviewAdapter);

        } else {
            try {
                Toast.makeText(MovieDetailsActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT)
                        .show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void fetchingDataForTrailer(Response<Trailer> response) {
        if (response.isSuccessful()) {
            mTrailerList = Objects.requireNonNull(response.body()).results;
            mTrailerAdapter = new TrailerAdapter(mTrailerList, MovieDetailsActivity.this);
            trailersRv.setAdapter(mTrailerAdapter);

        } else {
            try {
                Toast.makeText(MovieDetailsActivity.this, response.errorBody().string(), Toast.LENGTH_SHORT)
                        .show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClickTrailer(int clickedItemIndex) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(UtilityHelper.sUrlContainer.getBaseYoutubeMovieQuery() + mTrailerList.get(clickedItemIndex).getKey()));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(MovieDetailsActivity.this, "Issue with the trailer link :(", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClickReview(int reviewItemPosition) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_review, null);
        String fullReview = mReviewList.get(reviewItemPosition).getContent();
        String author = mReviewList.get(reviewItemPosition).getAuthor();
        displayModalFullReview(author, fullReview);
    }

    public void displayModalFullReview(String author, String reviewMsg) {
        View mView = getLayoutInflater().inflate(R.layout.dialog_review, null);
        TextView textView = (TextView) mView.findViewById(R.id.text_full_review);
        textView.setScroller(new Scroller(this));
        textView.setVerticalScrollBarEnabled(true);
        textView.setMovementMethod(new ScrollingMovementMethod());
        AlertDialog dialog = new AlertDialog.Builder(MovieDetailsActivity.this)
                .setTitle(author + "'s review")
                .setMessage(reviewMsg)
                .setNegativeButton(R.string.btn_close_dialog_review, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.sym_action_chat)
                .show();
        dialog.show();
    }
    //-------------------------- STOP HANDLING TRAILERS AND REVIEWS  --------------------------//

    //-------------------------- START HANDLING FAVOURITE BUTTON -------------------------- //

    private Uri getUri() {
        return BASE_CONTENT_URI.buildUpon()
                .appendPath(TaskContract.PATH_FAVOURITES)
                .appendPath(mMovieId + "")
                .build();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    private int deleteFavouriteState() {
        ContentResolver resolver = getContentResolver();
        Uri uri = getUri();
        int deleted = resolver.delete(uri, null, null);
        return deleted;
    }

    private Uri insertingIntoDataBase(Movie movie) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(FavouritesListEntry.COLUMN_MOVIE_TITLE, movie.getTitle());
        contentValues.put(FavouritesListEntry.COLUMN_MOVIE_ID, movie.getId());
        contentValues.put(FavouritesListEntry.COLUMN_MOVIE_POSTER_PATH, movie.getPosterPath());
        contentValues.put(FavouritesListEntry.COLUMN_MOVIE_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavouritesListEntry.COLUMN_MOVIE_VOTE_AVERAGE, movie.getVoteAverage());
        contentValues.put(FavouritesListEntry.COLUMN_MOVIE_VOTE_COUNT, movie.getVoteCount());

        Uri uri = getContentResolver().insert(FavouritesListEntry.CONTENT_URI, contentValues);
        if (uri != null) {
            return uri;
        }
        return null;
    }

    public void showFavActionStatusMessage(boolean isFavourite) {
        if (isFavourite == true) {
            Toast.makeText(this, "Movie has been added to favourite list", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Movie has been removed from favourite list", Toast.LENGTH_SHORT).show();
        }
    }

    private void changeFavIconColor() {
        if (mIsFavourite) {
            favouriteBtn.setImageResource(android.R.drawable.star_big_on);
        } else {
            favouriteBtn.setImageResource(android.R.drawable.star_big_off);
        }
    }

    public class ContentProviderAsyncTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            ContentResolver resolver = getContentResolver();
            Uri CONTENT_URI = getUri();
            Cursor cursor = resolver.query(CONTENT_URI,
                    null, null, null, null);
            return cursor;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            if (cursor.getCount() != 0) {
                mIsFavourite = true;
            } else {
                mIsFavourite = false;
            }
            changeFavIconColor();
        }
    }

}



