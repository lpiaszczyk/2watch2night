package dev.paj.towatchtonight.util.network;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dev.paj.towatchtonight.data.model.movie.Genre;

public class DiscoverMoviesParamsMapUtil {

    public static Map<String, String> create(Date dateFrom, Date dateTo, int voteFrom, int voteTo, List<Genre> genres) {
        HashMap<String, String> params = new HashMap<>();
        params.put("vote_average.gte", Integer.toString(voteFrom));
        params.put("vote_average.lte", Integer.toString(voteTo));

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        if (dateFrom != null) {
            params.put("primary_release_date.gte", format.format(dateFrom));
        }
        if(dateTo != null) {
            params.put("primary_release_date.lte", format.format(dateTo));
        }

        if(genres != null && !genres.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            String sep = ",";
            for (Genre genre : genres) {
                stringBuilder.append(sep).append(genre.getGenreId());
            }
            params.put("with_genres", stringBuilder.substring(1));
        }

        return params;
    }
}
