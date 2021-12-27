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
    private final Context context;
    private RecyclerView recyclerView;
    private final DatabaseReference rootReference;
    private final ChatAdapter adapter;
    private final List<ChatData> chatList = new ArrayList<>();
    private String nickName;

    public ChatManager(RecyclerView recyclerView, Context context, String nick) {
        this.context = context;
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
                    ChatData chat = new ChatData(data.get("msg") + "",
                            data.get("nickname") + "",
                            data.get("type") != null ? data.get("type") + "" : "chat");
                    if(chat.getType().equals("word"))
                        wordChainManager.chainWord(chat.getMsg());
                    adapter.addChat(chat);
                    recyclerView.scrollToPosition(chatList.size() - 1);
                }
                catch (Exception e) {
                    warn(e.toString());
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
        switch (state) {
            case UNMATCHED_CHAIN:
                warn("끝자리가 맞지 않습니다.");
                break;
            case NONEXISTENT_WORD:
                warn("단어가 존재하지 않습니다. (기준: (구)표준국어대사전 명사단어)");
                break;
            case FIRST_LEAD:
                warn("첫 단어로 한방 유도 단어를 사용하실 수 없습니다.");
                break;
            case FIRST_NEO:
                warn("첫 단어로 한방 단어를 사용하실 수 없습니다.");
                break;
            case USED_WORD:
                warn("이미 사용한 단어입니다.");
                break;
            default:
                sendMessage(word, "word");
                break;
        }
    }

    public void sendMessage(String msg) {
        sendMessage(msg, "chat");
    }

    public void sendMessage(String msg, String type) {
        ChatData chat = new ChatData(msg, nickName, type);
        rootReference.push().setValue(chat);
    }

    public void warn(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
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
