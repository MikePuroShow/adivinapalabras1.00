package com.example.adivinapalabras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private Partida p;
    private TextView palabraSeleccionada;
    private TextView intentosRestantes;
    private Button botonResolver;
    private EditText letraElegida;
    private Button otraPalabra;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*
        //inicializacion arraylist palabras
        palabras = new ArrayList<String>();
        cargarPalabras();
        cargarPalabrasXML();*/

        /*
        //inicializacion vistas
        palabraSeleccionada = findViewById(R.id.palabra);
        intentosRestantes = findViewById(R.id.intentos);
        botonResolver = findViewById(R.id.adivinar);
        letraElegida = findViewById(R.id.letra);
        otraPalabra = findViewById(R.id.nuevo);*/

        /*
        //inicio primera partida
        elegirPalabra();*/
        p = new Partida(findViewById(R.id.palabra),findViewById(R.id.intentos),findViewById(R.id.adivinar),findViewById(R.id.letra),this);
    }

    public void resolver(View vista){
        p.resolver(null);
    }

    public void otraPartida(View vista){
        p.otraPartida(null);
    }


}