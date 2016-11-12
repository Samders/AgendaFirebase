package com.alberthneerans.agendafirebase;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    EditText eId, eNombre, eTelefono, eCorreo;
    private String FIREBASE_URL="https://agendafirebase-b1c45.firebaseio.com/";
    private Firebase firebasedatos;
    Integer id=0;
    String nombre, correo,telefono,codigo;
    Button bInsertar, bActualizar, bBorrar, bBuscar,bLimpiar;
    ArrayList<Contacto> info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this);
        firebasedatos = new Firebase(FIREBASE_URL);

        info = new ArrayList<Contacto>();

        eId = (EditText) findViewById(R.id.eId);
        eNombre = (EditText) findViewById(R.id.eNombre);
        eTelefono = (EditText) findViewById(R.id.eTelefono);
        eCorreo = (EditText) findViewById(R.id.eMail);
        bInsertar = (Button) findViewById(R.id.bInsertar);
        bActualizar = (Button) findViewById(R.id.bActualizar);
        bBorrar = (Button) findViewById(R.id.bBorrar);
        bBuscar = (Button) findViewById(R.id.bBuscar);
        bLimpiar = (Button) findViewById(R.id.bLimpiar);

        bInsertar.setOnClickListener(this);
        bActualizar.setOnClickListener(this);
        bBorrar.setOnClickListener(this);
        bBuscar.setOnClickListener(this);
        bLimpiar.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        nombre=eNombre.getText().toString();
        correo=eCorreo.getText().toString();
        telefono=eTelefono.getText().toString();
        codigo=eId.getText().toString();
        Firebase firebd;
        switch(view.getId()){
            case R.id.bInsertar:
                Contacto contacto = new Contacto(nombre, telefono,correo,codigo);
                firebd =firebasedatos.child("contacto"+ id);
                firebd.setValue(contacto);
                id++;
                limpiar();

                break;
            case R.id.bActualizar:
                firebd = firebasedatos.child("contacto "+codigo);

                Map<String,Object> nuevonombre = new HashMap<>();
                nuevonombre.put("nombre",nombre);
                firebd.updateChildren(nuevonombre);

                Map<String,Object> nuevotelefono = new HashMap<>();
                nuevonombre.put("telefono",telefono);
                firebd.updateChildren(nuevotelefono);

                Map<String,Object> nuevocorreo = new HashMap<>();
                nuevonombre.put("mail",correo);
                firebd.updateChildren(nuevocorreo);
                limpiar();
                break;
            case R.id.bBorrar:
                firebd =firebasedatos.child("contacto"+ codigo);
                firebd.removeValue();
                break;
            case R. id.bBuscar:
                final String code = "contacto "+codigo;
                firebasedatos.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(code).exists()){
                            info.add(dataSnapshot.child("contacto "+codigo).getValue(Contacto.class));
                            Log.d("data",dataSnapshot.child(code).getValue().toString());
                            eNombre.setText(info.get(0).getNombre());
                            eTelefono.setText(info.get(0).getTelefono());
                            eCorreo.setText(info.get(0).getMail());
                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                    }
                });
                break;
            case R.id.bLimpiar: limpiar();
                break;
        }

    }

    private void limpiar() {
        eNombre.setText("");
        eCorreo.setText("");
        eTelefono.setText("");
        eId.setText("");
    }
}
