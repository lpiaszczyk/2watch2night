package dev.paj.towatchtonight.ui.movieDetailsView;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import javax.inject.Inject;

import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.dependencyContract.MovieDetailsContract;
import dev.paj.towatchtonight.util.component.DaggerMovieDetailsComponent;
import dev.paj.towatchtonight.util.module.MovieDetailsModule;


public class MovieDetailsCardFragment extends MovieDetailsCardFragmentBase {

    @Inject
    MovieDetailsContract.MovieCardPresenter presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        initializeDependencies();
        movieToShow = getArguments().getParcelable(getString(R.string.movie_parcelable_name));
        if(movieToShow == null){
            Toast.makeText(getContext(), R.string.show_movie_details_error, Toast.LENGTH_SHORT).show();
            this.getActivity().finish();
        }
        super.onCreate(savedInstanceState);
    }

    void initializeDependencies() {
        DaggerMovieDetailsComponent.builder()
                .movieDetailsModule(new MovieDetailsModule(this))
                .netComponent(((App)getActivity()
                        .getApplication())
                        .getNetComponent())
                .appContextComponent(((App)getActivity()
                        .getApplication())
                        .getAppContextComponent())
                .build()
                .inject(this);
        super.presenter = this.presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_details_card, container, false);
        ButterKnife.bind(this, view);
        presenter.getMovieDetailsData(movieToShow.getId());
        tvMovieDetailsError.setOnClickListener(movieDetailsErrorOnClickListener);

        return view;
    }

}
