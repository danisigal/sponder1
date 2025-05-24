package com.example.sponder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.sponder.R;
import com.example.sponder.SponsorAdapter;
import com.example.sponder.SponsorDB;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.List;

public  class SearchSponsorsActivity extends AppCompatActivity implements CardStackListener {

    private CardStackLayoutManager manager;
    private SponsorAdapter adapter;
    private CardStackView cardStackView;
    private List<SponsorDB.Sponsor> sponsors;
    private DatabaseReference databaseReference;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_search_sponsors);

            Button backToHomeButton = findViewById(R.id.button_back_to_home);
            backToHomeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchSponsorsActivity.this, MainActivity.class);
                    // Optional: clear the back stack so user can't return to this activity
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            });
            setContentView(R.layout.activity_search_sponsors);

            CardStackView cardStackView = findViewById(R.id.card_stack_view);
            manager = new CardStackLayoutManager(this, this);

            adapter = new SponsorAdapter(this, new ArrayList<>());
            cardStackView.setLayoutManager(manager);
            cardStackView.setAdapter(adapter);

            fetchSponsors();
        }



    @Override
    public void onCardDisappeared(View view, int position) {

    }

    public void onCardAppeared(View view, int position) {

    }

    public void onCardCanceled() {

    }

    public void onCardRewound() {

    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }



    private void fetchSponsors() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sponsors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                sponsors = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    SponsorDB.Sponsor sponsor = data.getValue(SponsorDB.Sponsor.class);
                    if (sponsor != null) {
                        sponsors.add(sponsor);
                    }
                }
                adapter.setSponsors(sponsors);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error
            }
        });
    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (direction == Direction.Right) {
            int position = manager.getTopPosition() - 1;
            if (position >= 0 && position < sponsors.size()) {
                SponsorDB.Sponsor sponsor = sponsors.get(position);
                showSponsorInfoDialog(sponsor);
            }
        }
    }
    private void showSponsorInfoDialog(SponsorDB.Sponsor sponsor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sponsor Information")
                .setMessage("Name: " + sponsor.getSponsorName() + "\n" +
                        "Amount: " + sponsor.getAmount() + "\n" +
                        "Email: " + sponsor.getEmail() + "\n" +
                        "Place: " + sponsor.getPlace())
                .setPositiveButton("OK", (dialog, id) -> {
                    // Dialog closes when OK is clicked
                });
        builder.create().show();
    }
}