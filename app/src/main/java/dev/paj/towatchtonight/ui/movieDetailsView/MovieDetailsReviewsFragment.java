package dev.paj.towatchtonight.ui.movieDetailsView;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieReview;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.ui.common.RecyclerViewItemDivider;
import dev.paj.towatchtonight.util.component.DaggerMovieReviewsComponent;
import dev.paj.towatchtonight.util.module.MovieReviewsModule;

public class MovieDetailsReviewsFragment extends Fragment implements MovieDetailsContract.MovieReviewsView {

    Movie movieToShow;
    @Inject
    MovieDetailsContract.MovieReviewsPresenter presenter;

    @BindView(R.id.movie_reviews_progress_bar)
    ProgressBar progressBar;

    @BindView(R.id.movie_reviews_error)
    TextView tvError;

    @BindView(R.id.movie_reviews_list)
    RecyclerView rvReviewsList;

    @BindView(R.id.movie_reviews_empty_list_message)
    TextView tvEmptyListMessage;

    private boolean isAttached;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DaggerMovieReviewsComponent.builder()
                .movieReviewsModule(new MovieReviewsModule(this))
                .netComponent(((App)getActivity()
                        .getApplication())
                        .getNetComponent())
                .appContextComponent(((App)getActivity()
                        .getApplication())
                        .getAppContextComponent())
                .build()
                .inject(this);
        /*if (savedInstanceState != null) {
            movieToShow = savedInstanceState.getParcelable(getString(R.string.movie_parcelable_name));
        }*/
        movieToShow = getArguments().getParcelable(getString(R.string.movie_parcelable_name));
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details_reviews, container, false);
        ButterKnife.bind(this, view);
        tvError.setOnClickListener(movieReviewsErrorOnClickListener);
        presenter.getMovieReviewsData(movieToShow.getId());
        return view;
    }


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
    public void populateMovieReviewsCard(List<MovieReview> movieReviews) {
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        rvReviewsList.setVisibility(View.VISIBLE);
        rvReviewsList.addItemDecoration(new RecyclerViewItemDivider
                (this.getResources().getDrawable(R.drawable.recycler_view_item_divider)));
        MovieReviewsListAdapter adapter = new MovieReviewsListAdapter();
        adapter.setReviewsList(movieReviews);
        rvReviewsList.setAdapter(adapter);
        rvReviewsList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        rvReviewsList.setNestedScrollingEnabled(false);
    }

    @Override
    public void showMovieReviewsError(RemoteSourceError remoteSourceError) {
        if(isAttached) {
            switch (remoteSourceError) {
                case NETWORK_UNAVAILABLE:
                    tvError.setText(getString(R.string.movie_details_error_network));
                    break;
                case SERVER_ERROR:
                    tvError.setText(getString(R.string.movie_details_error_server));
                    break;
            }
            progressBar.setVisibility(View.GONE);
            rvReviewsList.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void retryPopulatingMovieReviewsCard() {
        tvError.setVisibility(View.GONE);
        tvEmptyListMessage.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        presenter.getMovieReviewsData(movieToShow.getId());
    }

    @Override
    public void showEmptyListMessage() {
        progressBar.setVisibility(View.GONE);
        rvReviewsList.setVisibility(View.GONE);
        tvEmptyListMessage.setVisibility(View.VISIBLE);
    }

    View.OnClickListener movieReviewsErrorOnClickListener = view -> retryPopulatingMovieReviewsCard();
}
