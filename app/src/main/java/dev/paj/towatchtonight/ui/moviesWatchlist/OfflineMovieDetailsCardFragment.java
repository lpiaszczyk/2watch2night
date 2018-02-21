package dev.paj.towatchtonight.ui.moviesWatchlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsCardFragmentBase;
import dev.paj.towatchtonight.util.component.DaggerDatabaseAccessComponent;
import dev.paj.towatchtonight.util.component.DaggerOfflineMovieDetailsComponent;
import dev.paj.towatchtonight.util.module.DatabaseAccessModule;
import dev.paj.towatchtonight.util.module.OfflineMovieDetailsModule;
import dev.paj.towatchtonight.util.module.WatchlistDatabaseHelperModule;

public class OfflineMovieDetailsCardFragment extends MovieDetailsCardFragmentBase {

    @Inject
    MovieDetailsContract.MovieCardPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        this.initializeDependencies();
        movieToShow = getArguments().getParcelable(getString(R.string.movie_parcelable_name));

        super.onCreate(savedInstanceState);
    }

    void initializeDependencies() {
        DaggerOfflineMovieDetailsComponent.builder()
                .offlineMovieDetailsModule(new OfflineMovieDetailsModule(this))
                .databaseAccessComponent(DaggerDatabaseAccessComponent.builder()
                        .databaseAccessModule(new DatabaseAccessModule())
                        .watchlistDatabaseHelperModule(new WatchlistDatabaseHelperModule())
                        .appContextComponent(((App)getActivity().getApplication())
                                .getAppContextComponent())
                        .build())
                .build()
        .inject(this);
        super.presenter = this.presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details_card, container, false);
        ButterKnife.bind(this, view);
        presenter.getMovieDetailsData(movieToShow.getId());
        tvMovieDetailsError.setOnClickListener(movieDetailsErrorOnClickListener);

        return view;
    }


}
