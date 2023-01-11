package com.example.adivinapalabras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class mostrarPalabras extends AppCompatActivity implements Serializable {

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
            crearListView();



    }


    public void volverAtras(View vista) {
        Intent i = new Intent(this, MainActivity.class);
        i.putExtra("partida",p);
        startActivity(i);
    }
    public void crearListView(){
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, nombrePalabras);
        vista.setAdapter(adapter);
        vista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String value = String.valueOf(palabrasMostradas.get(position).getNombrePalabra());
                // Toast.makeText(mostrarPalabras.this, palabrasMostradas.get(position).getDescripcion().toString(), Toast.LENGTH_SHORT).show();
                // Toast.makeText(mostrarPalabras.this, descripcionPalabras[position].toString(), Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), SQLvsMongoDB.class);
                i.putExtra("partida", p);
                i.putExtra("booleano",false); //FALSO SI LA ES UNA PALABRA
                i.putExtra("posicion", position);
                startActivity(i);
            }
        });
    }
    public void AdministrarSQ(View vista){
        Intent i = new Intent(getApplicationContext(), FormularioPalabrasSQL.class);
        i.putExtra("partida", p);
        i.putExtra("booleano",true); //TRUE SI ESTA VACIO.
        startActivity(i);
    }
    public void AdministrarMongoDB(View vista){
        Intent i = new Intent(getApplicationContext(), FormularioPalabrasMongoDB.class);
        i.putExtra("partida", p);
        i.putExtra("booleano",true);
        startActivity(i);
    }
    public void borrarTodo(View vista){
       palabrasMostradas.clear();
        Intent i = new Intent(getApplicationContext(), mostrarPalabras.class);
        i.putExtra("partida", p);
        startActivity(i);
    }
}

