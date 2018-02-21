package dev.paj.towatchtonight.ui;

import android.app.AlertDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;

import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.ui.discoverMovies.DiscoverMoviesActivity;
import dev.paj.towatchtonight.ui.mainMoviesList.MainActivity;
import dev.paj.towatchtonight.ui.moviesWatchlist.MovieWatchlistActivity;
import dev.paj.towatchtonight.ui.searchMovies.SearchMoviesActivity;
import dev.paj.towatchtonight.ui.upcomingMoviesList.UpcomingMoviesActivity;
import dev.paj.towatchtonight.util.navigator.StartDiscoverMoviesActivity;
import dev.paj.towatchtonight.util.navigator.StartMainActivity;
import dev.paj.towatchtonight.util.navigator.StartMovieWatchlistActivity;
import dev.paj.towatchtonight.util.navigator.StartSearchMoviesActivity;
import dev.paj.towatchtonight.util.navigator.StartUpcomingMoviesActivity;

public class MainNavDrawerNavigationItemSelectedListener implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private AppCompatActivity currentActivity;

    public MainNavDrawerNavigationItemSelectedListener(DrawerLayout drawerLayout, AppCompatActivity currentActivity) {
        this.drawerLayout = drawerLayout;
        this.currentActivity = currentActivity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {
            case R.id.nav_item_menu_movies_lists:
                if(!(currentActivity instanceof MainActivity)) {
                    new StartMainActivity(currentActivity).navigate();
                    currentActivity.finish();
                }
                drawerLayout.closeDrawers();
                return true;
            case R.id.nav_item_menu_movies_watchlist:
                if(!(currentActivity instanceof MovieWatchlistActivity)) {
                    new StartMovieWatchlistActivity(currentActivity).navigate();
                    currentActivity.finish();
                }
                drawerLayout.closeDrawers();
                return true;
            case R.id.nav_item_menu_movies_discover:
                if(!(currentActivity instanceof DiscoverMoviesActivity)) {
                    new StartDiscoverMoviesActivity(currentActivity).navigate();
                    currentActivity.finish();
                }
                drawerLayout.closeDrawers();
                return true;
            case R.id.nav_item_menu_movies_upcoming:
                if(!(currentActivity instanceof UpcomingMoviesActivity)) {
                    new StartUpcomingMoviesActivity(currentActivity).navigate();
                    currentActivity.finish();
                }
                drawerLayout.closeDrawers();
                return true;
            case R.id.nav_item_menu_movies_search:
                if(!(currentActivity instanceof SearchMoviesActivity)) {
                    new StartSearchMoviesActivity(currentActivity).navigate();
                    currentActivity.finish();
                }
                drawerLayout.closeDrawers();
                return true;
            case R.id.nav_item_menu_about:
                AlertDialog.Builder builder = new AlertDialog.Builder(currentActivity);
                LayoutInflater inflater = currentActivity.getLayoutInflater();
                builder.setView(inflater.inflate(R.layout.dialog_about_app, null));
                builder.create().show();
                drawerLayout.closeDrawers();
                return true;
        }
        drawerLayout.closeDrawers();
        return false;
    }
}
