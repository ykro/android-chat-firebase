package edu.galileo.android.androidchat.util;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import de.greenrobot.event.EventBus;
import edu.galileo.android.androidchat.chat.ChatMessageEvent;
import edu.galileo.android.androidchat.entities.ChatMessage;

/**
 * Created by ykro.
 */
public class ChatUtil {
    private Firebase dataReference;
    private BackendUtil backendUtil;
    private ChildEventListener chatEventListener;

    private final static String SEPARATOR = "___";
    private final static String CHATS_PATH = "chats";

    private String sender, receiver;

    private static class SingletonHolder {
        private static final ChatUtil INSTANCE = new ChatUtil();
    }
    public static ChatUtil getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ChatUtil(){
        backendUtil = BackendUtil.getInstance();
        dataReference = backendUtil.getDataReference();
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender() {
        this.sender = backendUtil.getAuthUserEmail();
    }

    public void subscribeForChatUpates() {
        if (chatEventListener == null) {

            chatEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String previousChildKey) {
                    ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
                    String msgSender = chatMessage.getSender();
                    msgSender = msgSender.replace("_",".");
                    chatMessage.setSentByMe(msgSender.equals(sender));

                    ChatMessageEvent chatMessageEvent = new ChatMessageEvent(chatMessage);
                    EventBus.getDefault().post(chatMessageEvent);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String previousChildKey) {}

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {}

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {}

                @Override
                public void onCancelled(FirebaseError firebaseError) {}
            };
            getChatsReference().addChildEventListener(chatEventListener);
        }
    }

    public void unSubscribeForChatUpates() {
        if (chatEventListener != null) {
            dataReference.removeEventListener(chatEventListener);
        }
    }

    public void destroyChatListener() {
        chatEventListener = null;
    }

    public void sendMessage(String msg) {
        String keySender = this.sender.replace(".","_");
        ChatMessage chatMessage = new ChatMessage(keySender, msg);
        getChatsReference().push().setValue(chatMessage);
    }

    private Firebase getChatsReference(){
        String keySender = this.sender.replace(".","_");
        String keyReceiver = this.receiver.replace(".","_");

        String keyChat = keySender + SEPARATOR + keyReceiver;
        if (keySender.compareTo(keyReceiver) > 0) {
            keyChat = keyReceiver + SEPARATOR + keySender;
        }
        return dataReference.getRoot().child(CHATS_PATH).child(keyChat);
    }
}
