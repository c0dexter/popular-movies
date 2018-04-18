package pl.michaldobrowolski.popularmoviesapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;

public class MovieDetails extends AppCompatActivity {

    // Map UI elements by using ButterKnife library
    @BindView(R.id.image_movie_poster) ImageView moviePosterIv;
    @BindView (R.id.text_movie_title) TextView movieTitleTv;
    @BindView (R.id.text_movie_description) TextView movieDescTv;
    @BindView (R.id.text_release_date) TextView movieReleaseDate;
    @BindView (R.id.text_average_votes_result) TextView movieAverageRate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        // Get Movie object form intent
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MOVIE");

        // Populate UI elements by using data form a Movie Object
        Picasso.get().load(movie.getMoviePosterUrl()).into(moviePosterIv);
        movieTitleTv.setText(movie.getTitle());
        movieDescTv.setText(movie.getOverview());
        movieReleaseDate.setText(movie.getReleaseDate());
        movieAverageRate.setText(String.valueOf(movie.getVoteAverage()));

    }
}
