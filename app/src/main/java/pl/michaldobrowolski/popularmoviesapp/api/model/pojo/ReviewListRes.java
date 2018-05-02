package pl.michaldobrowolski.popularmoviesapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewListRes implements Parcelable {
    public static final Creator<ReviewListRes> CREATOR = new Creator<ReviewListRes>() {
        @Override
        public ReviewListRes createFromParcel(Parcel source) {
            return new ReviewListRes(source);
        }

        @Override
        public ReviewListRes[] newArray(int size) {
            return new ReviewListRes[size];
        }
    };

    @SerializedName("id")
    public int id;
    @SerializedName("page")
    public int page;
    @SerializedName("results")
    public List<Review> results = null;
    @SerializedName("total_pages")
    public int totalPages;
    @SerializedName("total_results")

    public int totalResults;

    public ReviewListRes() {
    }

    protected ReviewListRes(Parcel in) {
        this.id = in.readInt();
        this.page = in.readInt();
        this.results = in.createTypedArrayList(Review.CREATOR);
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