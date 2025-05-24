package com.example.sponder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

public  class SearchYouthGroupActivity extends AppCompatActivity implements CardStackListener {

    private CardStackLayoutManager manager;
    private YouthGroupAdapter adapter;
    private CardStackView cardStackView;
    private List<YouthGroupDB.YouthGroup> youthGroups;
    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_youth_group);

        Button backToHomeButton = findViewById(R.id.button_back_to_home);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchYouthGroupActivity.this, MainActivity.class);
                // Optional: clear the back stack so user can't return to this activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        setContentView(R.layout.activity_search_youth_group);

        CardStackView cardStackView = findViewById(R.id.card_stack_view);
        manager = new CardStackLayoutManager(this, this);

        adapter = new YouthGroupAdapter(this, new ArrayList<>());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        fetchYouthGroups();
    }
    private void fetchYouthGroups() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("youthGroups");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                youthGroups = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    YouthGroupDB.YouthGroup youthGroup = data.getValue(YouthGroupDB.YouthGroup.class);
                    if (youthGroup != null) {
                        youthGroups.add(youthGroup);
                    }
                }
                adapter.setYouthGroups(youthGroups);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Handle error (optional)
            }
        });
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
                youthGroups = new ArrayList<>();
                for (DataSnapshot data : snapshot.getChildren()) {
                    YouthGroupDB.YouthGroup youthGroup = data.getValue(YouthGroupDB.YouthGroup.class);
                    if (youthGroups != null) {
                        youthGroups.add((YouthGroupDB.YouthGroup) youthGroups);
                    }
                }
                adapter.setYouthGroups(youthGroups);
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
            if (position >= 0 && position < youthGroups.size()) {
                YouthGroupDB.YouthGroup youthGroup = youthGroups.get(position);
                showYouthGroupInfoDialog(youthGroup); // Pass the object here
            }
        }
    }
    private void showYouthGroupInfoDialog(YouthGroupDB.YouthGroup youthGroup) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Youth Group Information")
                .setMessage("Name: " + youthGroup.getYouthGroupName() + "\n" +
                        "Amount Request: " + youthGroup.getAmountRequest() + "\n" +
                        "Email: " + youthGroup.getEmail() + "\n" +
                        "Place: " + youthGroup.getPlace())
                .setPositiveButton("OK", (dialog, id) -> {
                    // Dialog closes when OK is clicked
                });
        builder.create().show();
    }
}