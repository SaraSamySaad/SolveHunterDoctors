package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DocProfileActivity extends AppCompatActivity {

    TextView docName;
    TextView doctorMedicalSpecialty;
    TextView docPhone;
    TextView chatPrice;
    ImageView docImage;
    SharedPreferences pref;
    Button logOut;
    SharedPreferences.Editor editor;
    ProgressBar postsLoader;
    List<PostsData> userPostsList;
    RecyclerView recyclerView;
    TextView noPosts;
    private FirebaseAuth mAuth;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ImageView editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_profile);
        docName=findViewById(R.id.doc_name);
        doctorMedicalSpecialty=findViewById(R.id.doctor_medical_specialty);
        docPhone=findViewById(R.id.doctor_phone);
        chatPrice=findViewById(R.id.chat_price);
        docImage=findViewById(R.id.doc_image);
        logOut=findViewById(R.id.log_out);
        postsLoader=findViewById(R.id.user_posts_loader);
        userPostsList = new ArrayList<>();
        mAuth = FirebaseAuth.getInstance();
        recyclerView=findViewById(R.id.user_posts_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noPosts=findViewById(R.id.no_posts);
        editProfile=findViewById(R.id.edit_profile);
    }
    @Override
    protected void onStart() {
        super.onStart();
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        docName.setText(pref.getString("name",""));
        docPhone.setText(pref.getString("phone",""));
        doctorMedicalSpecialty.setText(pref.getString("medicalSpecialty",""));
        chatPrice.setText("chat price :"+pref.getString("chatPrice",""));
        if(!pref.getString("image","").equals("")){
            Glide.with(DocProfileActivity.this).load(pref.getString("image","")).into(docImage);
        }
    }

    @Override
    protected void onResume() {
        postsLoader.setVisibility(View.VISIBLE);
        myRef.child("acceptedPosts").orderByChild("uploadedById").equalTo(mAuth.getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userPostsList.clear();
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            PostsData postsData = snapshot.getValue(PostsData.class);
                            postsData.setId(snapshot.getKey());
                            userPostsList.add(postsData);
                        }
                        Log.e("notshow",userPostsList.toString());
                        postsLoader.setVisibility(View.GONE);
                        Log.e("arraysize", userPostsList.toString());
                        if(userPostsList.isEmpty()){
                            noPosts.setVisibility(View.VISIBLE);
                        }
                        HomeAdapter adapter=new HomeAdapter(DocProfileActivity.this,userPostsList);
                        recyclerView.setAdapter(adapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
        super.onResume();
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
                editor = pref.edit();
                editor.remove("userID");
                editor.apply();
                Intent i = new Intent(DocProfileActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(DocProfileActivity.this, EditeProfileActivity.class);
//                mainIntent.putExtra("price",String.valueOf(allDoctorsData.getChatPrice()));
//                mainIntent.putExtra("docId",allDoctorsData.getId());
                startActivity(mainIntent);
            }
        });
    }
}
