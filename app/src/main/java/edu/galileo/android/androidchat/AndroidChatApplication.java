package edu.galileo.android.androidchat;

import android.app.Application;

import com.firebase.client.Firebase;

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
    }

    private void setupFirebase(){
        Firebase.setAndroidContext(this);
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
    }
}
