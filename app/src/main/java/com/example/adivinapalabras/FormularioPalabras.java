package com.example.adivinapalabras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class FormularioPalabras extends AppCompatActivity {
EditText nombre;
EditText descripcion;
Partida p;
int position=0;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);
        nombre=findViewById(R.id.nombrePalabra);
        descripcion=findViewById(R.id.descripcionText);

        /**RECOGER PARITDA DE LA OTRA ACTIVIDAD**/
        Bundle datos = getIntent().getExtras(); //BUNDLE
         p = (Partida) datos.getSerializable("partida"); //RECOGER PARTIDA
         position = datos.getInt("posicion");
        p.getPalabras().get(position);
        nombre.setText(p.getPalabras().get(position).getNombrePalabra());
        descripcion.setText(p.getPalabras().get(position).getDescripcion());
        System.out.println(position);
    }
    public void borrar(View vista){
        String palabra = nombre.getText().toString();
        for (int i=0; i<p.getPalabras().size();i++){
            if(p.getPalabras().get(i).getNombrePalabra().equals(palabra)){
                p.getPalabras().remove(i);
                Toast.makeText(this, "La palabra: "+palabra+" ha sido eliminada del array", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void anadir (View vista){
        String nombrePalabra = nombre.getText().toString();
        String descripcionPalabra = descripcion.getText().toString();
        p.getPalabras().add(new Palabra(nombrePalabra,descripcionPalabra));
    }
    public void volver(View vista){
        Intent i = new Intent(this,mostrarPalabras.class);
        i.putExtra("partida",p);
        startActivity(i);
    }
}
