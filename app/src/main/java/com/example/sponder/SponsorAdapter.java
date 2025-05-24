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

// מתאם (Adapter) עבור רשימת הספונסרים - אחראי להציג כל ספונסר ברשימה
public class SponsorAdapter extends RecyclerView.Adapter<SponsorAdapter.ViewHolder> {

    private Context context;
    List<SponsorDB.Sponsor> sponsors; // רשימת הספונסרים שמוצגת

    // בנאי שמקבל את ההקשר (context) ואת הרשימה של הספונסרים
    public SponsorAdapter(Context context, List<SponsorDB.Sponsor> sponsors) {
        this.context = context;
        this.sponsors = sponsors;
    }

    // יצירת ViewHolder חדש לכל שורה ברשימה (לפי תבנית ה-XML)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ניפוח (inflate) של תצוגת הפריט הבודד (item_sponsor)
        View view = LayoutInflater.from(context).inflate(R.layout.item_sponsor, parent, false);
        return new ViewHolder(view);
    }

    // קישור נתונים מהספונסר לתצוגה (bind)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SponsorDB.Sponsor sponsor = sponsors.get(position);
        holder.name.setText(sponsor.getSponsorName());
        holder.amount.setText("Amount Sponsored: " + sponsor.getAmount());
        holder.email.setText("Email: " + sponsor.getEmail());
        holder.place.setText("Place: " + sponsor.getPlace());
        // אם יש לך תמונה לספונסר, אפשר להשתמש ב-Glide כאן לטעון אותה ל-holder.image
        // Glide.with(context).load(sponsor.getImageUrl()).into(holder.image);
    }

    // מחזיר את מספר הפריטים ברשימה
    @Override
    public int getItemCount() {
        return sponsors.size();
    }

    // מחלקה פנימית שמייצגת את התצוגה של כל שורה (ViewHolder)
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amount, email, place;
        ImageView image;
        private List<SponsorDB.Sponsor> sponsors;

        ViewHolder(View itemView) {
            super(itemView);
            // קישור רכיבי התצוגה מה-XML
            name = itemView.findViewById(R.id.item_name);
            amount = itemView.findViewById(R.id.item_amount);
            email = itemView.findViewById(R.id.item_email);
            place = itemView.findViewById(R.id.item_place);
            // image = itemView.findViewById(R.id.item_image); // אם יש לך תמונה
        }

        // פונקציה לעדכון הרשימה (לא חובה כאן, אפשר למחוק)
        public void setSponsors(List<SponsorDB.Sponsor> sponsors) {
            this.sponsors = sponsors;
        }
    }

    // פונקציה שמאפשרת לעדכן את רשימת הספונסרים מבחוץ (למשל אחרי שליפה מה-Database)
    public void setSponsors(List<SponsorDB.Sponsor> sponsors) {
        this.sponsors = sponsors;
    }
}