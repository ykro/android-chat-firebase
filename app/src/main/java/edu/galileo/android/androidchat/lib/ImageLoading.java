package edu.galileo.android.androidchat.lib;

import android.content.Context;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ykro.
 */
public class ImageLoading {

    private static class SingletonHolder {
        private static final ImageLoading INSTANCE = new ImageLoading();
    }

    public static ImageLoading getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public void loadImage(Context context, String url, CircleImageView view) {
        Glide.with(context.getApplicationContext())
                .load(url)
                .into(view);
    }
}
