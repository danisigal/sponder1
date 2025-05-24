package com.example.sponder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // משתנה להפניה למסד הנתונים של Firebase
    private DatabaseReference databaseReference;
    // כפתורים להרשמת ספונסר וקבוצת נוער
    private Button btnSponsor, btnYouthGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // קישור הכפתורים מה-XML
        btnSponsor = findViewById(R.id.btnSponsor);
        btnYouthGroup = findViewById(R.id.btnYouthGroup);

        // מאזין לכפתור הרשמת ספונסר - פותח את מסך הרשמת ספונסר
        btnSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SponsorRegistrationActivity.class));
            }
        });

        // מאזין לכפתור הרשמת קבוצת נוער - פותח את מסך הרשמת קבוצת נוער
        btnYouthGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, YouthGroupRegistrationActivity.class));
            }
        });

        // קישור והגדרת ה-Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    // יצירת תפריט האופציות (menu)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("XXXX","+onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // הפניה לשורש מסד הנתונים של Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // הכנסת 15 ספונסרים ו-15 קבוצות נוער לדאטהבייס
        insertSponsors();
        insertYouthGroups();
        return true;
    }

    // טיפול בלחיצה על פריט בתפריט
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_home) {
            Toast.makeText(this, "דף הבית", Toast.LENGTH_SHORT).show();
        }
        if (item.getItemId() == R.id.action_search_sponsors) {
            startActivity(new Intent(this, SearchSponsorsActivity.class));
        }
        if (item.getItemId() == R.id.action_search_youthGroups) {
            startActivity(new Intent(this, SearchYouthGroupActivity.class));
        }

        // דוגמה לכתיבת הודעה למסד הנתונים
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");
        myRef.setValue("Hello, World!");

        return super.onOptionsItemSelected(item);
    }

    // פונקציה שמכניסה 15 ספונסרים אקראיים לדאטהבייס
    private void insertSponsors() {
        Log.e("XXXX","+insertSponsors");

        for (int i = 1; i <= 15; i++) {
            String sponsorName = "Sponsor " + i;
            double amount = new Random().nextInt(10000) + 1000; // סכום אקראי בין 1000 ל-11000
            String email = "sponsor" + i + "@example.com";
            String[] places = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
            String place = places[new Random().nextInt(places.length)];
            // יצירת אובייקט ספונסר
            SponsorDB.Sponsor sponsor = new SponsorDB.Sponsor(sponsorName, amount, email, place);
            // הוספת הספונסר לדאטהבייס
            databaseReference.child("sponsors").child(sponsorName).setValue(sponsor);
        }
    }

    // פונקציה שמכניסה 15 קבוצות נוער אקראיות לדאטהבייס
    private void insertYouthGroups() {
        Log.e("XXXX","+insertYouthGroups");

        String[] places = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        for (int i = 1; i <= 15; i++) {
            String youthGroupName = "Youth Group " + i;
            String email = "youthgroup" + i + "@example.com";
            double amountRequest = new Random().nextInt(5000) + 500; // סכום אקראי בין 500 ל-5500
            String place = places[new Random().nextInt(places.length)];
            // יצירת אובייקט קבוצת נוער
            YouthGroupDB.YouthGroup youthGroup = new YouthGroupDB.YouthGroup(youthGroupName, email, amountRequest, place);
            // הוספת קבוצת הנוער לדאטהבייס
            databaseReference.child("youthGroups").child(youthGroupName).setValue(youthGroup);
        }
    }

}