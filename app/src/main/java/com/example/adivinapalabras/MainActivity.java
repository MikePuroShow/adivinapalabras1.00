package com.example.adivinapalabras;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
        int posicion = (int) (Math.random()*palabras.size());//posicion palabra aleatoria
        palabraActual = palabras.get(posicion).toCharArray();//array caracteres con la palabra
        posicionesAcertadas = new boolean[palabraActual.length];//array de booleanos con el tamaño
        Arrays.fill(posicionesAcertadas,false);//array booleanos inicado a false
        intentos = 5;//intentos para la partida
        intentosRestantes.setText(String.valueOf(intentos));//se muestran los intentos
        mostrarPalabra();//se muestra la palabra seleccionada

    }

    /**
     * Metodo que muestra la palabra con la que se esta jugando en su estado actual
     */
    public void mostrarPalabra(){
        for (int i = 0; i < palabraActual.length; i++) {
            if (posicionesAcertadas[i]){
                palabraSeleccionada.setText(palabraSeleccionada.getText()+""+palabraActual[i]+" ");
            }else{
                palabraSeleccionada.setText(palabraSeleccionada.getText()+"_"+" ");
            }
        }
    }

}