package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllChatsListTab  extends Fragment {
    RecyclerView recyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    private List<LastMessage> lastMessagesList;
    ProgressBar loader;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.all_chat_list_tab,container,false);
        recyclerView = view.findViewById(R.id.all_chats);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lastMessagesList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        loader=view.findViewById(R.id.loader);
        getData();
        return view;
    }
    public void getData(){
        loader.setVisibility(View.VISIBLE);
        myRef.child("Doctors").child(mAuth.getCurrentUser().getUid()).child("dialoges").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lastMessagesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    LastMessage lastMessage = snapshot.getValue(LastMessage.class);
                    lastMessage.setId(snapshot.getKey());
                    lastMessagesList.add(lastMessage);
                }
                AllChatListAdapter adapter=new AllChatListAdapter(getContext(),lastMessagesList);
                recyclerView.setAdapter(adapter);
                loader.setVisibility(View.GONE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
