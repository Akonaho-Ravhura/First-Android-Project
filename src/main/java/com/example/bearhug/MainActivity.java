package com.example.bearhug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    Button Resend, btnprof, btnExercise, btnphysio, Record, btnCapture;
    TextView verify;
    String UserId;
    FirebaseAuth mAuth;
    FirebaseFirestore fstore;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Resend = findViewById(R.id.btnVerify);
        verify = findViewById(R.id.tvEmail);
        btnprof = findViewById(R.id.btnProfile);
        btnExercise = findViewById(R.id.Exercise);
        btnCapture = findViewById(R.id.btnCap);
        btnphysio = findViewById(R.id.btnPhys);
        Record = findViewById(R.id.btnReport);


        mAuth  = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();
        UserId = mAuth.getCurrentUser().getUid();

        FirebaseUser user = mAuth.getCurrentUser();

        btnprof.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profile = new Intent(MainActivity.this, Profile.class);
                startActivity(profile);
            }
        });

        btnExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent B = new Intent(MainActivity.this, Exercise.class);
                startActivity(B);
            }
        });

        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blue = new Intent(MainActivity.this, Capture.class);
                startActivity(blue);
            }
        });

            btnphysio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent red = new Intent(MainActivity.this, Physiology.class);
                    startActivity(red);
                }
            });

        if (!user.isEmailVerified())
             {
              verify.setVisibility(View.VISIBLE);
              Resend.setVisibility(View.VISIBLE);

              Resend.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      //send email link
                      FirebaseUser user = mAuth.getCurrentUser();
                      user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {

                              Toast.makeText(v.getContext(), "Verification email has been sent!", Toast.LENGTH_SHORT).show();
                          }
                      }).addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Toast.makeText(v.getContext(), " Error Email not sent!!!", Toast.LENGTH_SHORT).show();
                          }
                      });
                  }
              });
             }


        btnExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent blue = new Intent(MainActivity.this, Exercise.class);
                startActivity(blue);
            }
        });
    }
    public void logout(View view)
    {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), Login.class));
        finish();
    }
}