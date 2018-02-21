package dev.paj.towatchtonight.data.model;

public enum MoviesListsCriteria {
    POPULAR ("popular"),
    TOPRATED ("top_rated");

    private String description;

    MoviesListsCriteria(String description) {
        this.description = description;
    }

    public String getDescription() {
        return this.description;
    }
}
