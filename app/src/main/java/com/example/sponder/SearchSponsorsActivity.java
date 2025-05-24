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

public class SearchSponsorsActivity extends AppCompatActivity implements CardStackListener {

    // משתנים לניהול התצוגה והנתונים
    private CardStackLayoutManager manager;
    private SponsorAdapter adapter;
    private CardStackView cardStackView;
    private List<SponsorDB.Sponsor> sponsors;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_sponsors);

        // כפתור חזרה לדף הבית
        Button backToHomeButton = findViewById(R.id.button_back_to_home);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // מעבר לדף הבית וסיום האקטיביטי הנוכחי
                Intent intent = new Intent(SearchSponsorsActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        // קישור רכיב התצוגה של הקלפים מה-XML
        cardStackView = findViewById(R.id.card_stack_view);
        // הגדרת מנהל הקלפים
        manager = new CardStackLayoutManager(this, this);

        // יצירת מתאם (adapter) ריק בהתחלה
        adapter = new SponsorAdapter(this, new ArrayList<>());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        // שליפת רשימת הספונסרים מה-Database
        fetchSponsors();
    }

    // מאזינים לאירועים של קלפי הספונסרים (אפשר להשאיר ריק אם לא צריך)
    @Override
    public void onCardDisappeared(View view, int position) {}

    @Override
    public void onCardAppeared(View view, int position) {}

    @Override
    public void onCardCanceled() {}

    @Override
    public void onCardRewound() {}

    @Override
    public void onCardDragging(Direction direction, float ratio) {}

    // שליפת כל הספונסרים מה-Database והצגתם על המסך
    private void fetchSponsors() {
        // הפניה ל-node של sponsors ב-Firebase
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("sponsors");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                sponsors = new ArrayList<>();
                // מעבר על כל הספונסרים והוספתם לרשימה
                for (DataSnapshot data : snapshot.getChildren()) {
                    SponsorDB.Sponsor sponsor = data.getValue(SponsorDB.Sponsor.class);
                    if (sponsor != null) {
                        sponsors.add(sponsor);
                    }
                }
                // עדכון המתאם (adapter) עם הרשימה החדשה
                adapter.setSponsors(sponsors);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // טיפול בשגיאה (אם יש)
            }
        });
    }

    // פעולה שמופעלת כאשר גוללים קלף ימינה (לדוג' "אהבתי")
    @Override
    public void onCardSwiped(Direction direction) {
        if (direction == Direction.Right) {
            int position = manager.getTopPosition() - 1;
            if (position >= 0 && position < sponsors.size()) {
                SponsorDB.Sponsor sponsor = sponsors.get(position);
                showSponsorInfoDialog(sponsor); // הצגת פרטי הספונסר
            }
        }
    }

    // תיבת דיאלוג להצגת פרטי הספונסר
    private void showSponsorInfoDialog(SponsorDB.Sponsor sponsor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sponsor Information")
                .setMessage("Name: " + sponsor.getSponsorName() + "\n" +
                        "Amount: " + sponsor.getAmount() + "\n" +
                        "Email: " + sponsor.getEmail() + "\n" +
                        "Place: " + sponsor.getPlace())
                .setPositiveButton("OK", (dialog, id) -> {
                    // סגירת הדיאלוג בלחיצה על OK
                });
        builder.create().show();
    }
}