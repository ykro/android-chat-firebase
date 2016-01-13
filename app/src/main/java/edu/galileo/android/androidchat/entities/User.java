package edu.galileo.android.androidchat.entities;

import java.util.ArrayList;

/**
 * Created by ykro.
 */
public class User {
    String username;
    boolean online;
    ArrayList<String> contacts;

    public User(){ }

    public User(String username, boolean online, ArrayList<String> contacts){
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

    public ArrayList<String> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<String> contacts) {
        this.contacts = contacts;
    }
}
