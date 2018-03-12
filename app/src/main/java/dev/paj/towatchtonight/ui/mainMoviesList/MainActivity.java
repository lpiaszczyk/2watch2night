package dev.paj.towatchtonight.ui.mainMoviesList;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.dependencyContract.MainActivityContract;
import dev.paj.towatchtonight.presenter.mainMoviesList.MainActivityPresenter;
import dev.paj.towatchtonight.ui.MainNavDrawerNavigationItemSelectedListener;
import dev.paj.towatchtonight.util.component.DaggerMainActivityComponent;
import dev.paj.towatchtonight.util.module.MainActivityModule;

public class MainActivity extends AppCompatActivity implements MainActivityContract.View{

    @Inject
    MainActivityPresenter presenter;

    @BindView(R.id.main_activity_frame_layout)
    FrameLayout container;

    @BindView(R.id.main_navigation_drawer)
    NavigationView nvNavigationView;

    @BindView(R.id.drawer_layout)
    DrawerLayout dlDrawerLayout;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        DaggerMainActivityComponent
                .builder()
                .mainActivityModule(new MainActivityModule(this))
                .build()
                .inject(this);

        presenter.loadInitView(this, container.getId(), "MoviesViewFragment");

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, dlDrawerLayout, R.string.nav_drawer_opened, R.string.nav_drawer_closed) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
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
        return actionBarDrawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
