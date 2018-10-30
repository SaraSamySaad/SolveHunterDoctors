package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class RequestChatsAdapter  extends RecyclerView.Adapter<RequestChatsAdapter.RequestChatsAdapterViewHolder>{
    private Context mCtx;
    private List<RequestsChatsData> requestsChatsDataList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();
    private FirebaseAuth mAuth;
    public RequestChatsAdapter(Context mCtx, List<RequestsChatsData> requestsChatsDataList) {
        this.mCtx = mCtx;
        this.requestsChatsDataList = requestsChatsDataList;
    }

    @NonNull
    @Override
    public RequestChatsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.request_chat_item, null);
        return new RequestChatsAdapter.RequestChatsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final RequestChatsAdapterViewHolder holder, int position) {
        final RequestsChatsData requestsChatsData = requestsChatsDataList.get( position);
        holder.body.setText(requestsChatsData.getProblemHint());
        mAuth = FirebaseAuth.getInstance();
        myRef.child("Users").child(requestsChatsData.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                holder.userName.setText(dataSnapshot.child("name").getValue().toString());
                Glide.with(mCtx).load(dataSnapshot.child("image").getValue()).into(holder.userImage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Dialoges").child(mAuth.getCurrentUser().getUid()+requestsChatsData.getUserId()).push()
                        .setValue(new PushMessage("tell me more about ur problem","false",mAuth.getCurrentUser().getUid()))
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myRef.child("Doctors").child(mAuth.getCurrentUser().getUid()).child("dialoges")
                                .child(mAuth.getCurrentUser().getUid()+requestsChatsData.getUserId())
                                .setValue(new LastMessage("tell me more about ur problem",requestsChatsData.getUserId()))
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        myRef.child("Users").child(requestsChatsData.getUserId()).child("dialoges")
                                                .child(mAuth.getCurrentUser().getUid()+requestsChatsData.getUserId())
                                                .setValue(new LastMessage("tell me more about ur problem",mAuth.getCurrentUser().getUid()))
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        myRef.child("ChatRequests").child(mAuth.getCurrentUser().getUid()).child(requestsChatsData.getId())
                                                                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                AlertDialog.Builder builder1 = new AlertDialog.Builder(mCtx);
                                                                builder1.setMessage("chat accepted and find it in chat tab");
                                                                builder1.setPositiveButton(
                                                                        "ok",
                                                                        new DialogInterface.OnClickListener() {
                                                                            public void onClick(DialogInterface dialog, int id) {
                                                                                dialog.cancel();
                                                                            }
                                                                        });
                                                                AlertDialog alert11 = builder1.create();
                                                                alert11.show();
                                                                alert11.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(mCtx.getColor( R.color.colorPrimaryDark));

                                                            }
                                                        });

                                                    }
                                                });
                                    }
                                });
                    }
                });
            }
        });

    }

    @Override
    public int getItemCount() {
        return requestsChatsDataList.size();
    }

    class RequestChatsAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView body;
        TextView userName;
        ImageView userImage;
        TextView accept;
        TextView reject;
        public RequestChatsAdapterViewHolder(View itemView) {
            super(itemView);
            body=itemView.findViewById(R.id.last_message);
            userName=itemView.findViewById(R.id.sender_name);
            userImage=itemView.findViewById(R.id.sender_image);
            accept=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
        }

    }
}
