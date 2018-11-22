package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    TextView pendingPosts;
    TextView addDoctor;
    TextView allDocs;
    TextView allUsers;
    TextView docName;
    TextView doctorMedicalSpecialty;
    TextView docPhone;
    TextView chatPrice;
    ImageView docImage;
    SharedPreferences pref;
    Button logOut;
    SharedPreferences.Editor editor;
    ImageView editProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        pendingPosts=findViewById(R.id.view_pending_posts);
        addDoctor=findViewById(R.id.add_doctor);
        allDocs=findViewById(R.id.all_doctors);
        allUsers=findViewById(R.id.all_users);
        docName=findViewById(R.id.doc_name);
        doctorMedicalSpecialty=findViewById(R.id.doctor_medical_specialty);
        docPhone=findViewById(R.id.doctor_phone);
        chatPrice=findViewById(R.id.chat_price);
        docImage=findViewById(R.id.doc_image);
        logOut=findViewById(R.id.log_out);
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
            Glide.with(ProfileActivity.this).load(pref.getString("image","")).into(docImage);
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        pendingPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProfileActivity.this, PendingPostsActivity.class);
                startActivity(mainIntent);
            }
        });
        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProfileActivity.this, AddDoctorActivity.class);
                startActivity(mainIntent);
            }
        });
        allDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProfileActivity.this, AllDoctorsActivity.class);
                startActivity(mainIntent);
            }
        });
        allUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProfileActivity.this, AllUsersActivity.class);
                startActivity(mainIntent);
            }
        });
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
                editor = pref.edit();
                editor.remove("userID");
                editor.apply();
                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(ProfileActivity.this, EditeProfileActivity.class);
//                mainIntent.putExtra("price",String.valueOf(allDoctorsData.getChatPrice()));
//                mainIntent.putExtra("docId",allDoctorsData.getId());
                startActivity(mainIntent);
            }
        });

    }
}
