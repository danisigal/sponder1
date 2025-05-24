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

// מתאם (Adapter) עבור רשימת קבוצות הנוער - אחראי להציג כל קבוצה ברשימה
public class YouthGroupAdapter extends RecyclerView.Adapter<YouthGroupAdapter.ViewHolder> {

    private Context context;
    List<YouthGroupDB.YouthGroup> youthGroups; // רשימת קבוצות הנוער שמוצגת

    // בנאי שמקבל את ההקשר (context) ואת הרשימה של קבוצות הנוער
    public YouthGroupAdapter(Context context, List<YouthGroupDB.YouthGroup> youthGroups) {
        this.context = context;
        this.youthGroups = youthGroups;
    }

    // יצירת ViewHolder חדש לכל שורה ברשימה (לפי תבנית ה-XML)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // ניפוח (inflate) של תצוגת הפריט הבודד (item_youthgroup)
        View view = LayoutInflater.from(context).inflate(R.layout.item_youthgroup, parent, false);
        return new ViewHolder(view);
    }

    // קישור נתונים מהקבוצה לתצוגה (bind)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        YouthGroupDB.YouthGroup youthGroup = youthGroups.get(position);
        holder.name.setText(youthGroup.getYouthGroupName());
        holder.amountRequest.setText("Amount Requested: " + youthGroup.getAmountRequest());
        holder.email.setText("Email: " + youthGroup.getEmail());
        holder.place.setText("Place: " + youthGroup.getPlace());
        // אם תרצה להציג תמונה, תוכל להוסיף כאן שימוש ב-Glide
        // Glide.with(context).load(youthGroup.getImageUrl()).into(holder.image);
    }

    // מחזיר את מספר הפריטים ברשימה
    @Override
    public int getItemCount() {
        return youthGroups.size();
    }

    // מחלקה פנימית שמייצגת את התצוגה של כל שורה (ViewHolder)
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, amountRequest, email, place;
        ImageView image;
        private List<YouthGroupDB.YouthGroup> youthGroups;

        ViewHolder(View itemView) {
            super(itemView);
            // קישור רכיבי התצוגה מה-XML
            name = itemView.findViewById(R.id.item_name);
            amountRequest = itemView.findViewById(R.id.item_amount);
            email = itemView.findViewById(R.id.item_email);
            place = itemView.findViewById(R.id.item_place);
            // image = itemView.findViewById(R.id.item_image); // אם תוסיף תמונה
        }

        // פונקציה לעדכון הרשימה (לא חובה כאן, אפשר למחוק)
        public void setYouthGroups(List<YouthGroupDB.YouthGroup> youthGroups) {
            this.youthGroups = youthGroups;
        }
    }

    // פונקציה שמאפשרת לעדכן את רשימת קבוצות הנוער מבחוץ (למשל אחרי שליפה מה-Database)
    public void setYouthGroups(List<YouthGroupDB.YouthGroup> youthGroups) {
        this.youthGroups = youthGroups;
    }
}