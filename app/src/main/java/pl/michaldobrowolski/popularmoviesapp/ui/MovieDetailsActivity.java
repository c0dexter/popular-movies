package pl.michaldobrowolski.popularmoviesapp.ui;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.MovieListRes;
import pl.michaldobrowolski.popularmoviesapp.api.service.ApiInterface;
import pl.michaldobrowolski.popularmoviesapp.data.TaskDbHelper;
import pl.michaldobrowolski.popularmoviesapp.ui.adapter.MovieAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailsActivity extends AppCompatActivity {

    ApiInterface apiInterface;
    private Call call;

    // Map UI elements by using ButterKnife library
    @BindView(R.id.image_movie_poster)
    ImageView moviePosterIv;
    @BindView(R.id.text_movie_title)
    TextView movieTitleTv;
    @BindView(R.id.text_movie_description)
    TextView movieDescTv;
    @BindView(R.id.text_release_date)
    TextView movieReleaseDate;
    @BindView(R.id.text_average_votes_result)
    TextView movieAverageRate;
    @BindView(R.id.favouriteIcon)
    ImageButton favouriteButton;

    private int movieId;
    private String movieTitle;
    private boolean isFavourite;
    private SQLiteDatabase mDb;

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

        // Save info about movie title and ID into global variables
        movieId = movie.getId();
        movieTitle = movie.getTitle();

        // Populate UI elements by using data form a Movie Object
        Picasso.get().load(movie.getMoviePosterUrl()).into(moviePosterIv);
        movieTitleTv.setText(movie.getTitle());
        movieDescTv.setText(movie.getOverview());
        movieReleaseDate.setText(movie.getReleaseDate());
        movieAverageRate.setText(String.valueOf(movie.getVoteAverage()));
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
        favouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: use method for checking if the specific movie already exist in the DB
                // TODO: if not, take all important data and INSERT them into DB
                // TODO: change flag isFavourite to TRUE
                // TODO: change appearance of button, fill the shape by using some colour
            }
        });
    }

//    public void movieTrailers() {
//        call = apiInterface.mostPopularMovies();
//        call.enqueue(new Callback<MovieListRes>() {
//            @Override
//            public void onResponse(@NonNull Call<MovieListRes> call, @NonNull Response<MovieListRes> response) {
//                Log.d("LOG: Response Code: ", response.code() + "");
//                fetchingData(response);
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<MovieListRes> call, @NonNull Throwable t) {
//                call.cancel();
//                //Log.e(TAG, "onFailure: ", t);
//            }
//        });
//    }
//
//    private void fetchingData(@NonNull Response<MovieListRes> response) {
//        if (response.isSuccessful()) {
//
//            mMovieItems = Objects.requireNonNull(response.body()).resultMovieItems;
//            mAdapter = new MovieAdapter(mMovieItems, MainActivity.this); // MainActivity.this::onClick
//            mRecyclerView.setAdapter(mAdapter);
//
//        } else {
//            Toast.makeText(MainActivity.this, "Error. Fetching data failed :(", Toast.LENGTH_SHORT).show();
//            Log.e(TAG, "Response code: " + response.code());
//        }
//    }


}
