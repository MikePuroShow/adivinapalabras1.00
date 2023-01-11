package com.example.adivinapalabras;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class SQLvsMongoDB extends AppCompatActivity implements Serializable {

    Partida p;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mongodbvssqllite);
        Bundle datos = getIntent().getExtras();
        p = (Partida) datos.getSerializable("partida"); //RECOGER PARTIDA
        position = datos.getInt("posicion");

    }

    public void adSQLite (View vista){
        Intent i = new Intent(getApplicationContext(), FormularioPalabrasSQL.class);
        i.putExtra("partida", p);
        i.putExtra("booleano",false); //FALSO SI LA ES UNA PALABRA
        i.putExtra("posicion", position);
        startActivity(i);
    }
    public void adMongoDB (View vista){
        Intent i = new Intent(getApplicationContext(), FormularioPalabrasMongoDB.class);
        i.putExtra("partida", p);
        i.putExtra("booleano",false); //FALSO SI LA ES UNA PALABRA
        i.putExtra("posicion", position);
        startActivity(i);
    }
    public void adMySQL (View vista){
        Intent i = new Intent(getApplicationContext(), CruMYSQL.class);
        i.putExtra("partida", p);
        i.putExtra("booleano",false); //FALSO SI LA ES UNA PALABRA
        i.putExtra("posicion", position);
        startActivity(i);
    }

}
