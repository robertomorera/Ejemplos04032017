package com.example.cice.subactivities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent=new Intent(this,SubActivity.class);
        startActivityForResult(intent,55);

        Log.d(getClass().getCanonicalName(),"SubActivity lanzada");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d(getClass().getCanonicalName(),"COD PETICION= "+requestCode+" CODIGO RESPUESTA= "+resultCode);
        String nombre=data.getStringExtra("NOMBRE");
        Log.d(getClass().getCanonicalName(),"String devuelto= "+nombre);
        super.onActivityResult(requestCode,resultCode,data);


    }

    @Override
    protected void onPause() {
        Log.d(getClass().getCanonicalName(),"Activity on Pause");
        super.onPause();
    }
}
