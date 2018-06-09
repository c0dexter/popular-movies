package pl.michaldobrowolski.popularmoviesapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.ReviewList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private final ReviewAdapter.ReviewAdapterOnClickHandler listClickHandler;
    private List<ReviewList> reviewsList;

    public ReviewAdapter(List<ReviewList> dataReviews, ReviewAdapterOnClickHandler listClickHandler) {
        this.reviewsList = dataReviews;
        this.listClickHandler = listClickHandler;
    }


    @NonNull
    @Override
    public ReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        view.setFocusable(true);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewAdapter.ViewHolder holder, int position) {
        TextView textViewReviewContent = holder.tvReview;
        TextView textViewReviewAuthor = holder.tvReviewAuthor;
        String review = reviewsList.get(position).getContent();
        String author = reviewsList.get(position).getAuthor();

        textViewReviewContent.setText(review);
        textViewReviewAuthor.setText(author);
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "Item count: " + String.valueOf(reviewsList.size()));
        return reviewsList.size();
    }

    public interface ReviewAdapterOnClickHandler {
        void onClickReview(int reviewItemPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.text_view_review_content)
        TextView tvReview;
        @BindView(R.id.text_view_review_author)
        TextView tvReviewAuthor;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int reviewPosition = getAdapterPosition();
            listClickHandler.onClickReview(reviewPosition);
        }
    }
}
