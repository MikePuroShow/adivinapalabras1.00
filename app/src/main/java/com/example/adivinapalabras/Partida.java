package com.example.adivinapalabras;

import android.content.Context;
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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class Partida extends AppCompatActivity {

    private int intentosPartidas = 5;//variable que gestiona los intentos con los que se iniciara la partida
    private int intentos;//variable que gestiona los intentos actuales de la partida
    private ArrayList<String> palabras;
    private TextView palabraSeleccionada;
    private TextView intentosRestantes;
    private Button botonResolver;
    private EditText letraElegida;
    private Button otraPalabra;
    private char[] palabraActual;
    private boolean[] posicionesAcertadas;
    Context contexto;

    public Partida(TextView palabraSeleccionada, TextView intentosRestantes, Button botonResolver, EditText letraElegida, Context contexto) {
        this.palabraSeleccionada = palabraSeleccionada;
        this.intentosRestantes = intentosRestantes;
        this.botonResolver = botonResolver;
        this.letraElegida = letraElegida;
        this.contexto = contexto;
        palabras = new ArrayList<String>();
        cargarPalabras();
        //cargarPalabrasXML();
        elegirPalabra();
    }

    /**
     * Metodo para cargar palabras desde java al programa
     */
    public void cargarPalabras() {
        palabras.add("clase");
        palabras.add("ventana");
        palabras.add("puerta");
        palabras.add("piloto");
        palabras.add("hola");
    }

    /**
     * Metodo para cargar palabras desde xml al programa
     */
    public void cargarPalabrasXML() {
        String[] a = getResources().getStringArray(R.array.palabras);
        for (int i = 0; i < a.length; i++) {
            palabras.add(a[i]);
        }
    }

    /**
     * Metodo para anadir al programa las palabras que ha elegido el usuario
     *
     * @param palabrasUsuario array de cadena de caracteres introducido
     */
    public void cargarPalabrasUsuario(String[] palabrasUsuario) {
        for (int i = 0; i < palabrasUsuario.length; i++) {
            palabras.add(palabrasUsuario[i]);
        }
    }

    /**
     * Metodo para seleccionar una palabra de forma aleatoria e iniciar una partida
     */
    public void elegirPalabra() {
        int posicion = (int) (Math.random() * palabras.size());//posicion palabra aleatoria
        palabraActual = palabras.get(posicion).toCharArray();//array caracteres con la palabra
        posicionesAcertadas = new boolean[palabraActual.length];//array de booleanos con el tamaño
        Arrays.fill(posicionesAcertadas, false);//array booleanos inicado a false
        intentos = intentosPartidas;//actualiza el valor de la partida que se va a jugar
        calcularIntentos(intentosPartidas);//se inician los intentos
        mostrarPalabra();//se muestra la palabra seleccionada

    }

    /**
     * Metodo que muestra la palabra con la que se esta jugando en su estado actual
     */
    public void mostrarPalabra() {
        palabraSeleccionada.setText("");//se limpia el texto para que funcione correctamente la concatenacion
        for (int i = 0; i < palabraActual.length; i++) {
            if (posicionesAcertadas[i]) {
                palabraSeleccionada.setText(palabraSeleccionada.getText() + "" + palabraActual[i] + " ");
            } else {
                palabraSeleccionada.setText(palabraSeleccionada.getText() + "_" + " ");
            }
        }
    }

    /**
     * Metodo que sirve para mostrar el acierto en la palabra o restar intentos
     *
     * @param vista boton adivinar
     */
    public void resolver(View vista) {
        if (letraElegida.getText().toString().equals("")) {//si no se ha elegido ninguna letra
            Toast.makeText(contexto, "No has introducido ninguna letra", Toast.LENGTH_LONG).show();
        } else {
            //lectura de la letra
            String letraCadena = letraElegida.getText().toString();//la letra en el edit text, formato cadena
            char letra = letraCadena.charAt(0);//la letra elegida en formato char
            letraElegida.setText("");//se ayuda al usuario a que no tenga que borrar la letra para escribir otra

            //comprobacion de acierto o fallo
            boolean acertado = false;//se da por hecho que no va a acertar la letra
            for (int i = 0; i < palabraActual.length; i++) {
                if (Character.toLowerCase(letra) == Character.toLowerCase(palabraActual[i])) {//se hace la comparacion no case sensitive
                    posicionesAcertadas[i] = true;//se pone la posicion correspondiente a true
                    acertado = true;//se indica que se ha acertado la letra
                }
            }
            if (!acertado) {//solo se ejecutara si no se ha acertado letra
                if (intentos != 0) {
                    calcularIntentos(--intentos);//resta un intento
                    Toast.makeText(contexto, "Has fallado", Toast.LENGTH_LONG).show();
                }
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
        if (intentos == 0) {
            botonResolver.setEnabled(false);//deshabilita el boton al perder
            mostrarDialogo("Has perdido");
        } else {
            boolean[] comprobacionGanado = new boolean[posicionesAcertadas.length];
            Arrays.fill(comprobacionGanado, true);
            if (Arrays.equals(comprobacionGanado, posicionesAcertadas)) {
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
        AlertDialog.Builder builder = new AlertDialog.Builder(contexto);
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
        if (!botonResolver.isEnabled())
            botonResolver.setEnabled(true);//habilita el boton al empezar una partida si estaba deshabilitado
        elegirPalabra();
    }

}
