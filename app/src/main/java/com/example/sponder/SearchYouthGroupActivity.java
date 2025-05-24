
package com.example.sponder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

public class SearchYouthGroupActivity extends AppCompatActivity implements CardStackListener {

    // משתנים לניהול התצוגה והמידע
    private CardStackLayoutManager manager;
    private YouthGroupAdapter adapter;
    private CardStackView cardStackView;
    private List<YouthGroupDB.YouthGroup> youthGroups;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_youth_group);

        // הגדרת כפתור "חזרה לדף הבית"
        Button backToHomeButton = findViewById(R.id.button_back_to_home);
        backToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // הודעה קצרה למשתמש
                Toast.makeText(SearchYouthGroupActivity.this, "Going back to Home", Toast.LENGTH_SHORT).show();
                // מעבר לדף הבית וסיום האקטיביטי הנוכחי
                Intent intent = new Intent(SearchYouthGroupActivity.this, MainActivity.class);
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
        adapter = new YouthGroupAdapter(this, new ArrayList<>());
        cardStackView.setLayoutManager(manager);
        cardStackView.setAdapter(adapter);

        // שליפת קבוצות הנוער מה-Database
        fetchYouthGroups();
    }

    // שליפת כל קבוצות הנוער מה-Database והצגתן על המסך
    private void fetchYouthGroups() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("youthGroups");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                youthGroups = new ArrayList<>();
                // מעבר על כל קבוצות הנוער והוספתן לרשימה
                for (DataSnapshot data : snapshot.getChildren()) {
                    YouthGroupDB.YouthGroup youthGroup = data.getValue(YouthGroupDB.YouthGroup.class);
                    if (youthGroup != null) {
                        youthGroups.add(youthGroup);
                    }
                }
                // עדכון המתאם (adapter) עם הרשימה החדשה
                adapter.setYouthGroups(youthGroups);
                adapter.notifyDataSetChanged();

                // הודעה למשתמש כמה קבוצות נטענו
                Toast.makeText(SearchYouthGroupActivity.this, "Loaded: " + youthGroups.size(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // הודעת שגיאה אם יש בעיה בשליפה מה-Database
                Toast.makeText(SearchYouthGroupActivity.this, "Database error: " + error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    // מאזינים לאירועים של קלפי קבוצות הנוער (אפשר להשאיר ריק אם לא צריך)
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

    // פעולה שמופעלת כאשר גוללים קלף ימינה (לדוג' "אהבתי")
    @Override
    public void onCardSwiped(Direction direction) {
        if (direction == Direction.Right) {
            int position = manager.getTopPosition() - 1;
            if (position >= 0 && position < youthGroups.size()) {
                YouthGroupDB.YouthGroup youthGroup = youthGroups.get(position);
                showYouthGroupInfoDialog(youthGroup); // הצגת פרטי קבוצת הנוער
            }
        }
    }

    // תיבת דיאלוג להצגת פרטי קבוצת הנוער
    private void showYouthGroupInfoDialog(YouthGroupDB.YouthGroup youthGroup) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Youth Group Information")
                .setMessage("Name: " + youthGroup.getYouthGroupName() + "\n" +
                        "Amount Request: " + youthGroup.getAmountRequest() + "\n" +
                        "Email: " + youthGroup.getEmail() + "\n" +
                        "Place: " + youthGroup.getPlace())
                .setPositiveButton("OK", (dialog, id) -> {
                    // סגירת הדיאלוג בלחיצה על OK
                });
        builder.create().show();
    }
}