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


    private DatabaseReference databaseReference;
    private Button btnSponsor, btnYouthGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnSponsor = findViewById(R.id.btnSponsor);
        btnYouthGroup = findViewById(R.id.btnYouthGroup);

        btnSponsor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SponsorRegistrationActivity.class));
            }
        });

        btnYouthGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, YouthGroupRegistrationActivity.class));
            }
        });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.e("XXXX","+onCreateOptionsMenu");
        getMenuInflater().inflate(R.menu.menu_main, menu);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        insertSponsors();
        insertYouthGroups();
        return true;
    }

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

        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
            return super.onOptionsItemSelected(item);
    }

    private void insertSponsors() {
        Log.e("XXXX","+insertSponsors");

        for (int i = 1; i <= 15; i++) {
            String sponsorName = "Sponsor " + i;
            double amount = new Random().nextInt(10000) + 1000; // Random amount between 1000 and 11000
            String email = "sponsor" + i + "@example.com";
            String[] places = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
            String place = places[new Random().nextInt(places.length)];
            SponsorDB.Sponsor sponsor = new SponsorDB.Sponsor(sponsorName, amount, email, place);
            databaseReference.child("sponsors").child(sponsorName).setValue(sponsor);
        }
    }

    private void insertYouthGroups() {
        Log.e("XXXX","+insertYouthGroups");

        String[] places = {"New York", "Los Angeles", "Chicago", "Houston", "Phoenix"};
        for (int i = 1; i <= 15; i++) {
            String youthGroupName = "Youth Group " + i;
            String email = "youthgroup" + i + "@example.com";
            double amountRequest = new Random().nextInt(5000) + 500; // Random amount between 500 and 5500
            String place = places[new Random().nextInt(places.length)];

            YouthGroupDB.YouthGroup youthGroup = new YouthGroupDB.YouthGroup(youthGroupName, email, amountRequest, place);
            databaseReference.child("youthGroups").child(youthGroupName).setValue(youthGroup);
        }
    }


    }
