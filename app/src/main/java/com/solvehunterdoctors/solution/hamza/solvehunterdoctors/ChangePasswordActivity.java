package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePasswordActivity extends AppCompatActivity {
    EditText oldPass;
    EditText newPass;
    EditText newRePass;
    Button update;
    private FirebaseAuth mAuth;
    ProgressBar loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        oldPass=findViewById(R.id.old_pass);
        newPass=findViewById(R.id.new_pass);
        newRePass=findViewById(R.id.new_again_pass);
        update=findViewById(R.id.update);
        loader=findViewById(R.id.loader);
        mAuth=FirebaseAuth.getInstance();


    }

    @Override
    protected void onResume() {
        super.onResume();
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(newPass.getText().toString().equals(newRePass.getText().toString())){
                    loader.setVisibility(View.VISIBLE);
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(mAuth.getCurrentUser().getEmail(), oldPass.getText().toString());

                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        user.updatePassword(newPass.getText().toString())
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    loader.setVisibility(View.GONE);
                                                    Toast.makeText(ChangePasswordActivity.this,"password updated",Toast.LENGTH_LONG)
                                                            .show();
                                                } else {
                                                    loader.setVisibility(View.GONE);
                                                    Toast.makeText(ChangePasswordActivity.this,"some thing wrong try again",Toast.LENGTH_LONG)
                                                            .show();
                                                }
                                            }
                                        });
                                    } else {
                                        loader.setVisibility(View.GONE);
                                        Toast.makeText(ChangePasswordActivity.this,"Error auth failed",Toast.LENGTH_LONG)
                                                .show();
                                    }
                                }
                            });
                }

                else
                {
                    Toast.makeText(ChangePasswordActivity.this,"password not match",Toast.LENGTH_LONG)
                            .show();
                }

            }
        });
    }
}
