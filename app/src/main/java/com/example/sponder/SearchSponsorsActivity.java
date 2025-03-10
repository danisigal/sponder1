package com.example.sponder;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sponsors);

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, this);

        adapter = new SponsorAdapter(this, new ArrayList<>());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        fetchSponsors();
    }

    private void fetchSponsors() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sponsors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<SponsorDB.Sponsor> sponsors = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    sponsors.add(data.getValue(SponsorDB.Sponsor.class));
                }
                adapter.notifyDataSetChanged();
                adapter.sponsors.addAll(sponsors);
            }

            @Override
            public void onCancelled(DatabaseError error) { }
        });
    }

    @Override
    public void onCardSwiped(Direction direction) { }
}