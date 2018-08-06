package com.paddypal.azir.paddypal.getGuidance;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.paddypal.azir.paddypal.R;

import java.util.List;

public class GuidanceAdapter extends RecyclerView.Adapter<GuidanceAdapter.MyViewHolder> {
    private List<Guidance> guidanceList;
    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView step,desc,date;

        public MyViewHolder(View view){
            super(view);
            step=view.findViewById(R.id.txtStep);
            desc=view.findViewById(R.id.txtDescription);
            date=view.findViewById(R.id.txtDate);
        }
    }

    public GuidanceAdapter(List<Guidance> guidanceList){
        this.guidanceList=guidanceList;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position){
        Guidance guidance=guidanceList.get(position);
        holder.step.setText("Step: "+Integer.toString(guidance.getStep()));
        holder.date.setText(guidance.getDate());
        holder.desc.setText(guidance.getDetailedDescription());
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.guidance_view,parent,false);
        return new MyViewHolder(itemView);
    }

    @Override
    public int getItemCount() {
        return guidanceList.size();
    }
}
