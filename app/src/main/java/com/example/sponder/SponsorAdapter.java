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
import com.example.sponder.SponsorDB;

import java.util.List;

public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.ViewHolder> {

    private Context context;
    List<SponsorDB.Sponsor> sponsors;

    public SponsorAdapter(Context context, List<SponsorDB.Sponsor> sponsors) {
        this.context = context;
        this.sponsors = sponsors;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_sponsor, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SponsorDB.Sponsor sponsor = sponsors.get(position);
        holder.name.setText(sponsor.getSponsorName());
        holder.amount.setText("Amount Sponsored: " + sponsor.getAmount());
        holder.email.setText("Email: " + sponsor.getEmail());
        holder.place.setText("Place: " + sponsor.getPlace());
    }

    @Override
    public int getItemCount() {
        return sponsors.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount, email, place;
        ImageView image;
        private List<SponsorDB.Sponsor> sponsors;

        ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.item_name);
            amount = itemView.findViewById(R.id.item_amount);
            email = itemView.findViewById(R.id.item_email);
            place = itemView.findViewById(R.id.item_place);
        }
        public void setSponsors(List<SponsorDB.Sponsor> sponsors) {
            this.sponsors = sponsors;
        }
    }
}