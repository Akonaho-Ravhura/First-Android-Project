package com.example.bearhug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class EditProfile extends AppCompatActivity {
    EditText profileFullName,profileEmail,profilePhone;
    ImageView profileImageView;
    Button saveBtn,btnBack;
    FirebaseAuth mAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);


        Intent data = getIntent();
        final String fullName = data.getStringExtra("fullName");
        String email = data.getStringExtra("email");
        String phone = data.getStringExtra("Phone");


        profileFullName = findViewById(R.id.etPersonName);
        profileEmail = findViewById(R.id.etEMail1);
        profilePhone = findViewById(R.id.etPhoneNumber);
        profileImageView = findViewById(R.id.imageView);
        saveBtn = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.BackToMain);
        mAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = mAuth.getCurrentUser();


        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(EditProfile.this, "click!! ohh nothing happens", Toast.LENGTH_SHORT).show();

            }
        });



        profileFullName.setText(fullName);
        profileEmail.setText(email);
        profilePhone.setText(phone);



        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (profileFullName.getText().toString().isEmpty()||profileEmail.getText().toString().isEmpty()||profilePhone.getText().toString().isEmpty())
                {
                    Toast.makeText(EditProfile.this, "one or more fields is empty", Toast.LENGTH_SHORT).show();
                    return;
                }


                String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String,Object> edited = new HashMap<>();
                        edited.put("Email",email);
                        edited.put("Full Name", profileFullName.getText().toString());
                        edited.put("Phone Number", profilePhone.getText().toString());
                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(EditProfile.this, "profile data updated", Toast.LENGTH_SHORT).show();
                           startActivity(new Intent(getApplicationContext(),Profile.class));
                           finish();
                            }
                        });
                        Toast.makeText(EditProfile.this, "email updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(EditProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });








    }
}