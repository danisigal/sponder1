package com.example.sponder;

import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class YouthGroupDB extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_youth_group_db);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    // Moved outside of onCreate and renamed
    public static class YouthGroup {
        private String youthGroupName;
        private String email;
        private double amountRequest;
        private String place;

        public YouthGroup() {
            // Default constructor required for calls to DataSnapshot.getValue(YouthGroup.class)
        }

        public YouthGroup(String youthGroupName, String email, double amountRequest, String place) {
            this.youthGroupName = youthGroupName;
            this.email = email;
            this.amountRequest = amountRequest;
            this.place = place;
        }

        // Getters and setters
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