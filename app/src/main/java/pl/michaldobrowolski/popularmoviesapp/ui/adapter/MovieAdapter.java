package pl.michaldobrowolski.popularmoviesapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.Movie;
import pl.michaldobrowolski.popularmoviesapp.utils.UtilityHelper;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final MovieAdapterOnClickHandler listClickHandler;
    private final List<Movie> movieItems;
    private final UtilityHelper utilityHelper = new UtilityHelper();

    public MovieAdapter(List<Movie> movieItems, MovieAdapterOnClickHandler listClickHandler) {
        this.movieItems = movieItems;
        this.listClickHandler = listClickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ImageView imageView = holder.imageViewMovieThumbnail;
        String urlId = utilityHelper.getMoviePosterUrl(movieItems.get(position));
        Picasso.get().load(urlId).into(imageView);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Item count: " + String.valueOf(movieItems.size()));
        return movieItems.size();
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int clickedItemIndex);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView imageViewMovieThumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewMovieThumbnail = itemView.findViewById(R.id.image_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int moviePosition = getAdapterPosition();
            listClickHandler.onClick(moviePosition);
        }
    }
}
