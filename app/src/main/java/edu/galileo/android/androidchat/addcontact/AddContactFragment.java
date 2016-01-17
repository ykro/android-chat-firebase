package edu.galileo.android.androidchat.addcontact;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import edu.galileo.android.androidchat.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddContactFragment extends DialogFragment
                                implements  AddContactView {

    @Bind(R.id.editTxtEmail)        EditText inputEmail;
    @Bind(R.id.progressBar)         ProgressBar progressBar;
    @Bind(R.id.layoutMainContainer) RelativeLayout container;

    private AddContactPresenter addContactPresenter;

    public AddContactFragment() {
        addContactPresenter = new AddContactPresenterImpl(this);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder =  new  AlertDialog.Builder(getActivity())
                .setTitle(R.string.addcontact_message_title)
                .setPositiveButton(R.string.addcontact_message_add,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                addContactPresenter.addContact(inputEmail.getText().toString());
                            }
                        }
                )
                .setNegativeButton(R.string.addcontact_message_cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );

        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.fragment_add_contact_dialog,null);
        ButterKnife.bind(this, view);

        builder.setView(view);
        return builder.create();
    }

    @Override
    public void showInput() {
        inputEmail.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInput() {
        inputEmail.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void contactAdded() {
        Log.e("ASDF","agregado");
        Snackbar.make(container, R.string.addcontact_message_contactadded, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void contactNotAdded() {
        inputEmail.setText("");
        inputEmail.setError(getString(R.string.addcontact_error_message));
    }
}
