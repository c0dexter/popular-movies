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

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.ui.MainActivity;

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
        TextView tvAverageRatingFavMovie = holder.mTvRatingFavMovie;
        TextView tvVoteAmoutFavMovie = holder.mTvFavMovieVotesAmount;
        Button btnRemoveFavMovie = holder.mBtnRemoveFav;

        mCursor.moveToPosition(position);
//        String title = mCursor.getString(MainActivity.MOVIE_TITLE);
//        titleTextView.setText(title);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor){
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
        TextView mTvRatingFavMovie;
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
