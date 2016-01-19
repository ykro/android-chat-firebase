package edu.galileo.android.androidchat.api;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import edu.galileo.android.androidchat.chat.ChatEvent;
import edu.galileo.android.androidchat.lib.EventBus;
import edu.galileo.android.androidchat.model.ChatMessage;

/**
 * Created by ykro.
 */
public class ChatAPI {
    private Firebase dataReference;
    private APIHelper apiHelper;
    private ChildEventListener chatEventListener;

    private final static String SEPARATOR = "___";
    private final static String CHATS_PATH = "chats";

    private String sender, receiver;

    private static class SingletonHolder {
        private static final ChatAPI INSTANCE = new ChatAPI();
    }
    public static ChatAPI getInstance() {
        return SingletonHolder.INSTANCE;
    }

    public ChatAPI(){
        apiHelper = APIHelper.getInstance();
        dataReference = apiHelper.getDataReference();
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setSender() {
        this.sender = apiHelper.getAuthUserEmail();
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

                    ChatEvent chatEvent = new ChatEvent(chatMessage);
                    EventBus eventBus = EventBus.getInstance();
                    eventBus.post(chatEvent);
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
