package com.example.adivinapalabras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class mostrarPalabras extends AppCompatActivity {

    private ListView vista;
    List<Palabra> palabrasMostradas;
    Partida p;
    String[] nombrePalabras;
    String[] descripcionPalabras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_palabras);

        vista = findViewById(R.id.listViewPalabras);

        palabrasMostradas = new ArrayList<>();

        Intent i = getIntent();
        p = (Partida) i.getSerializableExtra("partida");
        palabrasMostradas = p.getPalabras();
        nombrePalabras = new String[palabrasMostradas.size()];
        descripcionPalabras = new String[palabrasMostradas.size()];

        for (int j = 0; j < palabrasMostradas.size(); j++) {
            nombrePalabras[j] = palabrasMostradas.get(j).getNombrePalabra();
            descripcionPalabras[j] = palabrasMostradas.get(j).getDescripcion();
        }


        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nombrePalabras);
        vista.setAdapter(adapter);
        vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String value = String.valueOf(palabrasMostradas.get(position).getNombrePalabra());
                Toast.makeText(mostrarPalabras.this, palabrasMostradas.get(position).getDescripcion().toString(), Toast.LENGTH_SHORT).show();
                Toast.makeText(mostrarPalabras.this, descripcionPalabras[position].toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void volverAtras(View vista) {
        finish();
    }

}