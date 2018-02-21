package dev.paj.towatchtonight.ui.mainMoviesList;


import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.Movie;
import dev.paj.towatchtonight.ui.endlessScroll.DetailsViewFromListInitiator;
import dev.paj.towatchtonight.util.network.PosterUtils;

public class MoviesListAdapter extends RecyclerView.Adapter<MoviesListAdapter.MovieViewHolder> {

    private List<Movie> moviesList;
    private Fragment fragment;

    public MoviesListAdapter(Fragment fragment) {
        this.fragment = fragment;
    }

    public void setMoviesList(List<Movie> moviesList) {
        this.moviesList = moviesList;
        notifyDataSetChanged();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list_item, null);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movie movie = moviesList.get(position);
        StringBuilder title = new StringBuilder();
        if(movie.getTitle() != null && !movie.getTitle().isEmpty()) {
            title.append(movie.getTitle());
            title.append(" ");
        }
        if (movie.getReleaseDate() != null && !movie.getReleaseDate().isEmpty()) {
            title.append(String.format("(%s)", movie.getReleaseDate().substring(0,4)));
        }

        holder.title.setText(title.toString());
        PosterUtils.loadPosterIntoView(movie.getPosterPath(), holder.poster, fragment.getContext());
    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_list_item_image)
        ImageView poster;
        @BindView(R.id.movie_list_item_title)
        TextView title;

        public MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener((view) -> {
                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {
                    ((DetailsViewFromListInitiator)fragment).startDetailsView(pos);
                }
            });
        }
    }

}
