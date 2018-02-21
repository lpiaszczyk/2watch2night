package dev.paj.towatchtonight.ui.movieDetailsView;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.MovieVideo;

public class MovieVideosListAdapter extends RecyclerView.Adapter<MovieVideosListAdapter.VideoViewHolder> {

    private List<MovieVideo> videosList;

    public void setVideosList(List<MovieVideo> videosList) {
        this.videosList = videosList;
        notifyDataSetChanged();
    }

    @Override
    public MovieVideosListAdapter.VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, null);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieVideosListAdapter.VideoViewHolder holder, int position) {
        MovieVideo video = videosList.get(position);
        holder.tvTitle.setText(video.getName());
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_video_item_title)
        TextView tvTitle;

        public VideoViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(view -> {
                MovieVideo clicked = videosList.get(getAdapterPosition());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(view.getContext().getString(R.string.youtube_intent_key) + clicked.getYoutubeKey()));
                if (intent.resolveActivity(view.getContext().getPackageManager()) == null) {
                    intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(view.getContext().getString(R.string.youtube_web_uri_base) + clicked.getYoutubeKey()));
                }

                view.getContext().startActivity(intent);
            });
        }
    }
}
