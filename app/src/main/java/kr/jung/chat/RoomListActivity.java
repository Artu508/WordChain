package kr.jung.chat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class RoomListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button createRoom;
    private RecyclerView.LayoutManager recyclerViewLayoutManager;
    private RoomAdapter adapter;
    private List<RoomData> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_list);

        createRoom = findViewById(R.id.create_room);
        recyclerView = findViewById(R.id.room_recy);
        recyclerViewLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        adapter = new RoomAdapter(list, this);
        recyclerView.setAdapter(adapter);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference rootRef = database.getReference(), roomListRef = rootRef.child("rooms");

        roomListRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                list.add(new RoomData(snapshot.getKey()));
                Toast.makeText(RoomListActivity.this, list.size(), Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        createRoom.setOnClickListener(v -> {
            DatabaseReference room = roomListRef.push();
            room.child("name").setValue("testroom");
            room.child("max_count").setValue(5);
            room.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("isHost").setValue(true);
        });
    }
}