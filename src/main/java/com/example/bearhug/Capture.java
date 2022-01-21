package com.example.bearhug;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Capture extends AppCompatActivity {
    public static final int GALLERY_REQUEST_CODE = 105;
    public static final int REQUEST_CODE = 102;

    ImageView selected;
    Button btnCamera, btngallery, btnsave;
    String currentPhotoPath;
    StorageReference storeRef;
    EditText text1;
    String text;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture);

        storeRef = FirebaseStorage.getInstance().getReference();

        selected = findViewById(R.id.imgViu);
        btnCamera = findViewById(R.id.btnCap_);
        btngallery = findViewById(R.id.btnGal);
        btnsave = findViewById(R.id.btn4);
        text1 = findViewById(R.id.etfoodInfo);

       // to retrieve data
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        String tet = prefs.getString("text","");
        text1.setText(tet);


        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text = text1.getText().toString();
                //to save data
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Capture.this);
                SharedPreferences.Editor editor = prefs.edit();


                editor.putString("text", text);
                editor.apply();
                Toast.makeText(Capture.this, "Information Saved!!", Toast.LENGTH_SHORT).show();

            }
        });




        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                askCamperm();

            }
        });


        btngallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(gallery, GALLERY_REQUEST_CODE);


            }
        });
    }

    private void askCamperm() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, new  String[]{Manifest.permission.CAMERA}, REQUEST_CODE);
        }

    }


}
