package com.example.sponder;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

// אקטיביטי שמייצג מסך/ניהול קבוצות נוער
public class YouthGroupDB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // הפעלת מצב Edge-to-Edge (עיצוב מודרני)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_youth_group_db);

        // הגדרת מרווחים (Padding) אוטומטית לפי מערכת ההפעלה
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // מחלקה פנימית שמייצגת אובייקט קבוצת נוער (YouthGroup)
    public static class YouthGroup {
        private String youthGroupName; // שם קבוצת הנוער
        private String email;          // אימייל של הקבוצה
        private double amountRequest;  // סכום הבקשה
        private String place;          // עיר/מקום

        // בנאי ריק - חובה בשביל Firebase (DataSnapshot.getValue)
        public YouthGroup() {}

        // בנאי עם פרמטרים ליצירת קבוצה חדשה
        public YouthGroup(String youthGroupName, String email, double amountRequest, String place) {
            this.youthGroupName = youthGroupName;
            this.email = email;
            this.amountRequest = amountRequest;
            this.place = place;
        }

        // Getters and setters (מאפשרים גישה ושינוי לערכים של הקבוצה)
        public String getYouthGroupName() {
            return youthGroupName;
        }

        public void setYouthGroupName(String youthGroupName) {
            this.youthGroupName = youthGroupName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public double getAmountRequest() {
            return amountRequest;
        }

        public void setAmountRequest(double amountRequest) {
            this.amountRequest = amountRequest;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }
    }
}