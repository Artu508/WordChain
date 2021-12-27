package kr.jung.chat;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder>{

    private List<RoomData> mDataset;

    public RoomAdapter(List<RoomData> myDataset, Context context) {
        this.mDataset = myDataset;
    }

    @NonNull
    @Override
    public RoomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.room_item, parent, false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: " + 1234);
        RoomData room = mDataset.get(position);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference roomRef = database.getReference().child("rooms").child(room.getRid());
        roomRef.get().addOnCompleteListener(dataSnapshot -> {
            if(dataSnapshot.isSuccessful()) {
                holder.name.setText(dataSnapshot.getResult().child("room").getValue().toString());
                holder.count.setText(dataSnapshot.getResult().child("users").getChildrenCount()
                        + "/" + dataSnapshot.getResult().child("maxCount").getValue());
            }
        });
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addRoom(String rid) {
        mDataset.add(new RoomData(rid));
        notifyDataSetChanged();
        Log.d(TAG, "addRoom: " + rid);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public TextView count;
        public View rootView;
        public MyViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.room_name);
            count = v.findViewById(R.id.room_user_count);
            rootView = v;
        }
    }
}
