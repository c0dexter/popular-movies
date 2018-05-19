package pl.michaldobrowolski.popularmoviesapp.ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import pl.michaldobrowolski.popularmoviesapp.data.TaskDbHelper;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.ReviewAdapter;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.ReviewAdapter.ReviewAdapterOnClickHandler;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.TrailerAdapter;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.TrailerAdapter.TrailerAdapterOnClickHandler;
import pl.michaldobrowolski.popularmoviesapp.utils.UtilityHelper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity implements ReviewAdapterOnClickHandler, TrailerAdapterOnClickHandler {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String GRID_RECYCLER_LAYOUT = "grid_layout";
    private static final String LINEAR_RECYCLER_LAYOUT = "linear_layout";

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

    private ApiInterface mApiInterface;
    private int mMovieId;
    private String movieTitle;
    private boolean mIsFavourite;
    private SQLiteDatabase mDb;
    private double mAverageVotes;
    private GridLayoutManager mGridLayoutManager;
    private LinearLayoutManager mLinearLayoutManager;
    private UtilityHelper mUtilityHelper = new UtilityHelper();
    private TrailerAdapter mTrailerAdapter;
    private ReviewAdapter mReviewAdapter;
    private List<TrailerListRes> mTrailerList;
    private List<ReviewList> mReviewList;
    private Call mCall;

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

        // Handling DB
        TaskDbHelper dbHelper = new TaskDbHelper(this);
        mDb = dbHelper.getWritableDatabase();
        Cursor cursor = getAllFavMovies();


        mApiInterface = ApiClient.getClient().create(ApiInterface.class);
        // Get Movie object form intent. Need this for getting object properties
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MOVIE");

        mGridLayoutManager = new GridLayoutManager(this, 2);
        mLinearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(GRID_RECYCLER_LAYOUT);
            Parcelable linearRecyclerLayoutState = savedInstanceState.getParcelable(LINEAR_RECYCLER_LAYOUT);
            if (linearRecyclerLayoutState != null) {
                mLinearLayoutManager.onRestoreInstanceState(linearRecyclerLayoutState);
            }
            if (savedRecyclerLayoutState != null) {
                mLinearLayoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }
        trailersRv.setLayoutManager(mLinearLayoutManager); // OR USE mGridLayoutManager
        trailersRv.setItemAnimator(new DefaultItemAnimator());
        reviewsRv.setLayoutManager(mGridLayoutManager);
        reviewsRv.setItemAnimator(new DefaultItemAnimator());

        // Save info about movie (title, ID, average votes) into global variables
        mMovieId = movie.getId();
        movieTitle = movie.getTitle();
        mAverageVotes = movie.getVoteAverage();

        // Populate UI elements by using data form a Movie Object
        Picasso.get().load(mUtilityHelper.getMoviePosterUrl(movie)).into(moviePosterIv);
        movieTitleTv.setText(movie.getTitle());
        movieDescTv.setText(movie.getOverview());
        movieReleaseDateTv.setText(movie.getReleaseDate());
        movieAverageRateTv.setText(String.valueOf(movie.getVoteAverage()));

        getTrailerObjects();
        getReviewObjects();
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

    private void markAsFavourite() {
        favouriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: use method for checking if the specific movie already exist in the DB
                // TODO: if not, take all important data and INSERT them into DB
                // TODO: change flag isFavourite to TRUE
                // TODO: change appearance of button, fill the shape by using some colour
            }
        });
    }

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

    private Cursor getAllFavMovies() {
        return mDb.query(
                TaskContract.FavouritesListEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                TaskContract.FavouritesListEntry.COLUMN_MOVIE_TITLE
        );
    }
}
