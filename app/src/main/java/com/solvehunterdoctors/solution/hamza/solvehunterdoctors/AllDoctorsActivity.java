package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AllDoctorsActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    List<AllDoctorsData> allDoctorsDataList;
    RecyclerView recyclerView;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_doctors);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.all_doctors);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        allDoctorsDataList = new ArrayList<>();
        loader=findViewById(R.id.loader);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loader.setVisibility(View.VISIBLE);
        myRef.child("Doctors").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allDoctorsDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AllDoctorsData allDoctorsData = snapshot.getValue(AllDoctorsData.class);
                    allDoctorsData.setDocId(snapshot.getKey());
                    allDoctorsDataList.add(allDoctorsData);
                }
                loader.setVisibility(View.GONE);
                AllDoctorsAdapter adapter=new AllDoctorsAdapter(AllDoctorsActivity.this,allDoctorsDataList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }
}
