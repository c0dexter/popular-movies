package pl.michaldobrowolski.popularmoviesapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class ReviewList implements Parcelable {
    public static final Creator<ReviewList> CREATOR = new Creator<ReviewList>() {
        @Override
        public ReviewList createFromParcel(Parcel source) {
            return new ReviewList(source);
        }

        @Override
        public ReviewList[] newArray(int size) {
            return new ReviewList[size];
        }
    };
    @SerializedName("url")
    private String url;
    @SerializedName("author")
    private String author;
    @SerializedName("content")
    private String content;
    @SerializedName("id")
    private String id;

    public ReviewList() {
    }

    private ReviewList(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.id = in.readString();
        this.url = in.readString();
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.author);
        dest.writeString(this.content);
        dest.writeString(this.id);
        dest.writeString(this.url);
    }
}
