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

    private TextView palabraSeleccionada;
    private TextView intentosRestantes;
    private Button botonResolver;
    private EditText letraElegida;
    private Button otraPalabra;
    private Partida p;

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

        p = new Partida();
        mostrarPalabra();
        calcularIntentos(p.getIntentos());
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }*/

    /*
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menuAnadirPalabras:
                EditText palabrasIntroducidas = new EditText(this);
                AlertDialog.Builder builderPalabras = new AlertDialog.Builder(this);
                builderPalabras.setTitle("Añadir palabras");
                builderPalabras.setMessage("Introduzca la/s palabra/s que quiere añadir, separadas por coma");
                builderPalabras.setView(palabrasIntroducidas);
                builderPalabras.setPositiveButton("Añadir palabras", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String palabrasLeidas = palabrasIntroducidas.getText().toString();
                        if (palabrasLeidas.equals("")) {
                            Toast.makeText(MainActivity.this, "Introduce alguna palabra", Toast.LENGTH_LONG).show();
                        } else {
                            cargarPalabrasUsuario(palabrasLeidas.replaceAll(" ", "").split(","));
                        }
                    }
                });
                AlertDialog dialogPalabras = builderPalabras.create();
                dialogPalabras.show();
                return true;
            case R.id.menuModificarIntentos:
                EditText nuevosIntentos = new EditText(this);
                AlertDialog.Builder builderIntentos = new AlertDialog.Builder(this);
                builderIntentos.setTitle("Modificar intentos");
                builderIntentos.setMessage("Introduzca los intentos con los que quiere jugar");
                builderIntentos.setView(nuevosIntentos);
                builderIntentos.setPositiveButton("Todas las partidas", new DialogInterface.OnClickListener() {//modifica para todas las partidas
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String intentosLeidos = nuevosIntentos.getText().toString();
                        if (intentosLeidos.equals("")) {
                            Toast.makeText(MainActivity.this, "Debes introducir un número", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                calcularIntentos(intentosPartidas = Integer.parseInt(intentosLeidos));
                                intentos = Integer.parseInt(intentosLeidos);//soluciona poblema para siguientes partidas
                            } catch (NumberFormatException nfe) {
                                Toast.makeText(MainActivity.this, "Debes introducir un número", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
                builderIntentos.setNegativeButton("Partida actual", new DialogInterface.OnClickListener() {//modifica solo para la partida actual
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String intentosLeidos = nuevosIntentos.getText().toString();
                        if (intentosLeidos.equals("")) {
                            Toast.makeText(MainActivity.this, "Debes introducir un número", Toast.LENGTH_LONG).show();
                        } else {
                            try {
                                calcularIntentos(intentos = Integer.parseInt(intentosLeidos));
                            } catch (NumberFormatException nfe) {
                                Toast.makeText(MainActivity.this, "Debes introducir un número", Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
                AlertDialog dialogIntentos = builderIntentos.create();
                dialogIntentos.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }*/


    /**
     * Metodo para anadir al programa las palabras que ha elegido el usuario
     *
     * @param palabrasUsuario array de cadena de caracteres introducido
     *//*
    public void cargarPalabrasUsuario(String[] palabrasUsuario) {
        for (int i = 0; i < palabrasUsuario.length; i++) {
            palabras.add(palabrasUsuario[i]);
        }
    }*/

    /**
     * Metodo que muestra la palabra con la que se esta jugando en su estado actual
     */
    public void mostrarPalabra() {
        palabraSeleccionada.setText(p.mostrarPalabra());
    }

    /**
     * Metodo que sirve para mostrar el acierto en la palabra o restar intentos
     *
     * @param vista boton adivinar
     */
    public void resolver(View vista) {
        String cadena = p.resolver(letraElegida);
        if (cadena.equals("")) {//si no se ha elegido ninguna letra
            Toast.makeText(this, "No has introducido ninguna letra", Toast.LENGTH_LONG).show();
        } else {
            letraElegida.setText("");//se ayuda al usuario a que no tenga que borrar la letra para escribir otra

            if (!p.acertado()) {//solo se ejecutara si no se ha acertado letra
                Toast.makeText(this, "Has fallado", Toast.LENGTH_LONG).show();
                calcularIntentos(p.getIntentos());
            }

            //se muestra la palabra en su estado actual tras elegir letra
            mostrarPalabra();

            //se comprueba si se ha ganado o se ha perdido la partida
            comprobarPartida();
        }
    }

    /**
     * Metodo para comprobar si se ha ganado o si se ha perdido la partida
     */
    public void comprobarPartida() {
        if (p.getIntentos()==0) {
            botonResolver.setEnabled(false);//deshabilita el boton al perder
            mostrarDialogo("Has perdido");
        } else {
            if (p.comprobarPartida()) {
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
     * Metodo para jugar otra partida con una palabra aleatoria
     *
     * @param vista boton que ejecuta el evento
     */
    public void otraPartida(View vista) {
        if (!botonResolver.isEnabled()){
            botonResolver.setEnabled(true);//habilita el boton al empezar una partida si estaba deshabilitado
        }
        p.elegirPalabra();
        mostrarPalabra();
        calcularIntentos(p.getIntentos());
    }

}