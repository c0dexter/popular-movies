package pl.michaldobrowolski.popularmoviesapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieListRes implements Parcelable {

    public static final Creator<MovieListRes> CREATOR = new Creator<MovieListRes>() {
        @Override
        public MovieListRes createFromParcel(Parcel source) {
            return new MovieListRes(source);
        }

        @Override
        public MovieListRes[] newArray(int size) {
            return new MovieListRes[size];
        }
    };

    @SerializedName("page")
    private Integer page;
    @SerializedName("total_results")
    private Integer totalResults;
    @SerializedName("total_pages")
    private Integer totalPages;
    @SerializedName("results")
    public List<Movie> resultMovieItems = null;

    public MovieListRes() {
    }

    private MovieListRes(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalResults = (Integer) in.readValue(Integer.class.getClassLoader());
        this.totalPages = (Integer) in.readValue(Integer.class.getClassLoader());
        this.resultMovieItems = in.createTypedArrayList(Movie.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeValue(this.totalResults);
        dest.writeValue(this.totalPages);
        dest.writeTypedList(this.resultMovieItems);
    }
}