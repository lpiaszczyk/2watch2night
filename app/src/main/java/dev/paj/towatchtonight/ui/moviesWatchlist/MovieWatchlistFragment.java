package dev.paj.towatchtonight.ui.moviesWatchlist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import dev.paj.towatchtonight.data.source.local.LocalSourceError;
import dev.paj.towatchtonight.dependencyContract.MovieWatchlistFragmentContract;
import dev.paj.towatchtonight.ui.endlessScroll.DetailsViewFromListInitiator;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesListAdapter;
import dev.paj.towatchtonight.util.component.DaggerDatabaseAccessComponent;
import dev.paj.towatchtonight.util.component.DaggerMovieWatchlistFragmentComponent;
import dev.paj.towatchtonight.util.module.DatabaseAccessModule;
import dev.paj.towatchtonight.util.module.MovieWatchlistFragmentModule;
import dev.paj.towatchtonight.util.module.MoviesListAdapterModule;
import dev.paj.towatchtonight.util.module.WatchlistDatabaseHelperModule;

public class MovieWatchlistFragment extends Fragment implements MovieWatchlistFragmentContract.View, DetailsViewFromListInitiator {

    @Inject
    MovieWatchlistFragmentContract.Presenter presenter;

    @Inject
    MoviesListAdapter moviesListAdapter;

    @BindView(R.id.rv_movies_watchlist_list)
    RecyclerView rvMoviesList;

    @BindView(R.id.tv_movies_watchlist_error)
    TextView tvErrorView;

    private List<Movie> moviesList;

    private boolean isAttached;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DaggerMovieWatchlistFragmentComponent.builder()
                .databaseAccessComponent
                (DaggerDatabaseAccessComponent.builder()
                        .databaseAccessModule(new DatabaseAccessModule())
                        .watchlistDatabaseHelperModule(new WatchlistDatabaseHelperModule())
                        .appContextComponent(((App)getActivity().getApplication())
                                .getAppContextComponent())
                        .build())
                .movieWatchlistFragmentModule(new MovieWatchlistFragmentModule(this))
                .moviesListAdapterModule(new MoviesListAdapterModule(this))
                .build()
                .inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_watchlist, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        presenter.initMoviesList();
        super.onResume();
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
    public void showError(LocalSourceError error) {
        if(isAttached) {
            tvErrorView.setText(R.string.local_error_get_movies);
            tvErrorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideError() {
        if(isAttached) {
            tvErrorView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showMoviesList(List<Movie> movies) {
        if(isAttached){
            hideError();
            this.moviesList = movies;
            moviesListAdapter.setMoviesList(movies);
            moviesListAdapter.notifyDataSetChanged();
            rvMoviesList.setAdapter(moviesListAdapter);
            rvMoviesList.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        }
        if(movies.isEmpty()) {
            showEmptyListMessage();
        }
    }

    @Override
    public void startDetailsView(int positionOnList) {
        if((positionOnList < moviesList.size())
                && isAttached) {
            presenter.startMovieDetailsFragment(moviesList.get(positionOnList), (MovieWatchlistActivity)getActivity(), true);
        } else if (isAttached){
            Toast.makeText(getActivity(), getString(R.string.movie_details_start_error), Toast.LENGTH_LONG).show();
        }
    }

    private void showEmptyListMessage() {
        if(isAttached) {
            tvErrorView.setText(R.string.local_error_empty_movie_watchlist);
            tvErrorView.setVisibility(View.VISIBLE);
        }
    }
}
