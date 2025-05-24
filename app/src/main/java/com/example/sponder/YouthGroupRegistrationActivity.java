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

// אקטיביטי לרישום קבוצת נוער חדשה
public class YouthGroupRegistrationActivity extends AppCompatActivity {
    // שדות קלט מהמשתמש
    private EditText etYouthGroupName, etAmountRequest, etEmail, etPlace, etPassword;
    private Button btnRegister;
    // משתנה לאימות משתמשים (Firebase Authentication)
    private FirebaseAuth mAuth;
    // משתנה להפניה למסד הנתונים של Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_youth_group_registration);

        // אתחול Firebase Authentication והפניה למסד הנתונים
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // קישור רכיבי הקלט מה-XML
        etYouthGroupName = findViewById(R.id.etYouthGroupName);
        etAmountRequest = findViewById(R.id.etAmountRequest);
        etEmail = findViewById(R.id.etEmail);
        etPlace = findViewById(R.id.etPlace);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        // מאזין ללחיצה על כפתור הרישום
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerYouthGroup(); // קריאה לפונקציה שרושמת את קבוצת הנוער
            }
        });
    }

    // פונקציה לרישום קבוצת נוער חדשה
    private void registerYouthGroup() {
        // קבלת הערכים מהשדות
        final String youthGroupName = etYouthGroupName.getText().toString().trim();
        final double amountRequest = Double.parseDouble(etAmountRequest.getText().toString().trim());
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
                                // יצירת אובייקט קבוצת נוער
                                YouthGroupDB.YouthGroup youthGroup = new YouthGroupDB.YouthGroup(youthGroupName, email, amountRequest, place);
                                // שמירת הקבוצה במסד הנתונים תחת "youthGroups" לפי ה-UID של המשתמש
                                mDatabase.child("youthGroups").child(user.getUid()).setValue(youthGroup)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                // הודעת הצלחה וסגירת המסך
                                                Toast.makeText(YouthGroupRegistrationActivity.this, "Youth Group registered successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // הודעת שגיאה אם השמירה נכשלה
                                                Toast.makeText(YouthGroupRegistrationActivity.this, "Failed to register Youth Group", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            // הודעת שגיאה אם ההרשמה נכשלה
                            Log.e("XXX", task.getException().getMessage());
                            Toast.makeText(YouthGroupRegistrationActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}