package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ImageView docImage;
    TextView name;
    TextView phone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);

        docImage=findViewById(R.id.doc_image);
        name=findViewById(R.id.doc_name);
        phone=findViewById(R.id.doctor_phone);
    }

    @Override
    protected void onStart() {
        super.onStart();
        myRef.child("Users").child(getIntent().getExtras().getString("userId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.e("hhhh",getIntent().getExtras().getString("userId"));
                if (!dataSnapshot.child("image").getValue().equals(""))
                    Glide.with(UserDataActivity.this).load(dataSnapshot.child("image").getValue()).into(docImage);
                name.setText("name: "+dataSnapshot.child("name").getValue().toString());
                phone.setText("phone: "+dataSnapshot.child("phone").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
