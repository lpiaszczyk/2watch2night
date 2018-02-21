package dev.paj.towatchtonight.ui.moviesWatchlist;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsActivityContract;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsActivityBase;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsReviewsFragment;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsVideosFragment;
import dev.paj.towatchtonight.util.component.DaggerDatabaseAccessComponent;
import dev.paj.towatchtonight.util.component.DaggerMovieDetailsActivityComponent;
import dev.paj.towatchtonight.util.module.DatabaseAccessModule;
import dev.paj.towatchtonight.util.module.MovieDetailsActivityModule;
import dev.paj.towatchtonight.util.module.WatchlistDatabaseHelperModule;

public class MovieWatchlistDetailsActivity extends MovieDetailsActivityBase {

    @BindView(R.id.movie_details_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.movie_details_view_pager)
    ViewPager viewPager;

    @Inject
    MovieDetailsActivityContract.Presenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);


        DaggerMovieDetailsActivityComponent.builder()
                .movieDetailsActivityModule(new MovieDetailsActivityModule(this))
                .databaseAccessComponent
                        (DaggerDatabaseAccessComponent.builder()
                                .databaseAccessModule(new DatabaseAccessModule())
                                .watchlistDatabaseHelperModule(new WatchlistDatabaseHelperModule())
                                .appContextComponent(((App)getApplication())
                                        .getAppContextComponent())
                                .build())
                .build()
                .inject(this);
        super.presenter = this.presenter;

        movieToShow = getIntent().getExtras().getParcelable(getString(R.string.movie_parcelable_name));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new OfflineMovieDetailsCardFragment());
        fragments.add(new MovieDetailsVideosFragment());
        fragments.add(new MovieDetailsReviewsFragment());
        String[] fragmentTitles = new String[] {
                getString(R.string.movie_details_card_details),
                getString(R.string.movie_details_card_videos),
                getString(R.string.movie_details_card_reviews)
        };

        super.addFragmentsToActivity(fragments, fragmentTitles, viewPager, tabLayout);
    }
}