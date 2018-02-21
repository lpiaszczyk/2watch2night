package dev.paj.towatchtonight.data.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
/*
    Single movie object from GET movie/popular or GET movie/top_rated call
 */
public class Movie implements Parcelable{

    @SerializedName("id")
    private String id;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("title")
    private String title;

    @SerializedName("release_date")
    private String releaseDate;

    @SerializedName("poster_path")
    private String posterPath;

    public Movie(String id, String originalTitle, String title, String releaseDate, String posterPath) {
        this.id = id;
        this.originalTitle = originalTitle;
        this.title = title;
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
    }

    protected Movie(Parcel in) {
        id = in.readString();
        originalTitle = in.readString();
        title = in.readString();
        releaseDate = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getId() {
        return id;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getTitle() {
        return title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(id);
        parcel.writeString(originalTitle);
        parcel.writeString(title);
        parcel.writeString(releaseDate);
        parcel.writeString(posterPath);
    }
}
