package dev.paj.towatchtonight.data.model.movie;

import java.util.List;

public class MovieResults {
    private List<Movie> results;
    private String total_results;
    private String total_pages;
    private String page;

    public List<Movie> getResults() {
        return results;
    }

    public String getTotal_results() {
        return total_results;
    }

    public String getTotal_pages() {
        return total_pages;
    }

    public String getPage() {
        return page;
    }
}
