package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class AllDoctorsAdapter extends RecyclerView.Adapter<AllDoctorsAdapter.AllDoctorsViewHolder>{
    private Context mCtx;
    private List<AllDoctorsData> allDoctorsDataList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public AllDoctorsAdapter(Context mCtx, List<AllDoctorsData> allDoctorsDataList) {
        this.mCtx = mCtx;
        this.allDoctorsDataList = allDoctorsDataList;
    }

    @NonNull
    @Override
    public AllDoctorsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.all_doctors_item, null);
        return new AllDoctorsAdapter.AllDoctorsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AllDoctorsViewHolder holder,final int position) {

        final AllDoctorsData allDoctorsData = allDoctorsDataList.get( position);
        holder.docName.setText(allDoctorsData.getName());
        if(!allDoctorsData.getImage().equals("")){
            Glide.with(mCtx).load(allDoctorsData.getImage()).into(holder.docImage);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(mCtx, AdminUsersViewActivity.class);
                mainIntent.putExtra("docId",allDoctorsData.getDocId());
                mCtx.startActivity(mainIntent);
            }
        });

        holder.deleteDoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx, R.style.AlertDialogDanger);
                builder1.setMessage("are you sure you want to delete this doctor");
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                myRef.child("acceptedPosts").orderByChild("uploadedById").equalTo(allDoctorsData.getDocId())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                                                    postSnapshot.getRef().removeValue();
                                                }
                                                myRef.child("Doctors").child(allDoctorsData.getDocId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        Toast.makeText(mCtx,"doc deleted",Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                            }
                        });

                builder1.setNegativeButton(
                        "cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();



            }
        });

    }

    @Override
    public int getItemCount() {
        return allDoctorsDataList.size();
    }

    class AllDoctorsViewHolder extends RecyclerView.ViewHolder{
        TextView docName;
        ImageView docImage;
        ImageView deleteDoc;
        public AllDoctorsViewHolder(View itemView) {
            super(itemView);
            docImage=itemView.findViewById(R.id.doctor_image);
            docName=itemView.findViewById(R.id.doctor_name);
            deleteDoc=itemView.findViewById(R.id.delete_doc);
        }
    }
}
