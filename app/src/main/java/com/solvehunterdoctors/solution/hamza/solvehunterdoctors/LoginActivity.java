package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    Button signIn;
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;
    ProgressBar signInProgress;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        signIn=findViewById(R.id.sign_in);
        email=findViewById(R.id.sign_in_email);
        password=findViewById(R.id.sign_in_password);
        pref = getApplicationContext().getSharedPreferences("myPref", MODE_PRIVATE);
        editor = pref.edit();
        mAuth = FirebaseAuth.getInstance();
        signInProgress=findViewById(R.id.sign_in_loader);
    }

    @Override
    protected void onResume() {
        super.onResume();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInProgress.setVisibility(View.VISIBLE);
                if (email.getText().toString().isEmpty()||password.getText().toString().isEmpty()){
                    Toast.makeText(LoginActivity.this, "enter your mail & paseword", Toast.LENGTH_SHORT).show();
                }
                else{
                    mAuth.signInWithEmailAndPassword(email.getText().toString(),password.getText().toString())
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        myRef.child("Doctors").child(mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                editor.putString("userID",mAuth.getCurrentUser().getUid());
                                                editor.putString("name",dataSnapshot.child("name").getValue().toString());
                                                editor.putString("type",dataSnapshot.child("type").getValue().toString());
                                                editor.putString("medicalSpecialty",dataSnapshot.child("medicalSpecialty").getValue().toString());
                                                editor.putString("phone",dataSnapshot.child("phone").getValue().toString());
                                                editor.putString("chatPrice",dataSnapshot.child("chatPrice").getValue().toString());
                                                editor.putString("image",dataSnapshot.child("image").getValue().toString());
                                                editor.commit();
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        signInProgress.setVisibility(View.GONE);
                                        Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        signInProgress.setVisibility(View.GONE);
                                        Toast.makeText(LoginActivity.this, "wrong data cheek your mail & paseword", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }



            }
        });
    }
}
