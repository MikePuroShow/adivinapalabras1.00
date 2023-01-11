package com.example.adivinapalabras;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements Serializable {

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
    private int intentosDisponiblesEmpezar;
    private ListView listViewMYSQL;

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

        Intent i = getIntent();
        partida = (Partida) i.getSerializableExtra("partida");
        if (partida == null) {

            cargaInicial(); //Se le añaden descripciones y palabras al array del MAIN ACTIVITY
            //se crea la partida
            partida = new Partida(palabrasInicio);

            actualizarPalabras(); //Contador de palabras

            //se realizan acciones de la partida
            mostrarPalabra(); //se muestran guiones y algunas letras
            mostrarDescripcion(); //descripcion
            calcularIntentos(partida.getIntentos()); //intentos.
            intentosDisponiblesEmpezar = partida.getIntentos();//INTENTOS INICIALES
        } else {
            try {
                actualizarPalabras();
                mostrarPalabra();
                mostrarDescripcion();
                calcularIntentos(partida.getIntentos());
            } catch (IndexOutOfBoundsException e) {
                otraPartida(null);
            }

        }


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
                Toast.makeText(this, "La palabra actual es: " + partida.getPalabras().get(partida.getPosicion()).getNombrePalabra(), Toast.LENGTH_LONG).show();
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
            case R.id.menuExportarSQL:
                partida.exportarPalabrasSQL(this);
                Toast.makeText(this, "Palabras exportadas a la base de datos", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuImportarSQL:
                partida.importarPalabrasSQL(this);
                actualizarPalabras();
                Toast.makeText(this, "Palabras importadas desde la base de datos", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.exportarobjetos:
                partida.exportarObjetos(this);
                Toast.makeText(this, "Palabras  exportadas al fichero de objetos", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.importarObjetos:
                partida.importarObjetos(this);
                actualizarPalabras();
                Toast.makeText(this, "Palabras  importadas desde el fichero de objetos", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuSalirAplicacion:
                System.exit(0);
                return true;
            case R.id.importarmongo:
                partida.importarMongo(this);
                try {
                    Thread.sleep(1000); //SLEEP porque lo jace tan rapido que no
                                                //le da tiempo a actualizar el numero palabras
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                actualizarPalabras();
                Toast.makeText(this, "Palabras  importadas desde MONGODB", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.importarMYSQL:
                getJSON("http://192.168.56.1/php/leerpalabras.php");
                return true;
            case R.id.exportarAmySQL:
                exportarPalabrasMYSQLusandoPHP("http://192.168.56.1/php/insertarpalabras.php");
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
        if (partida.getPalabras().size() == 0) { //PARA EL BORRAR TO DO
            Toast.makeText(this, "No hay palabras para jugar", Toast.LENGTH_SHORT).show();
        } else {
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
    }

    /**
     * Metodo para comprobar si se ha ganado o si se ha perdido la partida
     */
    public void comprobar() {
        if (partida.getIntentos() == 0) {
            botonResolver.setEnabled(false);//deshabilita el boton al perder
            mostrarDialogo("Has perdido");
        } else {
            if (partida.comprobarPartida()) {
                botonResolver.setEnabled(false);//deshabilita el boton al perder
                mostrarDialogo("Has ganado,realizando: " +(intentosDisponiblesEmpezar-partida.getIntentos())+" fallos");
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
        intentosRestantes.setText("Intentos "+ String.valueOf(intentosActualizados));
    }

    /**
     * Metodo para actualizar las palabras disponibles en la partida
     */
    public void actualizarPalabras() {
        palabrasDisponibles.setText("Palabras disponibles: " + partida.getPalabras().size());
    }

    public void mostrarDescripcion() {
        descripcionPalabra.setText(partida.getPalabras().get(partida.getPosicion()).getDescripcion());
    }

    /**
     * Metodo para jugar otra partida con una palabra aleatoria
     *
     * @param vista boton que ejecuta el evento
     */
    public void otraPartida(View vista) {
        if (!botonResolver.isEnabled()) {
            botonResolver.setEnabled(true);//habilita el boton al empezar una partida si estaba deshabilitado
        }
        if (partida.getPalabras().size() == 0) {
            Toast.makeText(this, "NO HAY PALABRAS PARA JUGAR", Toast.LENGTH_LONG);
            palabraSeleccionada.setText("");
        } else {
            partida.elegirPalabraPartida();
            mostrarPalabra();
            mostrarDescripcion();
            calcularIntentos(partida.getIntentos());
        }
    }


    /**
     * Metodo para anadir palabras a la partida
     */
    public void anadirPalabras() {
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
                    partida.cargarPalabrasUsuario(new Palabra(palabrasLeida, palabraDescripcion));
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
    public void mostrarPalabrasPartida() {
        Intent i = new Intent(this, mostrarPalabras.class);

        //i.putExtra("palabras",partida.getPalabras());
        i.putExtra("partida", partida);
            startActivity(i);




    }

    public void cargaInicial() {
        String[] nombrePalabras = {"pantalla", "cielo", "ordenador"};
        String[] descripcion = {"Dispositivo de salida que representa visualmente la información.", "Espacio en el que se mueven los astros.", "Maquina encargada de procesar datos."};
        for (int i = 0; i < nombrePalabras.length; i++) {
            palabrasInicio.add(new Palabra(nombrePalabras[i], descripcion[i]));
        }
    }



    private void getJSON(final String urlWebService) {

        class GetJSON extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                try {
                    loadIntoListView(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                    con.disconnect();
                    return sb.toString().trim();
                } catch (Exception e) {
                    return null;
                }
            }
        }
        GetJSON getJSON = new GetJSON();
        getJSON.execute();
    }

    private void loadIntoListView(String json) throws JSONException {
        JSONArray jsonArray = new JSONArray(json);
        String[] palabras = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject obj = jsonArray.getJSONObject(i);
            partida.getPalabras().add(new Palabra(obj.getString("palabra"), obj.getString("definicion")));
        }
        actualizarPalabras();
        //   ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, heroes);
        //  listView.setAdapter(arrayAdapter);
    }
    private void exportarPalabrasMYSQLusandoPHP(final String urlWebService) {

        class exportarAnonima extends AsyncTask<Void, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();

            }

            @Override
            protected String doInBackground(Void... voids) {
                try {
                    URL url = new URL(urlWebService);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    ArrayList<Palabra> palabrasPHP = new ArrayList<Palabra>();
                    palabrasPHP= partida.getPalabras();
                    for (int i = 0; i < palabrasPHP.size(); i++) {
                        url = new URL("http://192.168.56.1/php/insertarpalabras.php?palabra="+palabrasPHP.get(i).getNombrePalabra().trim()+"&definicion="+palabrasPHP.get(i).getDescripcion().trim());
                        con = (HttpURLConnection) url.openConnection();
                        con.getInputStream();
                    }
                    con.disconnect();
                    return "Exportación realizada con éxito";
                } catch (Exception e) {
                    return null;
                }
            }
        }
        exportarAnonima ex = new exportarAnonima();
        ex.execute();
    }

}