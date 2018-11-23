package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
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

public class AllUsersActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    List<AllUsersData> allUsersDataList;
    RecyclerView recyclerView;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.all_users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        allUsersDataList = new ArrayList<>();
        loader=findViewById(R.id.loader);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // API 5+ solution
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loader.setVisibility(View.VISIBLE);
        myRef.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                allUsersDataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    AllUsersData allUsersData = snapshot.getValue(AllUsersData.class);
                    allUsersDataList.add(allUsersData);
                 allUsersData.setUserId(snapshot.getKey());
                }
                loader.setVisibility(View.GONE);
                AllUsersAdapter adapter=new AllUsersAdapter(AllUsersActivity.this,allUsersDataList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
