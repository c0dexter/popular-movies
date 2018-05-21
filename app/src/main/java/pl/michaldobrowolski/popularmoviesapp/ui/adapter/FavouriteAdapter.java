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

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.data.TaskContract;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();

    private final Context mContext;
    private Cursor mCursor;

    public FavouriteAdapter(Context context) {
        this.mContext = context;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public FavouriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.fav_movie_item, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteAdapter.ViewHolder holder, int position) {
        TextView tvTitleFavMovie = holder.mTvFavMovieTitle;
        ImageView ivPosterFavMove = holder.mIvFavMoviePoster;
        TextView tvReleaseDateFavMovie = holder.mTvReleaseDateFavMovie;
        RatingBar rbFavMovie = holder.mRbFavMovie;
        TextView tvAverageVotesFavMovie = holder.mTvAverageVotesFavMovie;
        TextView tvVoteAmoutFavMovie = holder.mTvFavMovieVotesAmount;
        //Button btnRemoveFavMovie = holder.mBtnRemoveFav; // TODO: check if this should be here or not

        mCursor.moveToPosition(position);

        // Title
        String titleFavMovie;
        titleFavMovie = mCursor.getString(mCursor.getColumnIndex(TaskContract.FavouritesListEntry.COLUMN_MOVIE_TITLE));
        tvTitleFavMovie.setText(titleFavMovie);

        // Movie poster
        String posterFavMovie; //TODO: Probably I should use int value, because movie poster image for the fav movie should be stored in the phone memory
        posterFavMovie = mCursor.getString(mCursor.getColumnIndex(TaskContract.FavouritesListEntry.COLUMN_MOVIE_POSTER_PATH));
        Picasso.get().load(posterFavMovie).into(ivPosterFavMove);

        // Release date
        String releaseDate;
        releaseDate = mCursor.getString(mCursor.getColumnIndex(TaskContract.FavouritesListEntry.COLUMN_MOVIE_RELEASE_DATE));
        tvReleaseDateFavMovie.setText(releaseDate);

        // Vote average
        String voteAverage;
        voteAverage = mCursor.getString(mCursor.getColumnIndex(TaskContract.FavouritesListEntry.COLUMN_MOVIE_VOTE_AVERAGE));
        tvAverageVotesFavMovie.setText(voteAverage);

        // Vote Count
        String voteCount;
        voteCount = mCursor.getString(mCursor.getColumnIndex(TaskContract.FavouritesListEntry.COLUMN_MOVIE_VOTE_COUNT));
        tvVoteAmoutFavMovie.setText(voteCount);

        // Rating bar
        float averageVote;
        averageVote = Float.parseFloat(voteAverage);
        rbFavMovie.setRating(averageVote);


    }

    @Override
    public int getItemCount() {
        if (null == mCursor) {
            return 0;
        }
        return mCursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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
        @BindView(R.id.text_movie_title)
        TextView mTvFavMovieTitle;
        @BindView(R.id.text_release_date)
        TextView mTvReleaseDateFavMovie;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
