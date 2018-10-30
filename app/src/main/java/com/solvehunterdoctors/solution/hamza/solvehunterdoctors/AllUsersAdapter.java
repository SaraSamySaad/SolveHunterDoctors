package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AllUsersAdapter extends RecyclerView.Adapter<AllUsersAdapter.AllUsersAdapterViewHolder> {
    private Context mCtx;
    private List<AllUsersData> allUsersDataList;

    public AllUsersAdapter(Context mCtx, List<AllUsersData> allUsersDataList) {
        this.mCtx = mCtx;
        this.allUsersDataList = allUsersDataList;
    }

    @NonNull
    @Override
    public AllUsersAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.all_users_item, null);
        return new AllUsersAdapter.AllUsersAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllUsersAdapterViewHolder holder, int position) {
        final AllUsersData allUsersData = allUsersDataList.get( position);
        holder.docName.setText(allUsersData.getName());
        Glide.with(mCtx).load(allUsersData.getImage()).into(holder.docImage);
    }

    @Override
    public int getItemCount() {
        return allUsersDataList.size();
    }

    class AllUsersAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView docName;
        ImageView docImage;
        public AllUsersAdapterViewHolder(View itemView) {
            super(itemView);
            docImage=itemView.findViewById(R.id.user_image);
            docName=itemView.findViewById(R.id.user_name);
        }
    }
}
