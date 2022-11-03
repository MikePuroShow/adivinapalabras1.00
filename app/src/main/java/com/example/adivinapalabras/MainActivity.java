package com.example.adivinapalabras;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private TextView palabraSeleccionada;
    private TextView intentosRestantes;
    private Button botonResolver;
    private EditText letraElegida;
    private Button otraPalabra;
    private Partida partida;
    private boolean[] palabrasAcertadas;
    private char[] palabra;

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

        partida = new Partida();



        mostrarPalabra();//no es necesario si hay persistencia
        calcularIntentos(partida.getIntentos());//no es necesario si hay persistencia
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor miEditor = datos.edit();

        miEditor.putInt("intentos", partida.getIntentos());
        for (int i = 0; i < partida.getPosicionesAcertadas().length; i++) {
            miEditor.putBoolean("letrasAcertadas" + i, partida.getPosicionesAcertadas()[i]);
        }

        for (int i = 0; i < partida.getPalabraActual().length; i++) {
            miEditor.putString("letrasAcertadasChar" + i, String.valueOf(partida.getPalabraActual()[i]));
        }

        miEditor.putInt("tamano", partida.getPosicionesAcertadas().length);

        miEditor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);

        int tamano = datos.getInt("tamano", partida.getPalabraActual().length);

        palabrasAcertadas = new boolean[tamano];
        palabra = new char[tamano];


        //Se restaura el array de booleanos
        for (int i = 0; i < tamano; i++) {
            boolean resultado = datos.getBoolean("letrasAcertadas" + i, false);
            palabrasAcertadas[i] = resultado;
        }
        partida.setPosicionesAcertadas(palabrasAcertadas);

        //Se restaura el array de caracteres
        for (int i = 0; i < tamano; i++) {
            char letraLeida = datos.getString("letrasAcertadasChar" + i, "_").charAt(0);
            palabra[i] = letraLeida;
        }
        partida.setPalabraActual(palabra);

        //Se restauran los intentos
        int intentos = datos.getInt("intentos", partida.getIntentos());
        partida.setIntentos(intentos);
        calcularIntentos(intentos);//es necesario para el funcionamiento

        mostrarPalabra();
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
        calcularIntentos(partida.getIntentos());
    }

}