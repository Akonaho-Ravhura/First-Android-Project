package com.example.bearhug;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Exercise extends AppCompatActivity {

    Button btnsave;
    EditText textInput;
    TextView tv1;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        btnsave = findViewById(R.id.buttonsave);
        textInput = findViewById(R.id.editTextTextPersonName);
        tv1 = findViewById(R.id.steps);




        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                Toast.makeText(Exercise.this, "Input has been saved", Toast.LENGTH_SHORT).show();

            }
        });
    }
}