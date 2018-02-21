package dev.paj.towatchtonight.ui.moviesWatchlist;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.dependencyContract.MovieWatchlistActivityContract;
import dev.paj.towatchtonight.ui.MainNavDrawerNavigationItemSelectedListener;
import dev.paj.towatchtonight.util.component.DaggerMovieWatchlistActivityComponent;
import dev.paj.towatchtonight.util.module.MovieWatchlistActivityModule;

public class MovieWatchlistActivity extends AppCompatActivity implements MovieWatchlistActivityContract.View{

    @BindView(R.id.watchlist_activity_frame_layout)
    FrameLayout fragmentContainer;

    @BindView(R.id.watchlist_activity_drawer_layout)
    DrawerLayout dlDrawerLayout;

    @BindView(R.id.watchlist_activity_main_navigation_drawer)
    NavigationView nvNavigationView;

    @Inject
    MovieWatchlistActivityContract.Presenter presenter;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_watchlist);
        ButterKnife.bind(this);
        DaggerMovieWatchlistActivityComponent.builder()
                .movieWatchlistActivityModule(new MovieWatchlistActivityModule(this))
                .build().inject(this);

        presenter.loadWatchlistFragment(this, fragmentContainer.getId(), "WatchlistFragment");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dlDrawerLayout, R.string.nav_drawer_opened, R.string.nav_drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                Log.d("drawer", "drawer opened");
            }
        };
        dlDrawerLayout.addDrawerListener(actionBarDrawerToggle);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        nvNavigationView.setNavigationItemSelectedListener(new MainNavDrawerNavigationItemSelectedListener(dlDrawerLayout, this));


        actionBarDrawerToggle.syncState();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
