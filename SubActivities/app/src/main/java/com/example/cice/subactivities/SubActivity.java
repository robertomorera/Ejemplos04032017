package com.example.cice.subactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SubActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);
        Intent i= new Intent();
        i.putExtra("NOMBRE","Pepa Pig");

        setResult(8,i);
        finish();
    }
}
