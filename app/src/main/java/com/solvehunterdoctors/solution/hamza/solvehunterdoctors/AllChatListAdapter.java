package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AllChatListAdapter extends RecyclerView.Adapter<AllChatListAdapter.AllChatListAdapterViewHolder> {
    private Context mCtx;
    private List<LastMessage> lastMessagesList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    public AllChatListAdapter(Context mCtx, List<LastMessage> lastMessagesList) {
        this.mCtx = mCtx;
        this.lastMessagesList = lastMessagesList;
    }

    @NonNull
    @Override
    public AllChatListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.all_chat_list_item, null);
        return new AllChatListAdapter.AllChatListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllChatListAdapterViewHolder holder, int position) {
        final LastMessage lastMessage = lastMessagesList.get(position);
        myRef.child("Users").child(lastMessage.getReciverId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.reciverName.setText(String.valueOf(dataSnapshot.child("name").getValue()));

                if(!dataSnapshot.child("image").getValue().equals("")){
                    Glide.with(mCtx).load(dataSnapshot.child("image").getValue()).into(holder.reciverImage);
                }

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.lastMessage.setText(String.valueOf(lastMessage.getLastMessage()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(mCtx, ChattingActivity.class);
                mainIntent.putExtra("chatId",lastMessage.getId());
                mainIntent.putExtra("senderId",lastMessage.getReciverId());
                mCtx.startActivity(mainIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lastMessagesList.size();
    }

    class AllChatListAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView reciverName;
        TextView lastMessage;
        ImageView reciverImage;
        public AllChatListAdapterViewHolder(View itemView) {
            super(itemView);
            reciverName = itemView.findViewById(R.id.reciver_name);
            lastMessage = itemView.findViewById(R.id.last_message);
            reciverImage=itemView.findViewById(R.id.reciver_image);
        }

    }
}
