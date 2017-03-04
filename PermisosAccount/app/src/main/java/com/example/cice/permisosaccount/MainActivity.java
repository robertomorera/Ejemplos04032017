package com.example.cice.permisosaccount;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(tienePermisos()){
            obtenerCorreosUsuario();
        }else{
            pedirPermisos();
        }

    }

    boolean tienePermisos(){
        boolean tienePermiso=false;
        tienePermiso=ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)==PackageManager.PERMISSION_GRANTED;
        return tienePermiso;
    }

    private void pedirPermisos(){

        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.GET_ACCOUNTS)){
            //Mostramos mensaje explicativo AlertDialog.

        }

        //Pedimos el permiso
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.GET_ACCOUNTS},100);
    }


    private String[] obtenerCorreosUsuario (){
        String[] lista_correos=null;
        Account[] lista_cuentas=null;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)==PackageManager.PERMISSION_GRANTED){
            AccountManager accountManager=(AccountManager)getSystemService(ACCOUNT_SERVICE);
            lista_cuentas=accountManager.getAccounts();
            for(Account cuenta:lista_cuentas){
                if(cuenta.type.equals("com.google")) {
                    Log.d(getClass().getCanonicalName(), "Cuenta de correo de GMAIL " + cuenta.name);
                }else if(cuenta.type.equals(("com.facebook.auth.login"))){
                    4Log.d(getClass().getCanonicalName(), "Cuenta de FACEBOOK " + cuenta.name);

                }else{
                    Log.d(getClass().getCanonicalName(),"Cuenta (No google) tipo " +cuenta.type);
                }
            }
        }

        return lista_correos;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Si el usuario ha concedido los permisos
        if((grantResults.length>0)&&(grantResults[0]==PackageManager.PERMISSION_GRANTED)){
            Log.d(getClass().getCanonicalName(),"PERMISO CONCEDIDO");
            obtenerCorreosUsuario();
        }else{
            Log.d(getClass().getCanonicalName(),"PERMISO DENEGADO Y SALIMOS DE LA APP");
            finish();
        }
    }
}