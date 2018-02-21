package dev.paj.towatchtonight;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.TextView;

import junit.framework.Assert;

import org.apmem.tools.layouts.FlowLayout;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import dev.paj.towatchtonight.data.model.movie.MovieDetails;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsActivity;
import dev.paj.towatchtonight.ui.movieDetailsView.MovieDetailsCardFragment;

@RunWith(RobolectricTestRunner.class)
@Config( constants = BuildConfig.class, sdk = 25 )
public class MovieDetailsCardFragmentTests {

    @Test
    public void WhenPopulateMovieDetailsCardIsCalled_ViewsArePrepared() {
        final Context context = RuntimeEnvironment.application;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(context.getString(R.string.movie_parcelable_name), TestUtils.getSampleMovieData());
        intent.putExtras(bundle);
        MovieDetailsCardFragment fragment = new MovieDetailsCardFragment();

        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putParcelable(context.getString(R.string.movie_parcelable_name),TestUtils.getSampleMovieData());
        fragment.setArguments(fragmentBundle);

        MovieDetailsActivity activity = Robolectric.buildActivity( MovieDetailsActivity.class, intent )
                .create()
                .start()
                .resume()
                .get();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();

        MovieDetails testMovieDetailsData = TestUtils.getSampleMovieDetailsData();

        fragment.populateMovieDetailsCard(testMovieDetailsData);

        TextView rating = (TextView)fragment.getView().findViewById(R.id.movie_details_rating);
        TextView runtime = (TextView)fragment.getView().findViewById(R.id.movie_details_runtime);
        TextView plotOverview = (TextView)fragment.getView().findViewById(R.id.movie_details_plot_overview);
        FlowLayout genresContainer = (FlowLayout)fragment.getView().findViewById(R.id.movie_details_genres_container);

        String expectedRatingText = context.getString(R.string.movie_details_rating_format, Float.valueOf(testMovieDetailsData.getVoteAverage()));

        Assert.assertEquals(rating.getText().toString(), expectedRatingText);
        Assert.assertEquals(runtime.getText().toString(), context.getString(R.string.movie_details_runtime_format, testMovieDetailsData.getRuntime()));
        Assert.assertEquals(plotOverview.getText().toString(), testMovieDetailsData.getPlotOverview());

        Assert.assertEquals(((TextView)genresContainer.getChildAt(0)).getText().toString(), testMovieDetailsData.getGenres()[0].getGenreName());
    }

    @Test
    public void WhenShowErrorCalled_ProperErrorTextIsVisible() {
        final Context context = RuntimeEnvironment.application;
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putParcelable(context.getString(R.string.movie_parcelable_name), TestUtils.getSampleMovieData());
        intent.putExtras(bundle);
        MovieDetailsCardFragment fragment = new MovieDetailsCardFragment();

        Bundle fragmentBundle = new Bundle();
        fragmentBundle.putParcelable(context.getString(R.string.movie_parcelable_name),TestUtils.getSampleMovieData());
        fragment.setArguments(fragmentBundle);

        MovieDetailsActivity activity = Robolectric.buildActivity( MovieDetailsActivity.class, intent )
                .create()
                .start()
                .resume()
                .get();
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add( fragment, null );
        fragmentTransaction.commit();

        TextView errorTextView = (TextView)fragment.getView().findViewById(R.id.movie_details_error);
        fragment.showMovieDetailsError(RemoteSourceError.SERVER_ERROR);
        Assert.assertEquals(errorTextView.getText().toString(), context.getString(R.string.movie_details_error_server));
        fragment.showMovieDetailsError(RemoteSourceError.NETWORK_UNAVAILABLE);
        Assert.assertEquals(errorTextView.getText().toString(), context.getString(R.string.movie_details_error_network));
        fragment.showMovieDetailsError(RemoteSourceError.OTHER);
        Assert.assertEquals(errorTextView.getText().toString(), context.getString(R.string.remote_error_other));
    }
}
