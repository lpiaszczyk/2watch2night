package dev.paj.towatchtonight.ui.searchMovies;

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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.SearchMoviesFragmentContract;
import dev.paj.towatchtonight.ui.endlessScroll.DetailsViewFromListInitiator;
import dev.paj.towatchtonight.ui.endlessScroll.EndlessScrollList;
import dev.paj.towatchtonight.ui.endlessScroll.EndlessScrollListener;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesListAdapter;
import dev.paj.towatchtonight.util.component.DaggerSearchMoviesFragmentComponent;
import dev.paj.towatchtonight.util.module.EndlessScollListenerModule;
import dev.paj.towatchtonight.util.module.MoviesListAdapterModule;
import dev.paj.towatchtonight.util.module.SearchMoviesFragmentModule;

public class SearchMoviesFragment extends Fragment implements SearchMoviesFragmentContract.View, EndlessScrollList, DetailsViewFromListInitiator{

    @Inject
    SearchMoviesFragmentContract.Presenter presenter;

    @Inject
    EndlessScrollListener endlessScrollListener;

    @Inject
    MoviesListAdapter adapter;

    @BindView(R.id.rv_movies_search_list)
    RecyclerView rvMoviesList;

    @BindView(R.id.swipe_refresh_search_movies_view)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.tv_search_movies_view_error)
    TextView tvErrorView;

    @BindView(R.id.search_movies_filters_view)
    ExpandableLayout filtersList;

    @BindView(R.id.search_movies_search)
    Button searchButton;

    @BindView(R.id.search_movies_show_filters)
    Button showFilters;

    @BindView(R.id.search_movies_query_clear)
    Button clearQuery;

    @BindView(R.id.search_movies_query_text)
    EditText queryTextInput;

    private String queryText;
    private List<Movie> moviesList;

    boolean isAttached;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerSearchMoviesFragmentComponent
                .builder()
                .searchMoviesFragmentModule(new SearchMoviesFragmentModule(this))
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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movies_search, container, false);
        ButterKnife.bind(this, view);
        setupListeners();
        swipeRefreshLayout.setOnRefreshListener(() -> {
            filtersList.collapse();
            presenter.initMoviesList(queryText);
        });

        return view;
    }

    private void setupListeners() {
        showFilters.setOnClickListener(view -> {
            if(filtersList.isExpanded()) {
                hideSoftKeyboard();
                filtersList.collapse();
            } else {
                filtersList.expand();
            }
        });
        clearQuery.setOnClickListener(view -> {
            queryTextInput.setText("");
        });
        searchButton.setOnClickListener(view -> {
            queryText = queryTextInput.getText().toString();
            if(queryText.isEmpty()){
                Toast.makeText(this.getContext(), getString(R.string.search_query_empty_error), Toast.LENGTH_SHORT).show();
            } else {
                filtersList.collapse();
                hideSoftKeyboard();
                swipeRefreshLayout.setRefreshing(true);
                presenter.initMoviesList(queryText);
            }
        });
    }

    @Override
    public void loadMoreDataToList() {
        swipeRefreshLayout.setRefreshing(true);
        presenter.loadNextPage();
    }

    @Override
    public void startDetailsView(int positionOnList) {
        if((positionOnList < moviesList.size())
                && isAttached) {
            presenter.startMovieDetailsFragment(moviesList.get(positionOnList), (SearchMoviesActivity)getActivity(), false);
        } else if (isAttached){
            Toast.makeText(getActivity(), getString(R.string.movie_details_start_error), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void showError(RemoteSourceError errorType) {
        if ((moviesList == null || moviesList.isEmpty())
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
                    Toast.makeText(getActivity(), R.string.remote_error_network, Toast.LENGTH_SHORT).show();
                    endlessScrollListener.onNetworkError();
                    break;
                case SERVER_ERROR:
                    Toast.makeText(getActivity(), R.string.remote_error_server, Toast.LENGTH_SHORT).show();
                    break;
                case OTHER:
                    Toast.makeText(getActivity(), R.string.remote_error_other, Toast.LENGTH_SHORT).show();
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
            swipeRefreshLayout.setRefreshing(false);
            if(!movies.isEmpty()){
                this.moviesList = movies;
                adapter.setMoviesList(movies);
                rvMoviesList.setAdapter(adapter);
                rvMoviesList.setLayoutManager(new GridLayoutManager(this.getContext(), 2));

                rvMoviesList.addOnScrollListener(endlessScrollListener);
            } else {
                Toast.makeText(this.getContext(), getString(R.string.error_empty_search_result), Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void addMoviesToList(List<Movie> movies) {
        swipeRefreshLayout.setRefreshing(false);
        moviesList.addAll(movies);
        adapter.notifyDataSetChanged();
        endlessScrollListener.onLoadingFinished();
    }

    private void hideSoftKeyboard() {
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
