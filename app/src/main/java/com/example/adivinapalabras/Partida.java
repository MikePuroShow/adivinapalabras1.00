package com.example.adivinapalabras;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Partida {

    private int intentos;//variable que gestiona los intentos actuales de la partida
    private ArrayList<String> palabras;
    private char[] palabraActual;
    private boolean[] posicionesAcertadas;
    private char letra;

    public Partida() {
        //inicializacion arraylist palabras
        palabras = new ArrayList<String>();
        cargarPalabras();

        //inicio primera partida
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
     * Metodo para seleccionar una palabra de forma aleatoria e iniciar una partida
     */
    public void elegirPalabra() {
        int posicion = (int) (Math.random() * palabras.size());//posicion palabra aleatoria
        palabraActual = palabras.get(posicion).toCharArray();//array caracteres con la palabra
        posicionesAcertadas = new boolean[palabraActual.length];//array de booleanos con el tamaño
        Arrays.fill(posicionesAcertadas, false);//array booleanos inicado a false
        intentos = palabraActual.length/2;//actualiza el valor de la partida que se va a jugar
        mostrarPalabra();//se muestra la palabra seleccionada
    }


    /**
     * Metodo que devuelve una cadena con el estado actual de la palabra
     */
    public String mostrarPalabra() {
        String palabraMostrar = "";
        for (int i = 0; i < palabraActual.length; i++) {
            if (posicionesAcertadas[i]) {
                palabraMostrar += palabraActual[i] + " ";
            } else {
                palabraMostrar += "_" + " ";
            }
        }
        return palabraMostrar;
    }

    public int getIntentos() {
        return intentos;
    }

    /**
     * Metodo que sirve para mostrar el acierto en la palabra o restar intentos
     */
    public String resolver(EditText letraElegida) {
        String cadenaRetorno = "";
        if (letraElegida.getText().toString().equals("")) {//si no se ha elegido ninguna letra
        } else {
            //lectura de la letra
            cadenaRetorno = letraElegida.getText().toString();//la letra en el edit text, formato cadena
            letra = cadenaRetorno.charAt(0);//la letra elegida en formato char

            if(!acertado())intentos--;

            //se comprueba si se ha ganado o se ha perdido la partida
            comprobarPartida();
        }
        return cadenaRetorno;
    }

    /**
     * Metodo que pone las posiciones acertadas a true
     * @return retorna si se ha acertado o si se ha fallado
     */
    public boolean acertado(){
        //comprobacion de acierto o fallo
        boolean acertado = false;//se da por hecho que no va a acertar la letra
        for (int i = 0; i < palabraActual.length; i++) {
            if (Character.toLowerCase(letra) == Character.toLowerCase(palabraActual[i])) {//se hace la comparacion no case sensitive
                posicionesAcertadas[i] = true;//se pone la posicion correspondiente a true
                acertado = true;//se indica que se ha acertado la letra
            }
        }
        return acertado;
    }


    /**
     * Metodo para comprobar si se ha ganado o si se ha perdido la partida
     */
    public boolean comprobarPartida() {
        boolean ganado = false;
        if (intentos == 0) {
        } else {
            boolean[] comprobacionGanado = new boolean[posicionesAcertadas.length];
            Arrays.fill(comprobacionGanado, true);
            if (Arrays.equals(comprobacionGanado, posicionesAcertadas)) {
                ganado = true;
            }
        }
        return ganado;
    }

}
