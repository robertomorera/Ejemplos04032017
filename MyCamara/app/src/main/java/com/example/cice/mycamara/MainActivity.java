package com.example.cice.mycamara;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private String ruta_foto;



    private void tomarFoto(){

        Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = crearFicheroImagen();
        //Esto hace que la foto se guarde en photoUri, y omitimos esto la foto se guarga en un BITMAP.
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUri);
        startActivityForResult(intent,87);
    }

    private Uri crearFicheroImagen(){
        Uri uriDevuelta=null;
        //Crear un fichero en memoria y obtener su URI.
        String momentoActual=null;
        String nombreFichero=null;
        File f=null;
        momentoActual=new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        nombreFichero="fichero"+momentoActual+".jpg";
        //Destino en la memoria externa donde se guarda la foto;
        ruta_foto=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getPath()+"/"+nombreFichero;
        f=new File(ruta_foto);
        try {
            if(f.createNewFile()){
                Log.d(getClass().getCanonicalName(),"Fichero creado! "+ruta_foto);
            }else{
                Log.d(getClass().getCanonicalName(),"Fichero no creado!");
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(getClass().getCanonicalName(),"Error creando el fichero",e);
        }
        //Construye la Uri a partir de un objeto de tipo File.
        uriDevuelta=Uri.fromFile(f);
        return uriDevuelta;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pedirPermisos();
    }


    private void pedirPermisos(){
        String [] permisos=new String[2];
        permisos[0]= Manifest.permission.CAMERA;
        permisos[1]=Manifest.permission.WRITE_EXTERNAL_STORAGE;
        ActivityCompat.requestPermissions(this,permisos,999);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if((grantResults[0]== PackageManager.PERMISSION_GRANTED)&&(grantResults[1]== PackageManager.PERMISSION_GRANTED)){
            Log.d(getClass().getCanonicalName(),"Permisos para CÁMARA y MEMORIA concedidos");
            tomarFoto();
        }else{
            Log.d(getClass().getCanonicalName(),"Permisos para CÁMARA O MEMORIA no concedidos");
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(getClass().getCanonicalName(),"Vuelvo de hacer la foto");
        switch(resultCode){
            case RESULT_OK:
                Log.d(getClass().getCanonicalName(),"El usuario guardó la foto");
                Bitmap bitmap= null;
                if(data==null){//El fichero ha sido guardado en una ruta, se usó el putExtra.
                    Log.d(getClass().getCanonicalName(),"El usuario guardó la foto en URI");
                    File file=new File(ruta_foto);
                    bitmap= BitmapFactory.decodeFile(file.getAbsolutePath());

                }else{
                    Log.d(getClass().getCanonicalName(),"El usuario no guardó la foto en URI(la foto se guardó en memoria)");
                    bitmap=(Bitmap)data.getExtras().get("data");
                }
                //Ponemos la foto en el ImageView.
                ImageView imageView=(ImageView)findViewById(R.id.imagen);
                imageView.setImageBitmap(bitmap);
                break;
            case RESULT_CANCELED:
                Log.d(getClass().getCanonicalName(),"El usuario descartó la foto");
                break;
            default:
                Log.d(getClass().getCanonicalName(),"Algo no fue bien");

        }
    }
}
