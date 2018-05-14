package pl.michaldobrowolski.popularmoviesapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.TrailerListRes;
import pl.michaldobrowolski.popularmoviesapp.utils.UtilityHelper;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private final String TAG = this.getClass().getSimpleName();
    private final TrailerAdapterOnClickHandler listClickHandler;
    private List<TrailerListRes> trailerList;
    private UtilityHelper mUtilityHelper = new UtilityHelper();

    public TrailerAdapter(List<TrailerListRes> dataTrailers, TrailerAdapterOnClickHandler listClickHandler) {
        this.trailerList = dataTrailers;
        this.listClickHandler = listClickHandler;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Set the view which has to be inflated
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_item, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ImageView imageView = holder.ivTrailerThumbnail;
        String movieImageUrl = mUtilityHelper.getTrailerThumbnailUrl(trailerList.get(position));
        Picasso.get().load(movieImageUrl).into(imageView);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "Item count: " + String.valueOf(trailerList.size()));
        return trailerList.size();
    }

    public interface TrailerAdapterOnClickHandler {
        void onClickTrailer(int trailerItemPosition);

    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivTrailerThumbnail;
        ImageButton ibTrailerPlayButton;

        ViewHolder(View itemView) {
            super(itemView);
            ivTrailerThumbnail = itemView.findViewById(R.id.image_view_trailer);
            ibTrailerPlayButton = itemView.findViewById(R.id.image_button_play);
            ibTrailerPlayButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int trailerPosition = getAdapterPosition();
            listClickHandler.onClickTrailer(trailerPosition);
        }
    }
}
