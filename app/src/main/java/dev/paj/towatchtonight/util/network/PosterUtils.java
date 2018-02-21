package dev.paj.towatchtonight.util.network;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;

import dev.paj.towatchtonight.R;

public class PosterUtils {

    public static void loadPosterIntoView(String posterPath, ImageView imageView, Context callingContext) {
        if(posterPath != null &&
            !posterPath.isEmpty() &&
            !posterPath.equals("null")){
            GlideApp.with(callingContext)
                    .load(createMoviePosterUri(posterPath, callingContext))
                    .error(R.drawable.poster_not_avaliable)
                    .placeholder(R.drawable.poster_placeholder)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        } else {
            GlideApp.with(callingContext)
                    .load(R.drawable.poster_not_avaliable)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imageView);
        }

    }

    private static String createMoviePosterUri(String posterPath, Context callingContext) {
        return callingContext.getString(R.string.image_api_url) + posterPath;
    }
}
