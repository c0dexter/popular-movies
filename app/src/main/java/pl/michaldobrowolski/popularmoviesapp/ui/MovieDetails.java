package pl.michaldobrowolski.popularmoviesapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;

public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        // Get Movie object form intent
        Intent intent = getIntent();
        Movie movie = intent.getParcelableExtra("MOVIE");

        // Map UI elements
        ImageView moviePosterIv = findViewById(R.id.image_movie_poster);
        TextView movieTitleTv = findViewById(R.id.text_movie_title);
        TextView movieDescTv = findViewById(R.id.text_movie_description);
        TextView movieReleaseDate = findViewById(R.id.text_release_date);
        TextView movieAverageRate = findViewById(R.id.text_average_votes_result);

        // Populate UI elements by using data form a movie object
        Picasso.get().load(movie.getMoviePosterUrl()).into(moviePosterIv);
        movieTitleTv.setText(movie.getTitle());
        movieDescTv.setText(movie.getOverview());
        movieReleaseDate.setText(movie.getReleaseDate());
        movieAverageRate.setText(String.valueOf(movie.getVoteAverage()));

    }
}
