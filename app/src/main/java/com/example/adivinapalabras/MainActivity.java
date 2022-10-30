package com.example.adivinapalabras;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private int intentos;
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
        palabras.add("clase");
        palabras.add("ventana");
        palabras.add("puerta");
        palabras.add("piloto");
        palabras.add("hola");

        //inicializacion vistas
        palabraSeleccionada = findViewById(R.id.palabra);
        intentosRestantes = findViewById(R.id.intentos);
        botonResolver = findViewById(R.id.adivinar);
        letraElegida = findViewById(R.id.letra);
        otraPalabra = findViewById(R.id.nuevo);

        //inicio primera partida
        elegirPalabra();
    }

    /**
     * Metodo para seleccionar una palabra de forma aleatoria e iniciar una partida
     */
    public void elegirPalabra() {
        int posicion = (int) (Math.random() * palabras.size());//posicion palabra aleatoria
        palabraActual = palabras.get(posicion).toCharArray();//array caracteres con la palabra
        posicionesAcertadas = new boolean[palabraActual.length];//array de booleanos con el tamaño
        Arrays.fill(posicionesAcertadas, false);//array booleanos inicado a false
        calcularIntentos(intentos = 5);//se inician los intentos
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
        if(letraElegida.getText().toString().equals("")){//si no se ha elegido ninguna letra
            Toast.makeText(this, "No has introducido ninguna letra", Toast.LENGTH_LONG).show();
        }else{
            //lectura de la letra
            String letraCadena = letraElegida.getText().toString();//la letra en el edit text, formato cadena
            char letra = letraCadena.charAt(0);//la letra elegida en formato char
            letraElegida.setText("");//se ayuda al usuario a que no tenga que borrar la letra para escribir otra

            //comprobacion de acierto o fallo
            boolean acertado = false;//se da por hecho que no va a acertar la letra
            for (int i = 0; i < palabraActual.length; i++) {
                if (letra == palabraActual[i]) {
                    posicionesAcertadas[i] = true;//se pone la posicion correspondiente a true
                    acertado = true;//se indica que se ha acertado la letra
                }
            }
            if (!acertado) {//solo se ejecutara si no se ha acertado letra
                calcularIntentos(--intentos);
                Toast.makeText(this, "Has fallado", Toast.LENGTH_LONG).show();
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
            Toast.makeText(this, "Has perdido", Toast.LENGTH_LONG).show();//todo cambiar a alertdialog
        } else {
            boolean[] comprobacionGanado = new boolean[posicionesAcertadas.length];
            Arrays.fill(comprobacionGanado, true);
            if (Arrays.equals(comprobacionGanado, posicionesAcertadas)) {
                Toast.makeText(this, "Has ganado", Toast.LENGTH_LONG).show();//todo cambiar a alertdialog
            }
        }
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