package dev.paj.towatchtonight.ui.movieDetailsView;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsActivityContract;


public abstract class MovieDetailsActivityBase extends AppCompatActivity implements MovieDetailsActivityContract.View {

    protected Menu optionsMenu;
    protected boolean isMovieOnWatchlist;
    protected MovieDetailsActivityContract.Presenter presenter;
    protected Movie movieToShow;
    protected MovieDetails movieDetails;

    private List<Fragment> fragments;
    private String[] fragmentTitles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        optionsMenu = menu;
        getMenuInflater().inflate(R.menu.menu_movie_details_activity, menu);
        menu.getItem(0).setEnabled(false);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.menu_movie_details_watchlist_add:
                disableAddToWatchlistButton();
                if(!isMovieOnWatchlist) {
                    addCurrentMovieToDatabase();
                } else {
                    removeCurrentMovieFromWatchlist();
                }

                break;
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void addCurrentMovieToDatabase() {
        disableAddToWatchlistButton();
        presenter.addMovieToWatchlist(movieToShow, movieDetails);
    }

    @Override
    public void onMovieAddToWatchlistSuccess() {
        optionsMenu.getItem(0).setIcon(R.drawable.ic_star_white_18dp);
        enableAddToWatchlistButton();
        isMovieOnWatchlist = true;
        Toast.makeText(this, getString(R.string.movie_watchlist_add_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showAddToWatchlistError() {
        enableAddToWatchlistButton();
        Toast.makeText(this, getString(R.string.movie_watchlist_add_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void removeCurrentMovieFromWatchlist() {
        disableAddToWatchlistButton();
        presenter.removeMovieFromWatchlist(movieToShow);
    }

    @Override
    public void showRemoveFromWatchlistError() {
        enableAddToWatchlistButton();
        Toast.makeText(this, getString(R.string.movie_watchlist_remove_error), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMovieRemoveFromWatchlistSuccess() {
        optionsMenu.getItem(0).setIcon(R.drawable.ic_star_border_white_18dp);
        enableAddToWatchlistButton();
        Toast.makeText(this, getString(R.string.movie_watchlist_remove_success), Toast.LENGTH_SHORT).show();
        isMovieOnWatchlist = false;
    }

    @Override
    public void markMovieAsPresentOnWatchlist(boolean result) {
        if(result) {
            optionsMenu.getItem(0).setIcon(R.drawable.ic_star_white_18dp);
            isMovieOnWatchlist = true;
        }
        enableAddToWatchlistButton();
    }

    protected void disableAddToWatchlistButton() {
        MenuItem item = optionsMenu.getItem(0);
        item.setEnabled(false);
        item.getIcon().mutate().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
    }

    protected void enableAddToWatchlistButton() {
        MenuItem item = optionsMenu.getItem(0);
        item.setEnabled(true);
        item.getIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
    }

    protected void addFragmentsToActivity(List<Fragment> fragments, String[] fragmentTitles, ViewPager viewPager, TabLayout tabLayout) {
        this.fragments = fragments;
        this.fragmentTitles = fragmentTitles;
        viewPager.setAdapter(new MovieDetailsActivityBase.ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onMovieDetailsLoaded(MovieDetails movieDetails) {
        this.movieDetails = movieDetails;
        presenter.checkIfMovieIsPresentInWatchlist(movieToShow);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putParcelable(getString(R.string.movie_parcelable_name), movieToShow);
            Fragment fragment = fragments.get(position);

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitles[position];
        }
    }
}
