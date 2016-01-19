package edu.galileo.android.androidchat.lib;

import android.content.Context;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by ykro.
 */
public class ImageLoading {
    Context context;

    public ImageLoading(Context context){
        this.context = context;
    }

    public void loadImage(String url, CircleImageView view) {
        Glide.with(context.getApplicationContext())
                .load(url)
                .into(view);
    }

}
