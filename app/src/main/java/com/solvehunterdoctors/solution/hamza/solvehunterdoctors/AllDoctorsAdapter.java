package com.solvehunterdoctors.solution.hamza.solvehunterdoctors;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class AllDoctorsAdapter extends RecyclerView.Adapter<AllDoctorsAdapter.AllDoctorsViewHolder>{
    private Context mCtx;
    private List<AllDoctorsData> allDoctorsDataList;

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
    public void onBindViewHolder(@NonNull AllDoctorsViewHolder holder, int position) {

        final AllDoctorsData allDoctorsData = allDoctorsDataList.get( position);
        holder.docName.setText(allDoctorsData.getName());
        Glide.with(mCtx).load(allDoctorsData.getImage()).into(holder.docImage);
    }

    @Override
    public int getItemCount() {
        return allDoctorsDataList.size();
    }

    class AllDoctorsViewHolder extends RecyclerView.ViewHolder{
        TextView docName;
        ImageView docImage;
        public AllDoctorsViewHolder(View itemView) {
            super(itemView);
            docImage=itemView.findViewById(R.id.doctor_image);
            docName=itemView.findViewById(R.id.doctor_name);
        }
    }
}
