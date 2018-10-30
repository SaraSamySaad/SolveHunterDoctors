package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Created by C.M on 25/10/2018.
 */

public class PendingPostsAdapter extends RecyclerView.Adapter<PendingPostsAdapter.PendingPostsViewHolder> {
    private Context mCtx;
    private List<PendingPostsData> PendingPostsList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();

    public PendingPostsAdapter(Context mCtx, List<PendingPostsData> pendingPostsList) {
        this.mCtx = mCtx;
        PendingPostsList = pendingPostsList;
    }

    @NonNull
    @Override
    public PendingPostsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.pending_posts_item, null);
        return new PendingPostsAdapter.PendingPostsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PendingPostsViewHolder holder, final int position) {
        final PendingPostsData pendingPostsData = PendingPostsList.get( position);
        holder.postBody.setText(pendingPostsData.getBody());
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("Pending").child(pendingPostsData.getPostId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(mCtx,"post deleted",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        holder.accepte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myRef.child("acceptedPosts").push()
                        .setValue(new PostsData(pendingPostsData.getBody().toString(),0,0,pendingPostsData.getUploadedById().toString()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        myRef.child("Pending").child(pendingPostsData.getPostId()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(mCtx,"post accepted",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return PendingPostsList.size();
    }

    class PendingPostsViewHolder extends RecyclerView.ViewHolder{
        TextView postBody;
        TextView accepte;
        TextView reject;
        public PendingPostsViewHolder(View itemView) {
            super(itemView);
            postBody=itemView.findViewById(R.id.body);
            accepte=itemView.findViewById(R.id.accept);
            reject=itemView.findViewById(R.id.reject);
        }
    }
}
