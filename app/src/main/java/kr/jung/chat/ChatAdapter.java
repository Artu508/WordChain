package kr.jung.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;


public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MyViewHolder> {
    private List<ChatData> mDataset;
    private String uid;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView tv_Name;
        public TextView tv_Msg;
        public View rootView;
        public MyViewHolder(View v) {
            super(v);
            tv_Name = v.findViewById(R.id.tv_Name);
            tv_Msg = v.findViewById(R.id.tv_Msg);
            rootView = v;

        }


    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatAdapter(List<ChatData> myDataset, Context context, String uid) {
        //{"1","2"}
        mDataset = myDataset;
        this.uid = uid;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        LinearLayout v = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.chatitem, parent, false);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        ChatData chat = mDataset.get(position);

        FirebaseDatabase.getInstance().getReference().child("users")
                .child(chat.getUid()).child("name").get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                holder.tv_Name.setText(task.getResult().getValue().toString());
            }
        });
        holder.tv_Msg.setText(chat.getMsg());

        if(chat.getUid().equals(this.uid)) {
            holder.tv_Msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            holder.tv_Name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        }
        else {
            holder.tv_Msg.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
            holder.tv_Name.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset == null ? 0 :  mDataset.size();
    }

    public ChatData getChat(int position) {
        return mDataset != null ? mDataset.get(position) : null;
    }

    public void addChat(ChatData chat) {
        mDataset.add(chat);
        notifyItemInserted(mDataset.size()-1); //갱신
    }

}
