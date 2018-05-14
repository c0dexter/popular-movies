package pl.michaldobrowolski.popularmoviesapp.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pl.michaldobrowolski.popularmoviesapp.R;
import pl.michaldobrowolski.popularmoviesapp.api.model.pojo.ReviewList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder> {
    private final String TAG = this.getClass().getSimpleName();
    private List<ReviewList> reviewsList;
    private final ReviewAdapter.ReviewAdapterOnClickHandler listClickHandler;

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

    public interface ReviewAdapterOnClickHandler {
        void onClickReview(int reviewItemPosition);
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvReview;
        private TextView tvReviewAuthor;
        private TextView tvFullReview;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvReview = itemView.findViewById(R.id.text_view_review_content);
            this.tvReviewAuthor = itemView.findViewById(R.id.text_view_review_author);
            this.tvFullReview = itemView.findViewById(R.id.text_full_review);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int reviewPosition = getAdapterPosition();
            listClickHandler.onClickReview(reviewPosition);
        }
    }
}
