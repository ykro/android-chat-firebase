package edu.galileo.android.androidchat.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.galileo.android.androidchat.R;
import edu.galileo.android.androidchat.entities.ChatMessage;
import edu.galileo.android.androidchat.util.AvatarUtil;

public class ChatActivity extends AppCompatActivity
                          implements ChatView {

    @Bind(R.id.toolbar)              Toolbar toolbar;
    @Bind(R.id.txtUser)              TextView txtUser;
    @Bind(R.id.txtStatus)            TextView txtStatus;
    @Bind(R.id.editTxtMessage)       EditText inputMessage;
    @Bind(R.id.progressBar)          ProgressBar progressBar;
    @Bind(R.id.recyclerViewContacts) RecyclerView recyclerView;
    @Bind(R.id.imgAvatar)            CircleImageView imgAvatar;
    @Bind(R.id.contentLayout)        RelativeLayout contentLayout;

    public final static String EMAIL_KEY = "email";
    public final static String ONLINE_KEY = "online";

    private ChatAdapter adapter;
    private ChatPresenter chatPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        setToolbarData(intent);

        chatPresenter = new ChatPresenterImpl(this);
        adapter = new ChatAdapter(this, new ArrayList<ChatMessage>());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setToolbarData(Intent i) {
        String recipient = i.getStringExtra(EMAIL_KEY);
        chatPresenter.setChatRecipient(recipient);

        boolean online = i.getBooleanExtra(ONLINE_KEY, false);
        String status = online ? "online" : "offline";
        int color = online ? Color.GREEN : Color.RED;

        txtUser.setText(recipient);
        txtStatus.setText(status);
        txtStatus.setTextColor(color);

        Glide.with(getApplicationContext())
                .load(AvatarUtil.getAvatarUrl(recipient))
                .into(imgAvatar);
    }

    @Override
    public void showContent() {
        contentLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideContent() {
        contentLayout.setVisibility(View.GONE);
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
    @OnClick(R.id.btnSendMessage)
    public void sendMessage() {
        chatPresenter.sendMessage(inputMessage.getText().toString());
    }

    @Override
    public void onMessageReceived(String msg) {

    }
}
