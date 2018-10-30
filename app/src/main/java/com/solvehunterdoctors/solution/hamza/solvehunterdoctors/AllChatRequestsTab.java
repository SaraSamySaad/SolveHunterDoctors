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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllChatRequestsTab extends Fragment {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    List<RequestsChatsData> requestsChatsDataList;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        View view = inflater.inflate(R.layout.requests_chats_tab,container,false);
        recyclerView = view.findViewById(R.id.requests);
        //loader=view.findViewById(R.id.all_doctors_loader);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        requestsChatsDataList = new ArrayList<>();
        getData();
        return view;
    }
    public void  getData(){
        myRef.child("ChatRequests").child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                requestsChatsDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    RequestsChatsData requestsChatsData = snapshot.getValue(RequestsChatsData.class);
                    requestsChatsData.setId(snapshot.getKey());
                    requestsChatsDataList.add(requestsChatsData);
                }
                RequestChatsAdapter adapter=new RequestChatsAdapter(getContext(),requestsChatsDataList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
