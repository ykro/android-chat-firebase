package edu.galileo.android.androidchat.addcontact;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.galileo.android.androidchat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactDialogFragment extends DialogFragment {


    public AddContactDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_contact_dialog, container, false);
    }

}
