package dev.paj.towatchtonight.data.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/*
    Movie details object from GET /movie/{movie_id} call
 */
public class MovieDetails implements Parcelable{
    @SerializedName("overview")
    private String plotOverview;
    @SerializedName("runtime")
    private String runtime;
    @SerializedName("adult")
    private String adult;
    @SerializedName("vote_average")
    private String voteAverage;
    @SerializedName("genres")
    private Genre[] genres;


    public MovieDetails(String plotOverview, String runtime, String voteAverage, Genre[] genres) {
        this.plotOverview = plotOverview;
        this.runtime = runtime;
        this.adult = "false";
        this.voteAverage = voteAverage;
        this.genres = genres;
    }

    protected MovieDetails(Parcel in) {
        plotOverview = in.readString();
        runtime = in.readString();
        adult = in.readString();
        voteAverage = in.readString();
        genres = in.createTypedArray(Genre.CREATOR);
    }

    public static final Creator<MovieDetails> CREATOR = new Creator<MovieDetails>() {
        @Override
        public MovieDetails createFromParcel(Parcel in) {
            return new MovieDetails(in);
        }

        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[size];
        }
    };

    public Genre[] getGenres() {
        return genres;
    }

    public String getPlotOverview() {
        return plotOverview;
    }

    public String getRuntime() {
        return runtime;
    }

    public String getAdult() {
        return adult;
    }

    public String getVoteAverage() {
        return voteAverage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(plotOverview);
        parcel.writeString(runtime);
        parcel.writeString(adult);
        parcel.writeString(voteAverage);
        parcel.writeTypedArray(genres, i);
    }
}
