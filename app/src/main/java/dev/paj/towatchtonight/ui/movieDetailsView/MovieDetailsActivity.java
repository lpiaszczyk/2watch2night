package dev.paj.towatchtonight.ui.movieDetailsView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsActivityContract;
import dev.paj.towatchtonight.util.component.DaggerDatabaseAccessComponent;
import dev.paj.towatchtonight.util.component.DaggerMovieDetailsActivityComponent;
import dev.paj.towatchtonight.util.module.DatabaseAccessModule;
import dev.paj.towatchtonight.util.module.MovieDetailsActivityModule;
import dev.paj.towatchtonight.util.module.WatchlistDatabaseHelperModule;


public class MovieDetailsActivity extends MovieDetailsActivityBase {

    @BindView(R.id.movie_details_tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.movie_details_view_pager)
    ViewPager viewPager;

    @Inject
    MovieDetailsActivityContract.Presenter presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new MovieDetailsCardFragment());
        fragments.add(new MovieDetailsVideosFragment());
        fragments.add(new MovieDetailsReviewsFragment());

        String[] fragmentTitles = new String[] {
                getString(R.string.movie_details_card_details),
                getString(R.string.movie_details_card_videos),
                getString(R.string.movie_details_card_reviews)
        };

        super.addFragmentsToActivity(fragments, fragmentTitles, viewPager, tabLayout);

    }

    public void onMovieDetailsLoaded(MovieDetails movieDetails) {
        this.movieDetails = movieDetails;
        try {
            presenter.checkIfMovieIsPresentInWatchlist(movieToShow);
        } catch (IllegalArgumentException e) {
            Log.d(this.getClass().getSimpleName(), "Movie to show is null, finishing activity");
            Toast.makeText(this, R.string.show_movie_details_error, Toast.LENGTH_SHORT).show();
            this.finish();
        }

    }

}
