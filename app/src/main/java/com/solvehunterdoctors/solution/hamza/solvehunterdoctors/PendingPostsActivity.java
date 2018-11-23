package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingPostsActivity extends AppCompatActivity {
    List<PendingPostsData> PendingPostsList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    RecyclerView recyclerView;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_posts);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.pending_posts_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PendingPostsList = new ArrayList<>();
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
        myRef.child("Pending").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PendingPostsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PendingPostsData pendingPostsData = snapshot.getValue(PendingPostsData.class);
                    pendingPostsData.setPostId(snapshot.getKey());
                    PendingPostsList.add(pendingPostsData);
                }
                PendingPostsAdapter adapter=new PendingPostsAdapter(PendingPostsActivity.this,PendingPostsList);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
