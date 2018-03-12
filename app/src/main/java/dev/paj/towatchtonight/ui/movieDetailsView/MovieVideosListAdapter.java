package dev.paj.towatchtonight.ui.movieDetailsView;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import dev.paj.towatchtonight.R;
import dev.paj.towatchtonight.data.model.movie.MovieVideo;
import dev.paj.towatchtonight.util.network.NetworkUtils;
import dev.paj.towatchtonight.util.network.ImageUtils;

public class MovieVideosListAdapter extends RecyclerView.Adapter<MovieVideosListAdapter.VideoViewHolder> {

    private List<MovieVideo> videosList;
    private NetworkUtils networkUtils;
    private Context appContext;

    @Inject
    public MovieVideosListAdapter(NetworkUtils networkUtils, Context appContext) {
        this.networkUtils = networkUtils;
        this.appContext = appContext;
    }

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
        if (networkUtils.isNetworkAvailable()) {
            ImageUtils.loadYoutubeThumbnail(video.getYoutubeKey(), holder.ivThumbnail, appContext);
        }
    }

    @Override
    public int getItemCount() {
        return videosList.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.movie_video_item_title)
        TextView tvTitle;
        @BindView(R.id.movie_video_thumbnail)
        ImageView ivThumbnail;

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
