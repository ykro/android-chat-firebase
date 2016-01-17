package edu.galileo.android.androidchat;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Created by ykro.
 */
public class AndroidChatApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
