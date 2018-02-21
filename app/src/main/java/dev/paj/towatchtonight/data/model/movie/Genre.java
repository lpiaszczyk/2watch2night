package dev.paj.towatchtonight.data.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Genre implements Parcelable{
    @SerializedName("id")
    private String genreId;
    @SerializedName("name")
    private String genreName;

    public String getGenreId() {
        return genreId;
    }

    public String getGenreName() {
        return genreName;
    }

    public Genre(String genreId, String genreName) {
        this.genreId = genreId;
        this.genreName = genreName;
    }

    protected Genre(Parcel in) {
        genreId = in.readString();
        genreName = in.readString();
    }

    public static final Creator<Genre> CREATOR = new Creator<Genre>() {
        @Override
        public Genre createFromParcel(Parcel in) {
            return new Genre(in);
        }

        @Override
        public Genre[] newArray(int size) {
            return new Genre[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(genreId);
        parcel.writeString(genreName);
    }
}
