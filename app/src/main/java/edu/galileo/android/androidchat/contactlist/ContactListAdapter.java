package edu.galileo.android.androidchat.contactlist;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
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
import edu.galileo.android.androidchat.model.User;
import edu.galileo.android.androidchat.api.AvatarAPI;

/**
 * Created by ykro.
 */
public class ContactListAdapter extends RecyclerView.Adapter <ContactListAdapter.ViewHolder> {
    private Context context;
    private List<User> contactList;
    private OnItemClickListener clickListener;

    public ContactListAdapter(Context context,
                              List<User> contactList,
                              OnItemClickListener clickListener) {

        this.context = context;
        this.contactList = contactList;
        this.clickListener = clickListener;
    }

    @Override
    public ContactListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        User user = contactList.get(position);
        holder.setClickListener(user, clickListener);

        String email = user.getEmail();
        boolean online = user.isOnline();
        String status = online ? "online" : "offline";
        int color = online ? Color.GREEN : Color.RED;

        holder.txtUser.setText(email);
        holder.txtStatus.setText(status);
        holder.txtStatus.setTextColor(color);

        Glide.with(context.getApplicationContext())
                .load(AvatarAPI.getAvatarUrl(email))
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
        //this.notifyItemInserted(contactList.size() -1);
        this.notifyDataSetChanged();
    }

    public void update(User user) {
        int pos = getPositionByUsername(user.getEmail());
        contactList.set(pos, user);
        //notifyItemChanged(pos);
        this.notifyDataSetChanged();
    }

    public void remove(User user) {
        int pos = getPositionByUsername(user.getEmail());
        contactList.remove(pos);
        //notifyItemRemoved(pos);
        //notifyItemRangeChanged(pos, contactList.size());
        this.notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.imgAvatar) CircleImageView imgAvatar;
        @Bind(R.id.txtStatus) TextView txtStatus;
        @Bind(R.id.txtUser) TextView txtUser;
        View view;

        public ViewHolder(View view) {
            super(view);
            this.view = view;
            ButterKnife.bind(this, view);
        }

        public void setClickListener(final User user,
                                     final OnItemClickListener listener) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(user);
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    listener.onItemLongClick(user);
                    return false;
                }
            });
        }
    }
}
