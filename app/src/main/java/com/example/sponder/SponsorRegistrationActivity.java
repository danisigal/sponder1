package com.example.sponder;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SponsorRegistrationActivity extends AppCompatActivity {
    private EditText etSponsorName, etAmount, etEmail, etPlace, etPassword;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsor_registration);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        etSponsorName = findViewById(R.id.etSponsorName);
        etAmount = findViewById(R.id.etAmount);
        etEmail = findViewById(R.id.etEmail);
        etPlace = findViewById(R.id.etPlace);
        etPassword = findViewById(R.id.etPassword);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerSponsor();
            }
        });
    }

    private void registerSponsor() {
        final String sponsorName = etSponsorName.getText().toString().trim();
        final double amount = Double.parseDouble(etAmount.getText().toString().trim());
        final String email = etEmail.getText().toString().trim();
        final String place = etPlace.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                SponsorDB.Sponsor sponsor = new SponsorDB.Sponsor(sponsorName, amount, email,place);
                                mDatabase.child("sponsors").child(user.getUid()).setValue(sponsor)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(SponsorRegistrationActivity.this, "Sponsor registered successfully", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(SponsorRegistrationActivity.this, "Failed to register Sponsor", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            }
                        } else {
                            Log.e("XXX", task.getException().getMessage());
                            Toast.makeText(SponsorRegistrationActivity.this, "Authentication failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}