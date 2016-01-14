package edu.galileo.android.androidchat.entities;

import java.util.Map;

import edu.galileo.android.androidchat.util.BackendUtil;

/**
 * Created by ykro.
 */
public class User {
    String username;
    boolean online;
    Map<String, String> contacts;
    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;

    public User(){ }

    public User(String username, boolean online, Map<String, String> contacts){
        this.username = username;
        this.online = online;
        this.contacts = contacts;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline() {
        this.online = true;
    }

    public void setOffline() {
        this.online = false;
    }

    public Map<String, String> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, String> contacts) {
        this.contacts = contacts;
    }

    public String getAvatarURL() {
        return BackendUtil.getAvatarUrl(this.username);
    }
}
