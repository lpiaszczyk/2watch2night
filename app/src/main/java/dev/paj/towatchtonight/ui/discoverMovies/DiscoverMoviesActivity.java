package dev.paj.towatchtonight.ui.discoverMovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
import dev.paj.towatchtonight.dependencyContract.DiscoverMoviesActivityContract;
import dev.paj.towatchtonight.presenter.discoverMovies.DiscoverMoviesActivityPresenter;
import dev.paj.towatchtonight.ui.MainNavDrawerNavigationItemSelectedListener;
import dev.paj.towatchtonight.util.component.DaggerDiscoverMoviesActivityComponent;
import dev.paj.towatchtonight.util.module.DiscoverMoviesActivityModule;


public class DiscoverMoviesActivity extends AppCompatActivity implements DiscoverMoviesActivityContract.View {

    @Inject
    DiscoverMoviesActivityPresenter presenter;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @BindView(R.id.movie_discover_activity_frame_layout)
    FrameLayout container;

    @BindView(R.id.movie_discover_activity_main_navigation_drawer)
    NavigationView nvNavigationView;

    @BindView(R.id.movie_discover_activity_drawer_layout)
    DrawerLayout dlDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_discover);
        ButterKnife.bind(this);
        DaggerDiscoverMoviesActivityComponent
                .builder()
                .discoverMoviesActivityModule(new DiscoverMoviesActivityModule(this))
                .build()
                .inject(this);

        presenter.loadInitView(this, container.getId(), "DiscoverMoviesFragment");

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
