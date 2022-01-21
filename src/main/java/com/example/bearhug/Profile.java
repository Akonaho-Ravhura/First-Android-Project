package com.example.bearhug;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {
    TextView fullName, email, Phone;
    FirebaseAuth mAuth;
    ImageView profilePic;
    FirebaseFirestore fstore;
    String UserId;
     Button Change, back, pfPhoto;
     StorageReference store;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        fullName = findViewById(R.id.pfName);
        email = findViewById(R.id.pfEmail);
        Phone = findViewById(R.id.pfPhoneNumber);
        Change = findViewById(R.id.btnChange);
        back = findViewById(R.id.Backbtn);
        profilePic = findViewById(R.id.pfPhoto);
        pfPhoto = findViewById(R.id.Change2);
        store = FirebaseStorage.getInstance().getReference();







        mAuth = FirebaseAuth.getInstance();
        fstore = FirebaseFirestore.getInstance();

        UserId = mAuth.getCurrentUser().getUid();


        DocumentReference documentReference = fstore.collection("Users").document(UserId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                Phone.setText(documentSnapshot.getString("Phone Number"));
                fullName.setText(documentSnapshot.getString("Full Name"));
                email.setText(documentSnapshot.getString("Email"));


            }
        });


        Change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Edit = new Intent(Profile.this, EditProfile.class);
                Edit.putExtra("fullName", fullName.getText().toString());
                Edit.putExtra("email", email.getText().toString());
                Edit.putExtra("Phone", Phone.getText().toString());
                startActivity(Edit);


            }
        });

        pfPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGallery, 100);


            }
        });

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open gallery
                Intent openGalleryI = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryI, 100);


            }


        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100){
            if (resultCode== Activity.RESULT_OK){
                Uri imageUri = data.getData();

               // profilePic.setImageURI(imageUri);

                uploadImageToFirebase(imageUri);
            }
        }

    }
    private void uploadImageToFirebase(Uri imageUri) {
        //upload image to firebase storage
        StorageReference fileRef = store.child("profile.jpg");
        fileRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                   @Override
                   public void onSuccess(Uri uri) {
                       Picasso.get().load(uri).into(profilePic);
                   }
               });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(Profile.this, "failed!!!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
