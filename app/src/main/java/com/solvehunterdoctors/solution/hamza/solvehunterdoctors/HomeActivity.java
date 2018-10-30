package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    List<PostsData> PostsList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ProgressBar postsLoader;
    RecyclerView recyclerView;
    EditText homePost;
    private FirebaseAuth mAuth;
    SharedPreferences pref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        recyclerView = findViewById(R.id.user_home_posts);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PostsList = new ArrayList<>();
        postsLoader=findViewById(R.id.suser_home_loader);
        homePost=findViewById(R.id.home_post);
    }
    @Override
    protected void onResume() {
        super.onResume();
        postsLoader.setVisibility(View.VISIBLE);
        //get posts data
        getData();
        //write post
        homePost.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    postsLoader.setVisibility(View.VISIBLE);
                    myRef.child("acceptedPosts").push().setValue(new PostsData(homePost.getText().toString(),0,0,mAuth.getCurrentUser().getUid()))
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    postsLoader.setVisibility(View.GONE);
                                    homePost.setText("");
                                }
                            });
                    InputMethodManager imm = (InputMethodManager) getBaseContext().getSystemService(HomeActivity.this.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(homePost.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

    }
    public void getData(){
        myRef.child("acceptedPosts").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                PostsList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PostsData postsData = snapshot.getValue(PostsData.class);
                    postsData.setId(snapshot.getKey());
                    PostsList.add(postsData);
                    postsLoader.setVisibility(View.GONE);
                }
                HomeAdapter adapter=new HomeAdapter(HomeActivity.this,PostsList);
                recyclerView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    //and this to handle actions
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.chat) {
            Intent mainIntent = new Intent(HomeActivity.this,ChatActivity.class);
            HomeActivity.this.startActivity(mainIntent);
        }
        if (id == R.id.profile) {
            pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
            if (pref.getString("type","null").equals("admin")){
                Intent mainIntent = new Intent(HomeActivity.this,ProfileActivity.class);
                HomeActivity.this.startActivity(mainIntent);
            }
            else{
                Intent mainIntent = new Intent(HomeActivity.this,DocProfileActivity.class);
                HomeActivity.this.startActivity(mainIntent);
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
