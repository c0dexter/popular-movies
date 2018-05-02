package pl.michaldobrowolski.popularmoviesapp.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Review;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Trailer;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final String TAG = MovieDetailsActivity.class.getSimpleName();
    private static final String GRID_RECYCLER_LAYOUT = "grid_layout";
    private static final String LINEAR_RECYCLER_LAYOUT = "linear_layout";
    private ApiInterface apiInterface;
    private int movieId;
    private String movieTitle;
    private boolean isFavourite;
    private SQLiteDatabase mDb;
    private double averageVotes;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager linearLayoutManager;

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

        // Get Movie object form intent. Need this for getting object properties
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MOVIE");

        //
        gridLayoutManager = new GridLayoutManager(this, 2);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable(GRID_RECYCLER_LAYOUT);
            Parcelable linearRecyclerLayoutState = savedInstanceState.getParcelable(LINEAR_RECYCLER_LAYOUT);
            if (linearRecyclerLayoutState != null) {
                linearLayoutManager.onRestoreInstanceState(linearRecyclerLayoutState);
            }
            if (savedRecyclerLayoutState != null) {
                gridLayoutManager.onRestoreInstanceState(savedRecyclerLayoutState);
            }
        }

        // Save info about movie (title, ID, average votes) into global variables
        movieId = movie.getId();
        movieTitle = movie.getTitle();
        averageVotes = movie.getVoteAverage();

        // Populate UI elements by using data form a Movie Object
        Picasso.get().load(movie.getMoviePosterUrl()).into(moviePosterIv);
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
        apiInterface.getMovieTrailers(movieId).enqueue(new Callback<Trailer>() {
            @Override
            public void onResponse(@NonNull Call<Trailer> call, @NonNull Response<Trailer> response) {

                // TODO: do something with this response (adapter, recycler view)

            }

            @Override
            public void onFailure(@NonNull Call<Trailer> call, Throwable t) {
                Log.e(TAG, "Cannot get trailer objects!", t);
                Toast.makeText(MovieDetailsActivity.this, "An error has occurred. Cannot get trailers :(", Toast.LENGTH_SHORT);
            }
        });

    }

    private void getReviewObjects() {
        apiInterface.getMovieReviews(movieId).enqueue(new Callback<Review>() {
            @Override
            public void onResponse(@NonNull Call<Review> call, @NonNull Response<Review> response) {

                // TODO: do something with this response (adapter, recycler view)
            }

            @Override
            public void onFailure(@NonNull Call<Review> call, Throwable t) {
                Log.e(TAG, "Cannot get Review objects!", t);
                Toast.makeText(MovieDetailsActivity.this, "An error has occurred. Cannot get reviews :(", Toast.LENGTH_SHORT);
            }
        });

    }
}
