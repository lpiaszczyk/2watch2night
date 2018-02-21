package dev.paj.towatchtonight.ui.discoverMovies;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.DiscoverMoviesFragmentContract;
import dev.paj.towatchtonight.ui.endlessScroll.DetailsViewFromListInitiator;
import dev.paj.towatchtonight.ui.endlessScroll.EndlessScrollList;
import dev.paj.towatchtonight.ui.endlessScroll.EndlessScrollListener;
import dev.paj.towatchtonight.ui.mainMoviesList.MoviesListAdapter;
import dev.paj.towatchtonight.util.component.DaggerDiscoverMoviesFragmentComponent;
import dev.paj.towatchtonight.util.module.DiscoverMoviesFragmentModule;
import dev.paj.towatchtonight.util.module.EndlessScollListenerModule;
import dev.paj.towatchtonight.util.module.MoviesListAdapterModule;


public class DiscoverMoviesFragment extends Fragment implements DiscoverMoviesFragmentContract.View,
        EndlessScrollList, SelectVoteAverageDialog.OnVoteSelectedListener,
        GenresPickerDialog.OnGenresSelectedListener,
        DetailsViewFromListInitiator {

    @Inject
    DiscoverMoviesFragmentContract.Presenter presenter;

    @Inject
    EndlessScrollListener endlessScrollListener;

    @Inject
    MoviesListAdapter adapter;

    @BindView(R.id.rv_movies_discover_list)
    RecyclerView rvMoviesList;

    @BindView(R.id.swipe_refresh_discover_movies_view)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.tv_discover_movies_view_error)
    TextView tvErrorView;

    @BindView(R.id.discover_movies_show_filters)
    Button showFilters;

    @BindView(R.id.discover_movies_release_date_from_picker)
    TextView releaseDateFromPicker;

    @BindView(R.id.discover_movies_release_date_from_clear)
    Button releaseDateFromClearButton;

    @BindView(R.id.discover_movies_release_date_to_picker)
    TextView releaseDateToPicker;

    @BindView(R.id.discover_movies_release_date_to_clear)
    Button releaseDateToClearButton;

    @BindView(R.id.discover_movies_vote_average_picker)
    TextView voteAveragePicker;

    @BindView(R.id.discover_movies_vote_average_clear)
    Button voteAverageClearButton;

    @BindView(R.id.discover_movies_genres_picker)
    TextView genresPicker;

    @BindView(R.id.discover_movies_genres_clear)
    Button genresClearButton;

    @BindView(R.id.discover_movies_filters_view)
    ExpandableLayout filtersList;

    @BindView(R.id.discover_movies_search)
    Button searchButton;

    private Date releaseDateFromValue;
    private Date releaseDateToValue;

    private int voteAverageFrom = 0;
    private int voteAverageTo = 10;

    private List<Genre> genres = new ArrayList<>();

    SimpleDateFormat createFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    SimpleDateFormat printFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());

    private List<Movie> moviesList;
    private boolean isAttached;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerDiscoverMoviesFragmentComponent
                .builder()
                .discoverMoviesFragmentModule(new DiscoverMoviesFragmentModule(this))
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
        View view = inflater.inflate(R.layout.fragment_movies_discover, container, false);
        ButterKnife.bind(this, view);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            filtersList.collapse();
            presenter.initMoviesList(releaseDateFromValue, releaseDateToValue, voteAverageFrom,
                    voteAverageTo, genres);
        });
        setupFilterViewListeners();
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
    public void onVoteAverageSelected(SelectVoteAverageDialog dialog) {
        voteAverageFrom = dialog.getVoteFrom();
        voteAverageTo = dialog.getVoteTo();
        voteAveragePicker.setText(getResources().getString(R.string.dialog_select_vote_range_values, voteAverageFrom, voteAverageTo));
    }

    @Override
    public void onGenreSelected(List<Genre> selectedGenres) {
        if(selectedGenres != null){
            genres = new ArrayList<>(selectedGenres);
            if(!genres.isEmpty()) {
                genresPicker.setText(getString(R.string.discover_filters_genres_selected, genres.size()));
            } else {
                genresPicker.setText(getResources().getText(R.string.discover_filters_tap_to_select_genres));
            }
        }
    }

    @Override
    public void loadMoreDataToList() {
        swipeRefreshLayout.setRefreshing(true);
        presenter.loadNextPage();
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

    private void handleFilterPickerClick(View originator) {
        Calendar calendar;
        switch(originator.getId()) {
            case R.id.discover_movies_release_date_from_picker:
                calendar = Calendar.getInstance();
                new DatePickerDialog(this.getContext(), releaseDateFromListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            case R.id.discover_movies_release_date_to_picker:
                calendar = Calendar.getInstance();
                new DatePickerDialog(this.getContext(), releaseDateToListener,
                        calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                        .show();
                break;
            case R.id.discover_movies_vote_average_picker:
                DialogFragment dialog = SelectVoteAverageDialog.newInstance("DiscoverMoviesFragment", voteAverageFrom, voteAverageTo);
                dialog.show(this.getActivity().getFragmentManager(), "SelectVoteAverageDialog");
                break;
            case R.id.discover_movies_genres_picker:
                DialogFragment genresDialog = GenresPickerDialog.newInstance("DiscoverMoviesFragment", genres);
                genresDialog.show(this.getActivity().getFragmentManager(), "SelectGenresDialog");
                break;
        }
    }

    private void setupFilterViewListeners() {
        releaseDateFromPicker.setOnClickListener(this::handleFilterPickerClick);
        releaseDateToPicker.setOnClickListener(this::handleFilterPickerClick);
        releaseDateFromClearButton.setOnClickListener(clearReleaseFromButtonListener);
        releaseDateToClearButton.setOnClickListener(clearReleaseToButtonListener);
        voteAveragePicker.setOnClickListener(this::handleFilterPickerClick);
        genresPicker.setOnClickListener(this::handleFilterPickerClick);
        genresClearButton.setOnClickListener(clearGenresPickerListener);
        voteAverageClearButton.setOnClickListener(clearVoteAveragePickerListener);
        showFilters.setOnClickListener((v) -> {
            if(filtersList.isExpanded()) {
                filtersList.collapse();
            } else {
                filtersList.expand();
            }
        }
        );
        searchButton.setOnClickListener(searchButtonListener);
    }

    private void showFiltersErrorToast(String text) {
        Toast.makeText(this.getContext(), text, Toast.LENGTH_SHORT).show();
    }

    private boolean verifyQueryParameters(Date dateFrom, Date dateTo, int voteFrom, int voteTo) {
        boolean result = true;
        StringBuilder sb = new StringBuilder();
        if(dateFrom != null && dateTo!=null){
            if(dateFrom.after(dateTo)) {
                sb.append(getString(R.string.discover_filters_date_error));
                result = false;
            }
        }

        if (voteFrom > voteTo) {
            sb.append(getString(R.string.discover_filters_vote_error));
            result = false;
        }
        if(!result){
            this.showFiltersErrorToast(sb.toString());
        }
        return result;
    }

    @Override
    public void startDetailsView(int positionOnList) {
        if((positionOnList < moviesList.size())
                && isAttached) {
            presenter.startMovieDetailsFragment(moviesList.get(positionOnList), (DiscoverMoviesActivity)getActivity(), false);
        } else if (isAttached){
            Toast.makeText(getActivity(), getString(R.string.movie_details_start_error), Toast.LENGTH_LONG).show();
        }
    }

    private View.OnClickListener clearReleaseFromButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            releaseDateFromPicker.setText(getResources().getText(R.string.discover_filters_tap_to_select_date));
            releaseDateFromValue = null;
        }
    };

    private View.OnClickListener clearReleaseToButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            releaseDateToPicker.setText(getResources().getText(R.string.discover_filters_tap_to_select_date));
            releaseDateToValue = null;
        }
    };

    private View.OnClickListener clearGenresPickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            genres.clear();
            genresPicker.setText(getResources().getText(R.string.discover_filters_tap_to_select_genres));
        }
    };

    private View.OnClickListener clearVoteAveragePickerListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            voteAverageFrom = 0;
            voteAverageTo = 10;
            voteAveragePicker.setText(getResources().getString(R.string.discover_filters_tap_to_select_vote_average));
        }
    };

    private View.OnClickListener searchButtonListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (verifyQueryParameters(releaseDateFromValue, releaseDateToValue, voteAverageFrom, voteAverageTo)){
                filtersList.collapse();
                swipeRefreshLayout.setRefreshing(true);
                presenter.initMoviesList(releaseDateFromValue, releaseDateToValue, voteAverageFrom,
                        voteAverageTo, genres);
            }
        }
    };

    private DatePickerDialog.OnDateSetListener releaseDateFromListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            try {
                releaseDateFromValue = createFormat.parse(String.format(Locale.getDefault(), "%d-%d-%d", year, month + 1, dayOfMonth));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            releaseDateFromPicker.setText(printFormat.format(releaseDateFromValue));

        }
    };

    private DatePickerDialog.OnDateSetListener releaseDateToListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            try {
                releaseDateToValue = createFormat.parse(String.format(Locale.getDefault(), "%d-%d-%d", year, month + 1, dayOfMonth));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            releaseDateToPicker.setText(printFormat.format(releaseDateToValue));

        }
    };
}
