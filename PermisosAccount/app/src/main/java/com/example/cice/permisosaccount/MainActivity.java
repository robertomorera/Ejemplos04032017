package com.example.cice.permisosaccount;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

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

        Button botonLista=(Button)findViewById(R.id.btn_listarCuentas);
        botonLista.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Obtenemos la sharedPreferences.
                SharedPreferences sp=getSharedPreferences("listasCuentasUsuario",Context.MODE_PRIVATE);
                //Obtenemos el número de cuentas del usuario.
                int numeroCuentas=sp.getInt("numeroCuentas",0);
                String[] listaCuentas= new String[numeroCuentas];
                for(int i=0;i<listaCuentas.length;i++){
                    listaCuentas[i]=sp.getString("cuenta"+i,"test");
                }
                ArrayAdapter<String> listAdapter=new ArrayAdapter<String>(MainActivity.this,R.layout.fila,R.id.textView,listaCuentas);
                ListView listView=(ListView)findViewById(R.id.listaCuentas);
                listView.setAdapter(listAdapter);
            }


        });



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


    private List<String> obtenerCorreosUsuario (){
        List<String> lista_correos=null;
        Account[] lista_cuentas=null;
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS)==PackageManager.PERMISSION_GRANTED){
            AccountManager accountManager=(AccountManager)getSystemService(ACCOUNT_SERVICE);
            lista_cuentas=accountManager.getAccounts();
            SharedPreferences sharedPreferences=getSharedPreferences("listasCuentasUsuario", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            int i=0;
            for(Account cuenta:lista_cuentas){
                 lista_correos=new ArrayList<String>();
                 lista_correos.add(cuenta.name);
                //Añadimos las cuentas en el array de String[]

                /**if(cuenta.type.equals("com.google")) {
                    Log.d(getClass().getCanonicalName(), "Cuenta de correo de GMAIL " + cuenta.name);
                }else if(cuenta.type.equals(("com.facebook.auth.login"))){
                    Log.d(getClass().getCanonicalName(), "Cuenta de FACEBOOK " + cuenta.name);

                }else{
                    Log.d(getClass().getCanonicalName(),"Cuenta (No google) tipo " +cuenta.type);
                }**/
                editor.putInt("numeroCuentas",lista_cuentas.length);
                editor.putString("cuenta"+i,cuenta.name);
                editor.commit();
                i++;
            }
        }
        //Creamos el sharedPreferences y metemos la lista de las cuentas.



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