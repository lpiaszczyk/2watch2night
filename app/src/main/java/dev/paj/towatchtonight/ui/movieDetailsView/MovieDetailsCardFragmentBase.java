package dev.paj.towatchtonight.ui.movieDetailsView;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import butterknife.BindView;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.util.network.ImageUtils;


public abstract class MovieDetailsCardFragmentBase extends Fragment implements MovieDetailsContract.MovieCardView {

    protected boolean isAttached;
    protected Movie movieToShow;
    protected MovieDetails currentMovieDetails;

    protected MovieDetailsContract.MovieCardPresenter presenter;
    //region setting views
    @BindView(R.id.movie_details_poster)
    protected ImageView ivPoster;

    @BindView(R.id.movie_details_title)
    protected TextView tvTitle;

    @BindView(R.id.movie_details_original_title)
    protected TextView tvOriginalTitle;

    @BindView((R.id.movie_details_progress_bar))
    protected ProgressBar movieDetailsProgressBar;

    @BindView(R.id.movie_details_rating)
    protected TextView tvMovieDetailsRating;

    @BindView(R.id.movie_details_runtime)
    protected TextView tvMovieDetailsRuntime;

    @BindView(R.id.movie_details_release_date)
    protected TextView tvMovieReleaseDate;

    @BindView(R.id.movie_details_error)
    protected TextView tvMovieDetailsError;

    @BindView(R.id.movie_details_plot_overview)
    protected TextView tvPlotOverview;

    @BindView(R.id.movie_details_genres_container)
    protected FlowLayout llGenresContainer;
    //endregion
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        isAttached = true;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isAttached = false;
    }

    @Override
    public void populateMovieDetailsCard(MovieDetails movieDetails) {
        if(isAttached) {
            movieDetailsProgressBar.setVisibility(View.GONE);
            tvMovieDetailsError.setVisibility(View.GONE);
            tvMovieDetailsRating.setVisibility(View.VISIBLE);
            tvMovieDetailsRuntime.setVisibility(View.VISIBLE);
            tvMovieReleaseDate.setVisibility(View.VISIBLE);
            tvPlotOverview.setVisibility(View.VISIBLE);
            populateMovieGenresList(movieDetails.getGenres());
            tvTitle.setText(movieToShow.getTitle());
            tvOriginalTitle.setText(movieToShow.getOriginalTitle());
            ImageUtils.loadPosterIntoView(movieToShow.getPosterPath(), ivPoster, this.getContext());
            tvMovieDetailsRating.setText(getString(R.string.movie_details_rating_format, Float.valueOf(movieDetails.getVoteAverage())));
            tvMovieDetailsRuntime.setText(getString(R.string.movie_details_runtime_format, movieDetails.getRuntime()));

            String releaseDate = movieToShow.getReleaseDate();
            tvMovieReleaseDate.setText(getString(R.string.movie_details_release_date, releaseDate != null ? releaseDate : "-"));

            tvPlotOverview.setText(movieDetails.getPlotOverview());
            currentMovieDetails = movieDetails;
            notifyParentAboutMovieLoaded(movieDetails);
        }

    }

    private void notifyParentAboutMovieLoaded(MovieDetails movieDetails) {
        ((MovieDetailsActivityBase)getActivity()).onMovieDetailsLoaded(movieDetails);
    }

    protected void populateMovieGenresList(Genre[] genres) {
        if(isAttached) {
            LayoutInflater li = LayoutInflater.from(getActivity());
            for(Genre genre: genres) {
                TextView genreView = (TextView)li.inflate(R.layout.genre_text_view, null);
                genreView.setText(genre.getGenreName());
                llGenresContainer.addView(genreView);
            }
        }

    }

    @Override
    public void showMovieDetailsError(RemoteSourceError remoteSourceError) {
        if(isAttached) {
            switch (remoteSourceError) {
                case NETWORK_UNAVAILABLE:
                    tvMovieDetailsError.setText(getString(R.string.movie_details_error_network));
                    break;
                case SERVER_ERROR:
                    tvMovieDetailsError.setText(getString(R.string.movie_details_error_server));
                    break;
                default:
                    tvMovieDetailsError.setText(getString(R.string.remote_error_other));
            }
            tvMovieDetailsRating.setVisibility(View.GONE);
            tvMovieDetailsRuntime.setVisibility(View.GONE);
            tvPlotOverview.setVisibility(View.GONE);
            movieDetailsProgressBar.setVisibility(View.GONE);
            tvMovieDetailsError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void retryPopulatingMovieDetailsCard() {
        tvMovieDetailsError.setVisibility(View.GONE);
        movieDetailsProgressBar.setVisibility(View.VISIBLE);
        presenter.getMovieDetailsData(movieToShow.getId());
    }

    protected View.OnClickListener movieDetailsErrorOnClickListener = view -> retryPopulatingMovieDetailsCard();

}
