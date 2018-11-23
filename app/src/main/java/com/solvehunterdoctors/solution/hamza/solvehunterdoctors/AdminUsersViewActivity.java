package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminUsersViewActivity extends AppCompatActivity {
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    ImageView docImage;
    TextView name;
    TextView phone;
    TextView price;
    TextView doctorMedicalSpecialty;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users_view);
        docImage=findViewById(R.id.doc_image);
        doctorMedicalSpecialty=findViewById(R.id.doctor_medical_specialty);
        name=findViewById(R.id.doc_name);
        phone=findViewById(R.id.doctor_phone);
        price=findViewById(R.id.chat_price);
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
    protected void onStart() {
        super.onStart();
        myRef.child("Doctors").child(getIntent().getExtras().getString("docId")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (!dataSnapshot.child("image").getValue().equals(""))
                    Glide.with(AdminUsersViewActivity.this).load(dataSnapshot.child("image").getValue()).into(docImage);
                name.setText("name: "+dataSnapshot.child("name").getValue().toString());
                doctorMedicalSpecialty.setText("Medical Specialty: "+dataSnapshot.child("medicalSpecialty").getValue().toString());
                phone.setText("phone: "+dataSnapshot.child("phone").getValue().toString());
                price.setText("chat price: "+dataSnapshot.child("chatPrice").getValue().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
