package pl.michaldobrowolski.popularmoviesapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Review implements Parcelable {
    @SerializedName("author")
    @Expose
    public String author;
    @SerializedName("content")
    @Expose
    public String content;
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("url")
    @Expose
    public String url;

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

    public Review() {
    }

    protected Review(Parcel in) {
        this.author = in.readString();
        this.content = in.readString();
        this.id = in.readString();
        this.url = in.readString();
    }

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
}
