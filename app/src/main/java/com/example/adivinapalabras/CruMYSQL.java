package com.example.adivinapalabras;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

public class CruMYSQL extends AppCompatActivity implements Serializable {
    Partida p;
    int position;
    private EditText nombre;
    private EditText descripcion;
    int opcion; //1 añadir, 2 eliminar, 3 actualizar, 4 buscar


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mysqllayout);
        nombre = findViewById(R.id.cajaTextonombremysql);
        descripcion = findViewById(R.id.cajaTextodefinicionmysql);
        Bundle datos = getIntent().getExtras(); //BUNDLE
        p = (Partida) datos.getSerializable("partida"); //RECOGER PARTIDA
        position = datos.getInt("posicion");
        if (p.getPalabras().size() == 0) {
            //Si no hay palabras.
        } else {
            nombre.setText(p.getPalabras().get(position).getNombrePalabra());
            descripcion.setText(p.getPalabras().get(position).getDescripcion());
            boolean booleano = datos.getBoolean("booleano");
            if (booleano) {
                nombre.setText("");
                descripcion.setText("");
            }
        }
    }

    public void anadirMYSQL(View vista) {
        opcion = 1;
        gestionarSQL("http://192.168.56.1/php/insertarpalabras.php", opcion);
    }

    public void eliminarMYSQL(View vista) {
        opcion = 2;
        gestionarSQL("http://192.168.56.1/php/eliminarpalabras.php", opcion);
    }

    public void buscarMYSQL(View vista) {
        opcion = 3;
        gestionarSQL("http://192.168.56.1/php/buscarpalabras.php", opcion);
    }

    public void actualizarMYSQL(View vista) {
        opcion = 4;
        gestionarSQL("http://192.168.56.1/php/actualizar.php", opcion);
    }

    private void gestionarSQL(final String urlWebService, int operacion) {


        class exportarAnonima extends AsyncTask<Void, Void, String> {
            String json;//BUSQUEDa
            int opcionOnPostExecute = 0;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                if (opcionOnPostExecute == 3) {
                    try {
                        JSONObject object = new JSONObject(json); //3 caso
                        descripcion.setText(object.getString("definicion"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                switch (operacion) {
                    case 1:
                        try {
                            URL url = new URL(urlWebService);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            url = new URL("http://192.168.56.1/php/insertarpalabras.php?palabra=" + nombre.getText().toString() + "&definicion=" + descripcion.getText().toString());
                            con = (HttpURLConnection) url.openConnection();
                            con.getInputStream();
                            con.disconnect();
                            return "Exportación realizada con éxito";

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;

                    case 2:
                        try {
                            URL url = new URL(urlWebService);
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            url = new URL("http://192.168.56.1/php/eliminarpalabras.php?palabra=" + nombre.getText().toString() + "&definicion=" + descripcion.getText().toString());
                            con = (HttpURLConnection) url.openConnection();
                            con.getInputStream();
                            con.disconnect();
                            return "Borrado realizado con éxito";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
                        try {
                            URL url = new URL("http://192.168.56.1/php/buscarpalabras.php?palabra=" + nombre.getText().toString() + "&definicion=" + descripcion.getText().toString());
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                            json = bufferedReader.readLine();
                            opcionOnPostExecute = 3;
                            con.disconnect();
                            return "Busqueda realizada con éxito";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
                        try {
                            URL url = new URL("http://192.168.56.1/php/actualizar.php?palabra=" + nombre.getText().toString() + "&definicion=" + descripcion.getText().toString());
                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                            con.getInputStream();
                            //con.disconnect();
                            return "Actualizacion realizada con éxito";
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                }

                return null;
            }
        }
        exportarAnonima ex = new exportarAnonima();
        ex.execute();
    }
}
