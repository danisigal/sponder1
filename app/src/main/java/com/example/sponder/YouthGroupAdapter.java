package com.example.sponder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.sponder.R;
import com.example.sponder.YouthGroupDB;

import java.util.List;

public class YouthGroupAdapter extends RecyclerView.Adapter<YouthGroupAdapter.ViewHolder> {

    private Context context;
    List<YouthGroupDB.YouthGroup> youthGroups;

    public YouthGroupAdapter(Context context, List<YouthGroupDB.YouthGroup> youthGroups) {
        this.context = context;
        this.youthGroups = youthGroups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_youthgroup, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YouthGroupDB.YouthGroup youthGroup = youthGroups.get(position);
        holder.name.setText(youthGroup.getYouthGroupName());
        holder.amountRequest.setText("Amount Requested: " + youthGroup.getAmountRequest());
        holder.email.setText("Email: " + youthGroup.getEmail());
        holder.place.setText("Place: " + youthGroup.getPlace());
    }

    @Override
    public int getItemCount() {
        return youthGroups.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amountRequest, email, place;
        ImageView image;
        private List<YouthGroupDB.YouthGroup> youthGroups;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            amountRequest = itemView.findViewById(R.id.item_amount);
            email = itemView.findViewById(R.id.item_email);
            place = itemView.findViewById(R.id.item_place);
        }
        public void setYouthGroups(List<YouthGroupDB.YouthGroup> youthGroups) {
            this.youthGroups = youthGroups;
        }
    }
    public void setYouthGroups(List<YouthGroupDB.YouthGroup> youthGroups) {
        this.youthGroups = youthGroups;
    }
}