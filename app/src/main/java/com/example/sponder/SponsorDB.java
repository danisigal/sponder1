package com.example.sponder;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// מחלקה שמייצגת את מסך/אקטיביטי ניהול הספונסרים (SponsorDB)
public class SponsorDB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // הפעלת מצב Edge-to-Edge (עיצוב מודרני)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sponser_db);

        // הגדרת מרווחים (Padding) אוטומטית לפי מערכת ההפעלה
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // מחלקה פנימית שמייצגת אובייקט ספונסר (Sponsor)
    public static class Sponsor {
        private String sponsorName; // שם הספונסר
        private double amount;      // סכום התרומה
        private String email;       // אימייל של הספונסר
        private String place;       // עיר/מקום מגורים

        // בנאי ריק - חובה בשביל Firebase (DataSnapshot.getValue)
        public Sponsor() {}

        // בנאי עם פרמטרים ליצירת ספונסר חדש
        public Sponsor(String sponsorName, double amount, String email, String place) {
            this.sponsorName = sponsorName;
            this.amount = amount;
            this.email = email;
            this.place = place;
        }

        // Getters and setters (מאפשרים גישה ושינוי לערכים של הספונסר)
        public String getSponsorName() {
            return sponsorName;
        }

        public void setSponsorName(String sponsorName) {
            this.sponsorName = sponsorName;
        }

        public double getAmount() {
            return amount;
        }

        // שים לב: כאן בפרמטר זה int, עדיף לשנות ל-double כדי להתאים לשדה
        public void setAmount(int amount) {
            this.amount = amount;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }
    }
}