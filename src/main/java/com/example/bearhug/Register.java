package com.example.bearhug;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {



    EditText eFullName,tEmail, mPassword, ePhone;
    Button BtnRegister;
    FirebaseAuth mAuth;
    TextView Login;
    FirebaseFirestore fstore;
    String UserId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eFullName = findViewById(R.id.etFullName);
        tEmail = findViewById(R.id.etEmailreg);
        mPassword = findViewById(R.id.etPassword_);
        ePhone = findViewById(R.id.etPhone);
        BtnRegister = findViewById(R.id.btnRegister);
        Login = findViewById(R.id.tvLogin_);


        fstore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null)
        {
           startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }

        BtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = tEmail.getText().toString();
                String Password = mPassword.getText().toString();
                String FullName = eFullName.getText().toString();
                String PhoneN = ePhone.getText().toString();
                

                if (TextUtils.isEmpty(Email))
                {
                    tEmail.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(Password))
                {
                    mPassword.setError("Password is required!");
                    return;

                }

                if (Password.length() < 6)
                {
                    mPassword.setError("Password must be 6 characters or more");

                }

                //Register User into firebase database
                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful())
                        {
                            //send email link
                            FirebaseUser User = mAuth.getCurrentUser();
                            User.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(Register.this, "Verification email has been sent!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(Register.this, " Error Email not sent!!!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            //verify email address



                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();
                            UserId = mAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fstore.collection("Users").document(UserId);
                            Map<String, Object> user = new HashMap<>();
                            user.put("Full Name", FullName);
                            user.put("Email", Email);
                            user.put("Phone Number", PhoneN);
                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                }
                            });
                            Intent i = new Intent(Register.this, MainActivity.class);
                            startActivity(i);





                        }else
                            {
                                Toast.makeText(Register.this, "Error!"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                            }
                    }
                });


            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Register.this, Login.class);
                startActivity(i);
            }
        });
    }
}