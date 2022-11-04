package com.example.adivinapalabras;

import android.content.Context;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Partida {

    private int intentos;//variable que gestiona los intentos actuales de la partida
    private ArrayList<String> palabras;
    private char[] palabraActual;
    private boolean[] posicionesAcertadas;
    private char letra;
    private int posicion;//posicion en el array list de la palabra con la que se esta jugando
    private int dificultad = 0;//dificultad de la partida, -1 para dificil, 0 para normal y 1 para facil

    public Partida() {
        //inicializacion arraylist palabras
        palabras = new ArrayList<String>();
        cargarPalabras();

        //inicio primera partida
        elegirPalabraPartida();
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
     * Metodo para cargar palabras desde xml al programa
     */
    public void cargarPalabrasXML(Context contexto) {
        String[] arrayPalabrasXML = contexto.getResources().getStringArray(R.array.palabras);
        for (int i = 0; i < arrayPalabrasXML.length; i++) {
            palabras.add(arrayPalabrasXML[i]);
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
    public void elegirPalabraPartida() {
        posicion = (int) (Math.random() * palabras.size());//posicion palabra aleatoria
        palabraActual = palabras.get(posicion).toCharArray();//array caracteres con la palabra
        posicionesAcertadas = new boolean[palabraActual.length];//array de booleanos con el tamaño
        Arrays.fill(posicionesAcertadas, false);//array booleanos inicado a false, si hay persistencia no es necesario este metodo
        descubrirLetras();
        intentos = (palabraActual.length/2)+dificultad;//actualiza el valor de la partida que se va a jugar
        mostrarPalabraPartida();//se muestra la palabra seleccionada
    }


    /**
     * Metodo que devuelve una cadena con el estado actual de la palabra
     */
    public String mostrarPalabraPartida() {
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



    /**
     * Metodo que sirve para mostrar el acierto en la palabra o restar intentos
     */
    public String resolverPartida(EditText letraElegida) {
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

    /**
     * Metodo que descubre letras de la palabra, en funcion de la dificultad de la partida
     * facil: 1/3 de las letras + 1
     * Normal: 1/3 de las letras
     * Dificil: 1/3 de las letras -1
     */
    public void descubrirLetras(){
        int cantidadLetras = (palabraActual.length/3)+dificultad;
        int i = 0;
        while(i<cantidadLetras){
            Random aleatorio = new Random();
            int numAleatorio = aleatorio.nextInt(palabraActual.length);
            System.out.println(aleatorio);
            if(!posicionesAcertadas[numAleatorio]){
                posicionesAcertadas[numAleatorio]=true;
                i++;
            }
        }
    }

    //getters y setters
    public int getIntentos() {
        return intentos;
    }

    public void setIntentos(int intentos) {
        this.intentos = intentos;
    }

    public boolean[] getPosicionesAcertadas() {
        return posicionesAcertadas;
    }

    public void setPosicionesAcertadas(boolean[] posicionesAcertadas) {
        this.posicionesAcertadas = posicionesAcertadas;
    }

    public char[] getPalabraActual() {
        return palabraActual;
    }

    public void setPalabraActual(char[] palabraActual) {
        this.palabraActual = palabraActual;
    }

    public void setDificultad(int dificultad) {
        this.dificultad = dificultad;
    }

    public int getDificultad() {
        return dificultad;
    }

}
