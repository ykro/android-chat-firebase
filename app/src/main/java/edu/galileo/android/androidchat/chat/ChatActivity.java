package edu.galileo.android.androidchat.chat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import edu.galileo.android.androidchat.R;
import edu.galileo.android.androidchat.util.AvatarUtil;

public class ChatActivity extends AppCompatActivity {
    @Bind(R.id.imgAvatar) CircleImageView imgAvatar;
    @Bind(R.id.txtStatus) TextView txtStatus;
    @Bind(R.id.txtUser)   TextView txtUser;

    public final static String EMAIL_KEY = "email";
    public final static String ONLINE_KEY = "online";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent i = getIntent();
        String email = i.getStringExtra(EMAIL_KEY);
        boolean online = i.getBooleanExtra(ONLINE_KEY, false);
        String status = online ? "online" : "offline";
        int color = online ? Color.GREEN : Color.RED;

        txtUser.setText(email);
        txtStatus.setText(status);
        txtStatus.setTextColor(color);

        Glide.with(getApplicationContext())
                .load(AvatarUtil.getAvatarUrl(email))
                .into(imgAvatar);

    }

}
