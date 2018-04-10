package pl.michaldobrowolski.popularmoviesapp.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pl.michaldobrowolski.popularmoviesapp.api.Model.pojo.MultipleResource;
import pl.michaldobrowolski.popularmoviesapp.R;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private final String TAG = this.getClass().getClass().getSimpleName();

    private List<MultipleResource.Movie> movieItems;

    public Adapter(List<MultipleResource.Movie> movieItems, Context applicationContext) {
        this.movieItems = movieItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MultipleResource.Movie movieItem = movieItems.get(position);
        Picasso.with(holder.imageViewMovieThumbnail.getContext()).load(movieItem.posterPath).into(holder.imageViewMovieThumbnail);
    }

    @Override
    public int getItemCount() {
        Log.d(TAG, "Item count: " + String.valueOf(movieItems.size()));
        return movieItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageViewMovieThumbnail;

        ViewHolder(View itemView) {
            super(itemView);
            imageViewMovieThumbnail = (ImageView) itemView.findViewById(R.id.image_thumbnail);
        }
    }
}
