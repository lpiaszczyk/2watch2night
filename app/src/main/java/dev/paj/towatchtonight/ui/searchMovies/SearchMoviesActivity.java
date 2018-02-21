package dev.paj.towatchtonight.ui.searchMovies;

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
import dev.paj.towatchtonight.dependencyContract.SearchMoviesActivityContract;
import dev.paj.towatchtonight.ui.MainNavDrawerNavigationItemSelectedListener;
import dev.paj.towatchtonight.util.component.DaggerSearchMoviesActivityComponent;
import dev.paj.towatchtonight.util.module.SearchMoviesActivityModule;

public class SearchMoviesActivity extends AppCompatActivity implements SearchMoviesActivityContract.View {

    @Inject
    SearchMoviesActivityContract.Presenter presenter;

    @BindView(R.id.search_movies_activity_frame_layout)
    FrameLayout container;

    @BindView(R.id.main_navigation_drawer)
    NavigationView nvNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout dlDrawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_movies);
        ButterKnife.bind(this);
        DaggerSearchMoviesActivityComponent
                .builder()
                .searchMoviesActivityModule(new SearchMoviesActivityModule(this))
                .build()
                .inject(this);

        presenter.loadInitView(this, container.getId(), "SearchMoviesFragment");


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
