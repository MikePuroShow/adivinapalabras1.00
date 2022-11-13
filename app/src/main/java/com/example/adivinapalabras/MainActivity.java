package com.example.adivinapalabras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView palabrasDisponibles;
    private TextView palabraSeleccionada;
    private TextView intentosRestantes;
    private TextView descripcionPalabra;
    private Button botonResolver;
    private EditText letraElegida;
    private Button otraPalabra;
    private Partida partida;
    private boolean[] palabrasAcertadas;
    private char[] palabra;
    private boolean primeraEjecucion;
    private ArrayList<Palabra> palabrasInicio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializacion vistas
        palabraSeleccionada = findViewById(R.id.palabra);
        intentosRestantes = findViewById(R.id.intentos);
        botonResolver = findViewById(R.id.adivinar);
        letraElegida = findViewById(R.id.letra);
        otraPalabra = findViewById(R.id.nuevo);
        palabrasDisponibles = findViewById(R.id.palabrasDisponibles);
        descripcionPalabra = findViewById(R.id.descripcionPalabra);
        palabrasInicio = new ArrayList<>();

        cargaInicial();
        //se crea la partida
        partida = new Partida(palabrasInicio);

        actualizarPalabras();

        //se realizan acciones de la partida
        mostrarPalabra();
        mostrarDescripcion();
        calcularIntentos(partida.getIntentos());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuVerPalabra:
                Toast.makeText(this,"La palabra actual es: "+partida.getPalabras().get(partida.getPosicion()), Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuAnadirPalabras:
                anadirPalabras();
                return true;
            case R.id.menuMostrarPalabras:
                mostrarPalabrasPartida();
                return true;
            case R.id.menuImportarTXT:
                partida.cargarPalabrasTXT(this);
                actualizarPalabras();
                return true;
            case R.id.menuExportarTXT:
                partida.guardarPalabrasTXT(this);
                Toast.makeText(this, "Palabras exportadas al fichero de texto", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuSalirAplicacion:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo que muestra la palabra con la que se esta jugando en su estado actual
     */
    public void mostrarPalabra() {
        palabraSeleccionada.setText(partida.mostrarPalabraPartida());
    }

    /**
     * Metodo que sirve para mostrar el acierto en la palabra o restar intentos
     *
     * @param vista boton adivinar
     */
    public void resolver(View vista) {
        String cadena = partida.resolverPartida(letraElegida);
        if (cadena.equals("")) {//si no se ha elegido ninguna letra
            Toast.makeText(this, "No has introducido ninguna letra", Toast.LENGTH_LONG).show();
        } else {
            letraElegida.setText("");//se ayuda al usuario a que no tenga que borrar la letra para escribir otra

            if (!partida.acertado()) {//solo se ejecutara si no se ha acertado letra
                Toast.makeText(this, "Has fallado", Toast.LENGTH_LONG).show();
                calcularIntentos(partida.getIntentos());
            }

            //se muestra la palabra en su estado actual tras elegir letra
            mostrarPalabra();
            mostrarDescripcion();

            //se comprueba si se ha ganado o se ha perdido la partida
            comprobar();
        }
    }

    /**
     * Metodo para comprobar si se ha ganado o si se ha perdido la partida
     */
    public void comprobar() {
        if (partida.getIntentos()==0) {
            botonResolver.setEnabled(false);//deshabilita el boton al perder
            mostrarDialogo("Has perdido");
        } else {
            if (partida.comprobarPartida()) {
                botonResolver.setEnabled(false);//deshabilita el boton al perder
                mostrarDialogo("Has ganado");
            }
        }
    }

    /**
     * Metodo que muestra el alertdialog al ganar o perder la partida y permite jugar otra
     *
     * @param estadoPartida mensaje si se ha ganado o no
     */
    public void mostrarDialogo(String estadoPartida) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(estadoPartida);
        builder.setMessage(estadoPartida + ", acepta para jugar otra partida");
        builder.setPositiveButton("Jugar otra partida", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                otraPartida(null);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Metodo para actualizar el textview de los intentosActualizados a los actuales
     *
     * @param intentosActualizados intentosActualizados a mostrar
     */
    public void calcularIntentos(int intentosActualizados) {
        intentosRestantes.setText(String.valueOf(intentosActualizados));
    }

    /**
     * Metodo para actualizar las palabras disponibles en la partida
     */
    public void actualizarPalabras(){
        palabrasDisponibles.setText("Palabras disponibles: "+partida.getPalabras().size());
    }
    public void mostrarDescripcion(){
        descripcionPalabra.setText(partida.getPalabras().get(partida.getPosicion()).getDescripcion());
    }

    /**
     * Metodo para jugar otra partida con una palabra aleatoria
     *
     * @param vista boton que ejecuta el evento
     */
    public void otraPartida(View vista) {
        if (!botonResolver.isEnabled()){
            botonResolver.setEnabled(true);//habilita el boton al empezar una partida si estaba deshabilitado
        }
        partida.elegirPalabraPartida();
        mostrarPalabra();
        mostrarDescripcion();
        calcularIntentos(partida.getIntentos());
    }


    /**
     * Metodo para anadir palabras a la partida
     */
    public void anadirPalabras(){
        LinearLayout layout = new LinearLayout(this);
        EditText palabrasIntroducidas = new EditText(this);
        EditText palabrasDescripcion = new EditText(this);
        AlertDialog.Builder builderPalabras = new AlertDialog.Builder(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(palabrasIntroducidas);
        layout.addView(palabrasDescripcion);
        builderPalabras.setView(layout);
        builderPalabras.setTitle("Añadir palabra");
        builderPalabras.setMessage("Introduzca la palabra que quiere añadir y su descripcion");
        builderPalabras.setPositiveButton("Añadir palabra", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String palabrasLeida = palabrasIntroducidas.getText().toString();
                String palabraDescripcion = palabrasIntroducidas.getText().toString();
                if (palabrasLeida.equals("")) {
                    Toast.makeText(MainActivity.this, "Introduce una palabra", Toast.LENGTH_LONG).show();
                } else {
                    partida.cargarPalabrasUsuario(new Palabra(palabrasLeida,palabraDescripcion));
                    actualizarPalabras();
                }
            }
        });
        AlertDialog dialogPalabras = builderPalabras.create();
        dialogPalabras.show();
    }

    /**
     * Metodo para mostrar las palabras de la partida en un list view
     */
    public void mostrarPalabrasPartida(){
        Intent i = new Intent(this,mostrarPalabras.class);

        //i.putExtra("palabras",partida.getPalabras());
        i.putExtra("partida",partida);

        startActivity(i);
    }

    public void cargaInicial(){
        String[]nombrePalabras = {"pantalla","cielo","ordenador"};
        String[]descripcion = {"Dispositivo de salida que representa visualmente la información.","Espacio en el que se mueven los astros.","Maquina encargada de procesar datos."};
        for (int i = 0; i < nombrePalabras.length; i++) {
            palabrasInicio.add(new Palabra(nombrePalabras[i],descripcion[i]));
        }
    }

}