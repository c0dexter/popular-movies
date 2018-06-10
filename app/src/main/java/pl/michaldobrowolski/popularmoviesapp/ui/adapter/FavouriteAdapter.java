package pl.michaldobrowolski.popularmoviesapp.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.ui.MainActivity;
import pl.michaldobrowolski.popularmoviesapp.utils.UtilityHelper;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    //private final FavouriteOnClickHandler favHandler;
    private final Context mContext;
    private Cursor mCursor;
    private final UtilityHelper utilityHelper = new UtilityHelper();

    public FavouriteAdapter(Context context) {
        this.mContext = context;
        //this.favHandler = favHandler;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fav_movie_item, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TextView tvTitleFavMovie = holder.mTvFavMovieTitle;
        ImageView ivPosterFavMove = holder.mIvFavMoviePoster;
        TextView tvReleaseDateFavMovie = holder.mTvReleaseDateFavMovie;
        RatingBar rbFavMovie = holder.mRbFavMovie;
        TextView tvAverageVotesFavMovie = holder.mTvAverageVotesFavMovie;
        TextView tvVoteAmountFavMovie = holder.mTvFavMovieVotesAmount;
        Button btnRemoveFavMovie = holder.mBtnRemoveFav; // TODO: check if this should be here or not

        mCursor.moveToPosition(position);
        // Title
        String titleFavMovie;
        titleFavMovie = mCursor.getString(MainActivity.COLUMN_MOVIE_TITLE);
        tvTitleFavMovie.setText(titleFavMovie);

        // Movie poster
        String posterFavMovie;
        posterFavMovie = mCursor.getString(MainActivity.COLUMN_MOVIE_POSTER_PATH);
        String urlId = utilityHelper.getMoviePosterUrl(posterFavMovie);
        Picasso.get().load(urlId).into(ivPosterFavMove);

        // Release date
        String releaseDate;
        releaseDate = mCursor.getString(MainActivity.COLUMN_MOVIE_RELEASE_DATE);
        tvReleaseDateFavMovie.setText(releaseDate);

        // Vote average
        String voteAverage;
        voteAverage = mCursor.getString(MainActivity.COLUMN_MOVIE_VOTE_AVERAGE);
        tvAverageVotesFavMovie.setText(voteAverage);

        // Vote Count
        String voteCount;
        voteCount = mCursor.getString(MainActivity.COLUMN_MOVIE_VOTE_COUNT);
        tvVoteAmountFavMovie.setText(voteCount);

        // Movie ID
        final String movieId = mCursor.getString(MainActivity.COLUMN_MOVIE_ID);

        // Rating bar
        float averageVote = Float.parseFloat(voteAverage);
        rbFavMovie.setRating(averageVote);

    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    interface FavouriteOnClickHandler {
        void onClickFav(String clickedItemIndex);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements FavouriteOnClickHandler{
        @BindView(R.id.button_remove_favourite)
        Button mBtnRemoveFav;
        @BindView(R.id.image_fav_movie_poster)
        ImageView mIvFavMoviePoster;
        @BindView(R.id.rating_bar_fav_movie)
        RatingBar mRbFavMovie;
        @BindView(R.id.text_fav_votes_amout)
        TextView mTvFavMovieVotesAmount;
        @BindView(R.id.text_rating_fav_movie)
        TextView mTvAverageVotesFavMovie;
        @BindView(R.id.text_fav_movie_title)
        TextView mTvFavMovieTitle;
        @BindView(R.id.text_fav_relase_date_movie)
        TextView mTvReleaseDateFavMovie;


        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClickFav(String clickedItemIndex) {
            mBtnRemoveFav.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "USUWAM KURWA", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
