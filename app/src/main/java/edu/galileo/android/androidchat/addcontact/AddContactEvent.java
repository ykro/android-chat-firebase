package edu.galileo.android.androidchat.addcontact;

/**
 * Created by ykro.
 */
public class AddContactEvent {
    boolean error = false;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }
}
