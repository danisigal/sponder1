package com.example.sponder;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.sponder.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
    // אובייקט של Firebase לאימות משתמשים
    FirebaseAuth auth;
    // אובייקט לניהול התחברות עם חשבון גוגל
    GoogleSignInClient googleSignInClient;
    // אלמנטים של ממשק המשתמש להצגת תמונה, שם ודוא"ל
    ShapeableImageView imageView;
    TextView name, mail;

    // לאנצ'ר שמטפל בתוצאה מהאקטיביטי של ההתחברות עם גוגל
    private final ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                // פונקציה שמופעלת כאשר חוזרים מהאקטיביטי של גוגל
                public void onActivityResult(ActivityResult result) {
                    Log.d("onActivityResult", result.toString());

                    // בודק אם התוצאה תקינה (המשתמש התחבר)
                    if (result.getResultCode() == RESULT_OK) {
                        // מקבל את חשבון הגוגל מה-intent שחזר
                        Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(result.getData());
                        try {
                            // מנסה לקבל את החשבון
                            GoogleSignInAccount signInAccount = accountTask.getResult(ApiException.class);
                            // יוצר אישור (Credential) בעזרת הטוקן של גוגל
                            AuthCredential authCredential = GoogleAuthProvider.getCredential(signInAccount.getIdToken(), null);
                            // מתחבר ל-Firebase עם האישורים של גוגל
                            auth.signInWithCredential(authCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    // אם ההתחברות הצליחה
                                    if (task.isSuccessful()) {
                                        auth = FirebaseAuth.getInstance();
                                        // טוען את תמונת הפרופיל עם Glide
                                        Glide.with(LoginActivity.this)
                                                .load(Objects.requireNonNull(auth.getCurrentUser()).getPhotoUrl())
                                                .into(imageView);
                                        // מציג את שם המשתמש והאימייל בממשק
                                        name.setText(auth.getCurrentUser().getDisplayName());
                                        mail.setText(auth.getCurrentUser().getEmail());
                                        // הודעת הצלחה
                                        Toast.makeText(LoginActivity.this, "Sign in successfully!", Toast.LENGTH_SHORT).show();
                                        // מעבר למסך הראשי (MainActivity) עם שם המשתמש
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("USERNAME", auth.getCurrentUser().getDisplayName());
                                        startActivity(intent);
                                    } else {
                                        // הודעת שגיאה אם ההתחברות נכשלה
                                        Toast.makeText(LoginActivity.this, "Failed to sign in: " + task.getException(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } catch (ApiException e) {
                            // טיפול בשגיאה אם ההתחברות נכשלה
                            e.printStackTrace();
                        }
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // הפעלת תצוגה מקצה לקצה (Edge-to-Edge)
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // אתחול Firebase (חובה לפני שימוש בשירותים של Firebase)
        FirebaseApp.initializeApp(this);

        // קישור אלמנטים מה-XML למשתנים בקוד
        imageView = findViewById(R.id.profileImage);
        name = findViewById(R.id.nameTV);
        mail = findViewById(R.id.mailTV);

        // הגדרת אפשרויות להתחברות עם גוגל
        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.client_id)) // בקשת טוקן מזהה מ-Google
                .requestEmail() // בקשת אימייל מהמשתמש
                .build();

        // יצירת אובייקט להתחברות עם גוגל לפי ההגדרות
        googleSignInClient = GoogleSignIn.getClient(LoginActivity.this, options);

        // קבלת מופע של FirebaseAuth
        auth = FirebaseAuth.getInstance();

        // קישור כפתור ההתחברות מה-XML
        SignInButton signInButton = findViewById(R.id.signIn);
        // מאזין ללחיצה על כפתור ההתחברות
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // פתיחת אקטיביטי ההתחברות של גוגל
                Intent intent = googleSignInClient.getSignInIntent();
                activityResultLauncher.launch(intent);
            }
        });
    }
}