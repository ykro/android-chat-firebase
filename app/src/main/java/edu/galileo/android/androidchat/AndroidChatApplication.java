package edu.galileo.android.androidchat;

import android.app.Application;

import com.firebase.client.Firebase;

import de.hdodenhof.circleimageview.CircleImageView;
import edu.galileo.android.androidchat.lib.ImageLoading;

/**
 * Created by ykro.
 */
public class AndroidChatApplication extends Application {
    ImageLoading imageLoading;

    @Override
    public void onCreate() {
        super.onCreate();
        setupFirebase();
        setupImageLoading();
    }

    private void setupImageLoading() {
        imageLoading = new ImageLoading(this);
    }

    private void setupFirebase(){
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }

    public void loadImage(String url, CircleImageView imgAvatar) {
        imageLoading.loadImage(url, imgAvatar);
    }
}
