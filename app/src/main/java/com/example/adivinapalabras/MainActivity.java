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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //inicializacion arraylist palabras
        palabras = new ArrayList<String>();
        cargarPalabras();

        //inicializacion vistas
        palabraSeleccionada = findViewById(R.id.palabra);
        intentosRestantes = findViewById(R.id.intentos);
        botonResolver = findViewById(R.id.adivinar);
        letraElegida = findViewById(R.id.letra);
        otraPalabra = findViewById(R.id.nuevo);

        //inicio primera partida
        elegirPalabra();
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
                            calcularIntentos(intentosPartidas = Integer.parseInt(intentosLeidos));
                            intentos = Integer.parseInt(intentosLeidos);//soluciona poblema para siguientes partidas
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
                            calcularIntentos(intentos = Integer.parseInt(intentosLeidos));
                        }
                    }
                });
                AlertDialog dialogIntentos = builderIntentos.create();
                dialogIntentos.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Metodo para cargar palabras desde java al programa
     */
    public void cargarPalabras() {
        palabras.add("clase");/*
        palabras.add("ventana");
        palabras.add("puerta");
        palabras.add("piloto");
        palabras.add("hola");*/
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
            Toast.makeText(this, "No has introducido ninguna letra", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this, "Has fallado", Toast.LENGTH_LONG).show();
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
            mostrarDialogo("Has perdido");
        } else {
            boolean[] comprobacionGanado = new boolean[posicionesAcertadas.length];
            Arrays.fill(comprobacionGanado, true);
            if (Arrays.equals(comprobacionGanado, posicionesAcertadas)) {
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
        elegirPalabra();
    }

}