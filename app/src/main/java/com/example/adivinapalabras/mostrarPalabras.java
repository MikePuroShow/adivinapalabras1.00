package com.example.adivinapalabras;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class mostrarPalabras extends AppCompatActivity {

    private ListView vista;
    List<String> palabrasMostradas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mostrar_palabras);

        vista = findViewById(R.id.listViewPalabras);

        palabrasMostradas = new ArrayList<String>();


        //palabrasMostradas = getIntent().getStringArrayListExtra("palabras");
        Intent i = getIntent();
        Partida p = (Partida) i.getSerializableExtra("partida");
        if(p!=null){
            palabrasMostradas = p.getPalabras();
        }



        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,palabrasMostradas);
        vista.setAdapter(adapter);

        vista.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long l) {
                String value = String.valueOf(palabrasMostradas.get(position));
                //Toast.makeText(getApplicationContext(),"funciona",Toast.LENGTH_SHORT).show();

                Uri webpage = Uri.parse("https://www.wikipedia.org");
                Intent intent = new Intent(Intent.ACTION_VIEW,webpage);
                if(intent.resolveActivity(getPackageManager())!=null){
                    startActivity(intent);
                }

            }
        });


    }

    public void volverAtras(View vista){
        Intent i = new Intent(this,MainActivity.class);
        i.putExtra("palabrasDevueltas", (ArrayList) palabrasMostradas);
        startActivity(i);
    }

}