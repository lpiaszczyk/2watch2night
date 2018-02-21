package dev.paj.towatchtonight.ui.movieDetailsView;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.MovieReview;

public class MovieReviewsListAdapter extends RecyclerView.Adapter<MovieReviewsListAdapter.VideoViewHolder> {

    private List<MovieReview> reviewsList;
    public void setReviewsList(List<MovieReview> reviewsList) {
        this.reviewsList = reviewsList;
        notifyDataSetChanged();
    }

    @Override
    public MovieReviewsListAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_list_item, null);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieReviewsListAdapter.VideoViewHolder holder, int position) {
        MovieReview review = reviewsList.get(position);
        holder.tvReviewAuthor.setText(review.getReviewAuthor());
        holder.tvReviewContent.setText(review.getReviewContent());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.review_item_author)
        TextView tvReviewAuthor;
        @BindView(R.id.review_item_content)
        TextView tvReviewContent;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
