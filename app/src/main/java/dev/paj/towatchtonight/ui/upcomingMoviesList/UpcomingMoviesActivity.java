package dev.paj.towatchtonight.ui.upcomingMoviesList;

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
import dev.paj.towatchtonight.dependencyContract.UpcomingMoviesActivityContract;
import dev.paj.towatchtonight.ui.MainNavDrawerNavigationItemSelectedListener;
import dev.paj.towatchtonight.util.component.DaggerUpcomingMoviesActivityComponent;
import dev.paj.towatchtonight.util.module.UpcomingMoviesActivityModule;


public class UpcomingMoviesActivity extends AppCompatActivity implements UpcomingMoviesActivityContract.View{

    @Inject
    UpcomingMoviesActivityContract.Presenter presenter;

    @BindView(R.id.main_navigation_drawer)
    NavigationView nvNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout dlDrawerLayout;

    @BindView(R.id.upcoming_movies_activity_frame_layout)
    FrameLayout container;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_movies);
        ButterKnife.bind(this);
        DaggerUpcomingMoviesActivityComponent
                .builder()
                .upcomingMoviesActivityModule(new UpcomingMoviesActivityModule(this))
                .build()
                .inject(this);

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

        presenter.initUpcomingList(this, container.getId(), "UpcomingMovies");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
