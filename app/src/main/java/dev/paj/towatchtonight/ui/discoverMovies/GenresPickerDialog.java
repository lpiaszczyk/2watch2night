package dev.paj.towatchtonight.ui.discoverMovies;

import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.App;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Genre;
import dev.paj.towatchtonight.data.source.remote.RemoteSourceError;
import dev.paj.towatchtonight.dependencyContract.GenresDialogPickerContract;
import dev.paj.towatchtonight.util.component.DaggerGenresDialogPickerComponent;
import dev.paj.towatchtonight.util.module.GenresDialogPickerModule;

public class GenresPickerDialog extends DialogFragment implements GenresDialogPickerContract.View {

    @Inject
    GenresDialogPickerContract.Presenter presenter;

    @BindView(R.id.dialog_genres_picker_genres_list)
    ListView genresList;
    @BindView(R.id.dialog_genres_picker_error)
    TextView errorView;
    @BindView(R.id.dialog_genres_select_button)
    Button selectButton;

    private ArrayAdapter<String> adapter;

    private List<Genre> allGenres;
    private List<String> allGenresNames;
    private List<Genre> selectedGenres;
    private String parentFragmentTag;

    private OnGenresSelectedListener mListener;

    public static GenresPickerDialog newInstance(String parentFragmentTag, List<Genre> alreadySelected) {
        GenresPickerDialog f = new GenresPickerDialog();
        Bundle args = new Bundle();
        args.putString("parentFragmentTag", parentFragmentTag);
        f.setArguments(args);

        f.selectedGenres = alreadySelected;

        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        parentFragmentTag = getArguments().getString("parentFragmentTag");
        try {
            mListener = (OnGenresSelectedListener) ((AppCompatActivity)context).getSupportFragmentManager().findFragmentByTag(parentFragmentTag);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnGenresSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        DaggerGenresDialogPickerComponent.builder()
                .netComponent(((App)getActivity()
                        .getApplication())
                        .getNetComponent())
                .appContextComponent(((App)getActivity()
                        .getApplication())
                        .getAppContextComponent())
                .genresDialogPickerModule(new GenresDialogPickerModule(this))
                .build()
                .inject(this);
        View view = inflater.inflate(R.layout.dialog_genres_picker, container, false);
        ButterKnife.bind(this, view);

        selectButton.setOnClickListener((button) -> this.dismiss());

        presenter.initGenresList();

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener.onGenreSelected(getListChosenElements());
        selectedGenres.clear();
    }

    @Override
    public void populateGenresList(List<Genre> genres) {
        this.allGenres = genres;
        allGenresNames = new ArrayList<>();
        for(Genre genre : allGenres) {
            allGenresNames.add(genre.getGenreName());
        }
        genresList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        adapter = new ArrayAdapter<>(this.getActivity(), android.R.layout.simple_list_item_multiple_choice, allGenresNames);
        genresList.setAdapter(adapter);
        if(!selectedGenres.isEmpty()) {
            handleAlreadySelected();
        }
    }

    @Override
    public void showError(RemoteSourceError error) {
        errorView.setVisibility(View.VISIBLE);
        genresList.setVisibility(View.GONE);
    }

    @Override
    public void hideError() {
        genresList.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
    }

    private void handleAlreadySelected() {
        for(Genre genre : selectedGenres){
            for(String genreName : allGenresNames){
                if(genre.getGenreName().equals(genreName)){
                    int i = adapter.getPosition(genreName);
                    genresList.setItemChecked(i, true);
                }
            }
        }
        selectedGenres.clear();
        adapter.notifyDataSetChanged();
    }

    private List<Genre> getListChosenElements() {
        SparseBooleanArray checked = genresList.getCheckedItemPositions();
        if(checked!=null && checked.size() > 0){
            ArrayList<String> selectedItems = new ArrayList<>();
            for (int i = 0; i < checked.size(); i++) {
                int position = checked.keyAt(i);
                if (checked.valueAt(i))
                    selectedItems.add(adapter.getItem(position));
            }
            for(String item : selectedItems) {
                for(Genre genre : allGenres) {
                    if(item.equals(genre.getGenreName())) {
                        selectedGenres.add(genre);
                    }
                }
            }
        }
        return selectedGenres;
    }

    interface OnGenresSelectedListener {
        void onGenreSelected(List<Genre> selectedGenres);
    }
}
