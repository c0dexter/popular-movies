package pl.michaldobrowolski.popularmoviesapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import pl.michaldobrowolski.popularmoviesapp.TrailerListRes;

public class Trailer implements Parcelable {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("results")
    @Expose
    public List<TrailerListRes> results = null;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeTypedList(this.results);
    }

    public Trailer() {
    }

    protected Trailer(Parcel in) {
        this.id = in.readInt();
        this.results = in.createTypedArrayList(TrailerListRes.CREATOR);
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel source) {
            return new Trailer(source);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
