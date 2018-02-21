package dev.paj.towatchtonight.data.model.movie;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class MovieReview implements Parcelable {
    @SerializedName("author")
    private String reviewAuthor;

    @SerializedName("content")
    private String reviewContent;

    public String getReviewAuthor() {
        return reviewAuthor;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected MovieReview(Parcel in) {
        reviewAuthor = in.readString();
        reviewContent = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(reviewAuthor);
        parcel.writeString(reviewContent);
    }
}
