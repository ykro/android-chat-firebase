package edu.galileo.android.androidchat.contactlist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.galileo.android.androidchat.R;
import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class ContactListAdapter extends RecyclerView.Adapter <ContactListAdapter.ViewHolder> {
    private List<User> contactList;
    private Context context;

    public ContactListAdapter(Context context, List<User> contactList) {
        this.contactList = contactList;
        this.context = context;
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_contact_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = contactList.get(position);
        String email = user.getEmail();
        boolean online = user.isOnline();
        String status = online ? "online" : "offline";
        int color = online ? Color.GREEN : Color.RED;

        holder.txtUser.setText(email);
        holder.txtStatus.setText(status);
        holder.txtStatus.setTextColor(color);

        Glide.with(context.getApplicationContext())
                .load(user.getAvatarURL())
                .into(holder.imgAvatar);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }


    public int getPositionByUsername(String username) {
        int position = 0;
        for (User user : contactList) {
            if (user.getEmail().equals(username)) {
                break;
            }
            position++;
        }

        return position;
    }

    public void add(User user) {
        this.contactList.add(user);
        this.notifyItemInserted(contactList.size() -1);
    }

    public void update(User user) {
        int pos = getPositionByUsername(user.getEmail());
        contactList.set(pos, user);
        notifyItemChanged(pos);
    }

    public void remove(User user) {
        int pos = getPositionByUsername(user.getEmail());
        contactList.remove(pos);
        notifyItemRemoved(pos);
        notifyItemRangeChanged(pos, contactList.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgAvatar) CircleImageView imgAvatar;
        @Bind(R.id.txtStatus) TextView txtStatus;
        @Bind(R.id.txtUser) TextView txtUser;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
