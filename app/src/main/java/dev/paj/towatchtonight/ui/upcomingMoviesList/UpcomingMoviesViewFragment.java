package dev.paj.towatchtonight.ui.upcomingMoviesList;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.UpcomingMoviesViewContract;
import dev.paj.towatchtonight.presenter.upcomingMoviesList.UpcomingMoviesViewPresenter;
import dev.paj.towatchtonight.ui.endlessScroll.DetailsViewFromListInitiator;
import dev.paj.towatchtonight.ui.endlessScroll.EndlessScrollList;
import dev.paj.towatchtonight.ui.endlessScroll.EndlessScrollListener;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesListAdapter;
import dev.paj.towatchtonight.util.component.DaggerUpcomingMoviesViewComponent;
import dev.paj.towatchtonight.util.module.EndlessScollListenerModule;
import dev.paj.towatchtonight.util.module.MoviesListAdapterModule;
import dev.paj.towatchtonight.util.module.UpcomingMoviesViewModule;


public class UpcomingMoviesViewFragment extends Fragment implements UpcomingMoviesViewContract.View, EndlessScrollList, DetailsViewFromListInitiator {

    @Inject
    UpcomingMoviesViewPresenter presenter;

    List<Movie> moviesList;

    @Inject
    EndlessScrollListener endlessScrollListener;

    @Inject
    MoviesListAdapter adapter;

    @BindView(R.id.rv_upcoming_movies_view_list)
    RecyclerView rvMoviesList;

    @BindView((R.id.tv_upcoming_movies_view_error))
    TextView tvErrorView;

    @BindView(R.id.swipe_refresh_upcoming_movies_view)
    SwipeRefreshLayout swipeRefreshLayout;

    private boolean isAttached;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerUpcomingMoviesViewComponent
                .builder()
                .upcomingMoviesViewModule(new UpcomingMoviesViewModule(this))
                .netComponent(((App)getActivity()
                        .getApplication())
                        .getNetComponent())
                .appContextComponent(((App)getActivity()
                        .getApplication())
                        .getAppContextComponent())
                .moviesListAdapterModule(new MoviesListAdapterModule(this))
                .endlessScollListenerModule(new EndlessScollListenerModule(this))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upcoming_movies_view, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setRefreshing(true);
        presenter.initMoviesList();
        swipeRefreshLayout.setOnRefreshListener(() -> presenter.initMoviesList());

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
    public void showError(RemoteSourceError errorType) {
        if((moviesList == null || moviesList.isEmpty())
                && isAttached) {
            switch (errorType) {
                case NETWORK_UNAVAILABLE:
                    tvErrorView.setText(getString(R.string.remote_error_network));

                    break;
                case SERVER_ERROR:
                    tvErrorView.setText(getString(R.string.remote_error_server));
                    break;
                case OTHER:
                    tvErrorView.setText(getString(R.string.remote_error_other));
                    break;
            }
            swipeRefreshLayout.setVisibility(View.GONE);
            tvErrorView.setVisibility(View.VISIBLE);
        } else if (isAttached) {
            switch (errorType) {
                case NETWORK_UNAVAILABLE:
                    Toast.makeText(getActivity(), R.string.remote_error_network , Toast.LENGTH_SHORT).show();
                    endlessScrollListener.onNetworkError();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getActivity(), R.string.remote_error_server , Toast.LENGTH_SHORT).show();
                    break;
                case OTHER:
                    Toast.makeText(getActivity(), R.string.remote_error_other , Toast.LENGTH_SHORT).show();
                    break;
            }

            endlessScrollListener.onNetworkError();
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void hideError() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        tvErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showMoviesList(List<Movie> movies) {
        if(isAttached) {
            hideError();
            this.moviesList = movies;
            adapter.setMoviesList(movies);
            rvMoviesList.setAdapter(adapter);
            rvMoviesList.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

            rvMoviesList.addOnScrollListener(endlessScrollListener);
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void addMoviesToList(List<Movie> movies) {
        swipeRefreshLayout.setRefreshing(false);
        moviesList.addAll(movies);
        adapter.notifyDataSetChanged();
        endlessScrollListener.onLoadingFinished();
    }

    @Override
    public void loadMoreDataToList() {
        swipeRefreshLayout.setRefreshing(false);
        presenter.loadNextPage();
    }

    @Override
    public void startDetailsView(int positionOnList) {
        if((positionOnList < moviesList.size())
                && isAttached) {
            presenter.startMovieDetailsFragment(moviesList.get(positionOnList), (UpcomingMoviesActivity)getActivity(), false);
        } else if (isAttached){
            Toast.makeText(getActivity(), getString(R.string.movie_details_start_error), Toast.LENGTH_LONG).show();
        }
    }
}
