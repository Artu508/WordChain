package kr.jung.chat;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ChatManager {

    private final WordChainManager wordChainManager;
    private RecyclerView recyclerView;
    private final DatabaseReference rootReference;
    private final ChatAdapter adapter;
    private final List<ChatData> chatList = new ArrayList<>();
    private String nickName;

    public ChatManager(RecyclerView recyclerView, Context context, String nick) {
        wordChainManager = new WordChainManager(context.getResources());
        rootReference = FirebaseDatabase.getInstance().getReference();
        nickName = nick;

        adapter = new ChatAdapter(chatList, context, nick);
        recyclerView.setAdapter(adapter);

        rootReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                try {
                    HashMap<String, Object> data = (HashMap<String, Object>) dataSnapshot.getValue();
                    ChatData chat = new ChatData(data.get("msg") + "", data.get("nickname") + "");
                    adapter.addChat(chat);
                    recyclerView.scrollToPosition(chatList.size() - 1);
                }
                catch (Exception e) {
                    adapter.addChat(new ChatData(e.toString(), "에러"));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public ChatAdapter getAdapter() {
        return adapter;
    }

    public void sendWord(String word) {
        WordChainManager.ChainState state = wordChainManager.chainWord(word);
        if(state == WordChainManager.ChainState.NONEXISTENT_WORD) {
            addFakeMessage("그런 단어 없다 짜슥아", "[!]");
        }
        else if(state == WordChainManager.ChainState.FIRST_LEAD) {
            addFakeMessage("첫유도 양심있냐 짜슥아", "[!]");
        }
        else if(state == WordChainManager.ChainState.FIRST_NEO) {
            addFakeMessage("첫한방 양심있냐 짜슥아", "[!]");
        }
        else {
            sendMessage(word);
        }
    }

    public void sendMessage(String msg) {
        ChatData chat = new ChatData(msg, nickName);
        rootReference.push().setValue(chat);
    }

    public void addFakeMessage(String msg, String nick) {
        ChatData chat = new ChatData(msg, nick);
        adapter.addChat(chat);
        recyclerView.scrollToPosition(chatList.size() - 1);
    }

    public WordChainManager getWordChainManager() {
        return wordChainManager;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public DatabaseReference getRootReference() {
        return rootReference;
    }

    public List<ChatData> getChatList() {
        return chatList;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
