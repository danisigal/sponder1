package com.example.sponder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

// אקטיביטי לרישום ספונסר חדש
public class SponsorRegistrationActivity extends AppCompatActivity {
    // שדות קלט מהמשתמש
    private EditText etSponsorName, etAmount, etEmail, etPlace, etPassword;
    private Button btnRegister;
    // משתנה לאימות משתמשים (Firebase Authentication)
    private FirebaseAuth mAuth;
    // משתנה להפניה למסד הנתונים של Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_registration);

        // אתחול Firebase Authentication והפניה למסד הנתונים
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // קישור רכיבי הקלט מה-XML
        etSponsorName = findViewById(R.id.etSponsorName);
        etAmount = findViewById(R.id.etAmount);
        etEmail = findViewById(R.id.etEmail);
        etPlace = findViewById(R.id.etPlace);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // מאזין ללחיצה על כפתור הרישום
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSponsor(); // קריאה לפונקציה שרושמת את הספונסר
            }
        });
    }

    // פונקציה לרישום ספונסר חדש
    private void registerSponsor() {
        // קבלת הערכים מהשדות
        final String sponsorName = etSponsorName.getText().toString().trim();
        final double amount = Double.parseDouble(etAmount.getText().toString().trim());
        final String email = etEmail.getText().toString().trim();
        final String place = etPlace.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        // יצירת משתמש חדש ב-Firebase Authentication עם אימייל וסיסמה
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // אם ההרשמה הצליחה, מקבלים את המשתמש הנוכחי
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                // יצירת אובייקט ספונסר
                                SponsorDB.Sponsor sponsor = new SponsorDB.Sponsor(sponsorName, amount, email, place);
                                // שמירת הספונסר במסד הנתונים תחת "sponsors" לפי ה-UID של המשתמש
                                mDatabase.child("sponsors").child(user.getUid()).setValue(sponsor)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // הודעת הצלחה וסגירת המסך
                                                Toast.makeText(SponsorRegistrationActivity.this, "Sponsor registered successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // הודעת שגיאה אם השמירה נכשלה
                                                Toast.makeText(SponsorRegistrationActivity.this, "Failed to register Sponsor", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            // הודעת שגיאה אם ההרשמה נכשלה
                            Log.e("XXX", task.getException().getMessage());
                            Toast.makeText(SponsorRegistrationActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}