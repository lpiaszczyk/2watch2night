package dev.paj.towatchtonight.data.model.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GenresResults {
    @SerializedName("genres")
    private List<Genre> results;

    public List<Genre> getResults() {
        return results;
    }
}
