package edu.galileo.android.androidchat.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.galileo.android.androidchat.R;
import edu.galileo.android.androidchat.contactlist.OnItemClickListener;
import edu.galileo.android.androidchat.entities.ChatMessage;
import edu.galileo.android.androidchat.entities.User;

/**
 * Created by ykro.
 */
public class ChatAdapter extends RecyclerView.Adapter <ChatAdapter.ViewHolder> {
    private Context context;
    private List<ChatMessage> chatMessages;

    public ChatAdapter(Context context,
                       List<ChatMessage> chatMessages) {
        this.context = context;
        this.chatMessages = chatMessages;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_contact, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ChatMessage chatMessage = chatMessages.get(position);

        String msg = chatMessage.getMsg();
        String sender = chatMessage.getSender();
        /*
        holder.txtUser.setText(email);
        holder.txtStatus.setText(status);
        holder.txtStatus.setTextColor(color);
*/
    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    public void add(ChatMessage message) {
        this.chatMessages.add(message);
        this.notifyItemInserted(chatMessages.size() - 1);
        //this.notifyDataSetChanged();
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
                    listener.onItemClick(user);
                    return false;
                }
            });
        }
    }
}
