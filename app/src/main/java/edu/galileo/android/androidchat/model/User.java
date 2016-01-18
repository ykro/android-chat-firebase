package edu.galileo.android.androidchat.model;

import java.util.Map;

/**
 * Created by ykro.
 */
public class User {
    String email;
    boolean online;
    Map<String, Boolean> contacts;
    public final static boolean ONLINE = true;
    public final static boolean OFFLINE = false;

    public User(){ }

    public User(String email, boolean online, Map<String, Boolean> contacts){
        this.email = email;
        this.online = online;
        this.contacts = contacts;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public Map<String, Boolean> getContacts() {
        return contacts;
    }

    public void setContacts(Map<String, Boolean> contacts) {
        this.contacts = contacts;
    }
}
