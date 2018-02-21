package dev.paj.towatchtonight.dependencyContract;

import java.util.List;

import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.data.model.movie.MovieReview;
import dev.paj.towatchtonight.data.model.movie.MovieVideo;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;

public interface MovieDetailsContract {
    interface MovieCardView {
        void populateMovieDetailsCard(MovieDetails movieDetails);
        void showMovieDetailsError(RemoteSourceError remoteSourceError);
        void retryPopulatingMovieDetailsCard();

    }

    interface MovieCardPresenter {
        void getMovieDetailsData(String movieId);
    }

    interface MovieVideosView {
        void populateMovieVideosCard(List<MovieVideo> movieVideos);
        void showMovieVideosError(RemoteSourceError remoteSourceError);
        void retryPopulatingMovieVideosCard();
        void showEmptyListMessage();
    }

    interface MovieVideosPresenter {
        void getMovieVideosData(String movieId);
    }

    interface MovieReviewsView {
        void populateMovieReviewsCard(List<MovieReview> movieReviews);
        void showMovieReviewsError(RemoteSourceError remoteSourceError);
        void retryPopulatingMovieReviewsCard();
        void showEmptyListMessage();
    }

    interface MovieReviewsPresenter {
        void getMovieReviewsData(String movieId);
    }
}
