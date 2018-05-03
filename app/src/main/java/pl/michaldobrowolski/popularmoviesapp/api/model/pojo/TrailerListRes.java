package pl.michaldobrowolski.popularmoviesapp.api.model.pojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class TrailerListRes implements Parcelable {
    public static final Creator<TrailerListRes> CREATOR = new Creator<TrailerListRes>() {
        @Override
        public TrailerListRes createFromParcel(Parcel source) {
            return new TrailerListRes(source);
        }

        @Override
        public TrailerListRes[] newArray(int size) {
            return new TrailerListRes[size];
        }
    };

    @SerializedName("id")
    public String id;
    @SerializedName("iso_639_1")
    public String iso6391;
    @SerializedName("iso_3166_1")
    public String iso31661;
    @SerializedName("key")
    public String key;
    @SerializedName("name")
    public String name;
    @SerializedName("site")
    public String site;
    @SerializedName("size")
    public int size;
    @SerializedName("type")
    public String type;

    public TrailerListRes() {
    }

    protected TrailerListRes(Parcel in) {
        this.id = in.readString();
        this.iso6391 = in.readString();
        this.iso31661 = in.readString();
        this.key = in.readString();
        this.name = in.readString();
        this.site = in.readString();
        this.size = in.readInt();
        this.type = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.iso6391);
        dest.writeString(this.iso31661);
        dest.writeString(this.key);
        dest.writeString(this.name);
        dest.writeString(this.site);
        dest.writeInt(this.size);
        dest.writeString(this.type);
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }
}
