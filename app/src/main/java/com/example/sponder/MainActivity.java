package com.example.sponder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.sponder.ChatActivity;
import com.example.sponder.CreateCampaignActivity;
import com.example.sponder.ManageCampaignsActivity;
import com.example.sponder.ProfileActivity;
import com.example.sponder.R;
import com.example.sponder.SearchSponsorsActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if (item.getItemId() == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }
        if (item.getItemId() == R.id.action_create_campaign) {
            startActivity(new Intent(this, CreateCampaignActivity.class));
        }
        if (item.getItemId() == R.id.action_manage_campaigns) {
            startActivity(new Intent(this, ManageCampaignsActivity.class));
        }
        if (item.getItemId() == R.id.action_chat) {
            startActivity(new Intent(this, ChatActivity.class));
        }
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        myRef.setValue("Hello, World!");
            return super.onOptionsItemSelected(item);

    }
}
