package pl.michaldobrowolski.popularmoviesapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Review implements Parcelable {
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel source) {
            return new Review(source);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    @SerializedName("id")
    public int id;
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private List<ReviewList> results = null;
    @SerializedName("total_pages")
    private int totalPages;
    @SerializedName("total_results")
    private int totalResults;

    public Review() {
    }

    public int getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public List<ReviewList> getResults() {
        return results;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getTotalResults() {
        return totalResults;
    }

    private Review(Parcel in) {
        this.id = in.readInt();

        this.page = in.readInt();
        this.results = in.createTypedArrayList(ReviewList.CREATOR);
        this.totalPages = in.readInt();
        this.totalResults = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.page);
        dest.writeTypedList(this.results);
        dest.writeInt(this.totalPages);
        dest.writeInt(this.totalResults);
    }
}
