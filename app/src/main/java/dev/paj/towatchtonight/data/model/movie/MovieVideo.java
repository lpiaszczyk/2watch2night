package dev.paj.towatchtonight.data.model.movie;

import com.google.gson.annotations.SerializedName;

public class MovieVideo {

    @SerializedName("key")
    private String youtubeKey;

    @SerializedName("site")
    private String site;

    @SerializedName("type")
    private String type;

    @SerializedName("name")
    String name;

    public String getName() {
        return name;
    }

    public String getYoutubeKey() {
        return youtubeKey;
    }

    public String getSite() {
        return site;
    }

    public String getType() {
        return type;
    }
}
