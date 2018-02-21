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
import dev.paj.towatchtonight.data.model.movie.MovieVideo;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.presenter.movieDetailsView.MovieDetailsVideosPresenter;
import dev.paj.towatchtonight.util.component.DaggerMovieVideosComponent;
import dev.paj.towatchtonight.util.module.MovieVideosModule;

public class MovieDetailsVideosFragment extends Fragment implements MovieDetailsContract.MovieVideosView{

    @BindView(R.id.movie_videos_progress_bar)
    ProgressBar movieVideosProgressBar;
    @BindView(R.id.movie_videos_error)
    TextView tvMovieVideosError;
    @BindView(R.id.movie_videos_list)
    RecyclerView rvVideosList;
    @BindView(R.id.movie_videos_empty_list_message)
    TextView tvEmptyListMessage;

    @Inject
    MovieDetailsVideosPresenter presenter;

    Movie movieToShow;
    boolean isAttached;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DaggerMovieVideosComponent.builder()
                .movieVideosModule(new MovieVideosModule(this))
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
        View view = inflater.inflate(R.layout.fragment_movie_details_videos, container, false);
        ButterKnife.bind(this, view);
        tvMovieVideosError.setOnClickListener(movieVideosErrorOnClickListener);
        presenter.getMovieVideosData(movieToShow.getId());
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
    public void populateMovieVideosCard(List<MovieVideo> movieVideos) {
        if(isAttached) {
            movieVideosProgressBar.setVisibility(View.GONE);
            tvMovieVideosError.setVisibility(View.GONE);
            rvVideosList.setVisibility(View.VISIBLE);
            MovieVideosListAdapter adapter = new MovieVideosListAdapter();
            adapter.setVideosList(movieVideos);
            rvVideosList.setAdapter(adapter);
            rvVideosList.setLayoutManager(new LinearLayoutManager(this.getContext()));
            rvVideosList.setNestedScrollingEnabled(false);
        }

    }

    @Override
    public void showMovieVideosError(RemoteSourceError remoteSourceError) {
        if(isAttached) {
            switch (remoteSourceError) {
                case NETWORK_UNAVAILABLE:
                    tvMovieVideosError.setText(getString(R.string.movie_details_error_network));
                    break;
                case SERVER_ERROR:
                    tvMovieVideosError.setText(getString(R.string.movie_details_error_server));
                    break;
            }
            movieVideosProgressBar.setVisibility(View.GONE);
            rvVideosList.setVisibility(View.GONE);
            tvMovieVideosError.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void retryPopulatingMovieVideosCard() {
        tvMovieVideosError.setVisibility(View.GONE);
        tvEmptyListMessage.setVisibility(View.GONE);
        movieVideosProgressBar.setVisibility(View.VISIBLE);
        presenter.getMovieVideosData(movieToShow.getId());
    }

    @Override
    public void showEmptyListMessage() {
        movieVideosProgressBar.setVisibility(View.GONE);
        rvVideosList.setVisibility(View.GONE);
        tvEmptyListMessage.setVisibility(View.VISIBLE);
    }

    View.OnClickListener movieVideosErrorOnClickListener = view -> retryPopulatingMovieVideosCard();
}
