package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddDoctorActivity extends AppCompatActivity {
    TextView docName;
    TextView docEmail;
    TextView docPhone;
    TextView docMedicalSpecialty;
    TextView docChatPrice;
    Button createDoc;
    RadioButton radioSexButton;
    String type="not admin";
    CheckBox docType;
    private RadioGroup radioSexGroup;
    private FirebaseAuth mAuth;
    private FirebaseAuth mAuth2;
    ProgressBar adddocProgress;
    FirebaseDatabase database=FirebaseDatabase.getInstance();
    DatabaseReference myRef =database.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_doctor);
        docName=findViewById(R.id.doctor_name);
        docEmail=findViewById(R.id.doctor_email);
        docPhone=findViewById(R.id.Phone);
        docMedicalSpecialty=findViewById(R.id.doctor_medical_specialty);
        docChatPrice=findViewById(R.id.chat_price);
        createDoc=findViewById(R.id.add_doc);
        radioSexGroup = findViewById(R.id.radioSex);
        adddocProgress=findViewById(R.id.add_doc_loader);
        docType =findViewById(R.id.makeAdmin);
        mAuth2 = FirebaseAuth.getInstance();



        FirebaseOptions firebaseOptions = new FirebaseOptions.Builder()
                .setDatabaseUrl("[https://console.firebase.google.com/project/solvehunter/database/solvehunter/data]")
                .setApiKey("AIzaSyAKZJjr4nju1s0SBnBqvp263nPb3Q2cK8Q")
                .setApplicationId("solvehunter").build();

        try { FirebaseApp myApp = FirebaseApp.initializeApp(getApplicationContext(), firebaseOptions, "solveHunter");
            mAuth2 = FirebaseAuth.getInstance(myApp);
        } catch (IllegalStateException e){
            mAuth2 = FirebaseAuth.getInstance(FirebaseApp.getInstance("solveHunter"));
        }
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
        createDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                adddocProgress.setVisibility(View.VISIBLE);
                int selectedId = radioSexGroup.getCheckedRadioButtonId();
                radioSexButton =  findViewById(selectedId);
                if (docEmail.getText().toString().isEmpty() || docChatPrice.getText().toString().isEmpty() || docMedicalSpecialty.getText().toString().isEmpty() || docName.getText().toString().isEmpty() || docPhone.getText().toString().isEmpty()) {
                    adddocProgress.setVisibility(View.GONE);
                    Toast.makeText(AddDoctorActivity.this, "Please enter all data", Toast.LENGTH_LONG).show();
                }

                else {
                    if (docType.isChecked())
                        type="admin";
                    mAuth2.createUserWithEmailAndPassword(docEmail.getText().toString(), "123456").addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                myRef.child("Doctors").child(task.getResult().getUser().getUid())
                                        .setValue(new AddDoctorData(docName.getText().toString(), docChatPrice.getText().toString(), docPhone.getText().toString(), type, "", radioSexButton.getText().toString(),docMedicalSpecialty.getText().toString()))
                                        .addOnCompleteListener(AddDoctorActivity.this, new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mAuth2.signOut();
                                                adddocProgress.setVisibility(View.GONE);
                                                docName.setText("");
                                                docChatPrice.setText("");
                                                docPhone.setText("");
                                                docMedicalSpecialty.setText("");
                                                docEmail.setText("");
                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(AddDoctorActivity.this,R.style.AlertDialogDanger);
                                                builder1.setMessage("Doctor account created with password 123456 ");
                                                builder1.setPositiveButton(
                                                        "ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {
                                                                dialog.cancel();
                                                            }
                                                        });
                                                AlertDialog alert11 = builder1.create();
                                                alert11.show();
                                            }
                                        });
                            } else {
                                adddocProgress.setVisibility(View.GONE);
                                if (task.getException() instanceof FirebaseAuthUserCollisionException)
                                    Toast.makeText(getApplicationContext(), "the email address is already a user", Toast.LENGTH_SHORT).show();
                                else if (task.getException().getMessage().equals("The email address is badly formatted.")) {
                                    Toast.makeText(getApplicationContext(), "The email address is badly formatted.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "some thing wrong please try again", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    });
                }
            }
        });

    }
}
