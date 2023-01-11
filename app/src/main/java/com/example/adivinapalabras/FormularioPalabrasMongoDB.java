package com.example.adivinapalabras;

import static com.mongodb.client.model.Filters.eq;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.bson.Document;
import org.bson.conversions.Bson;

import java.io.Serializable;

public class FormularioPalabrasMongoDB extends AppCompatActivity implements Serializable {


    Partida p;
    int position;
    private EditText nombre;
    private EditText descripcion;
    int opcion; //1 añadir, 2 eliminar, 3 actualizar, 4 buscar
    Button botonAnadirMongo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mongodbformulario);
        botonAnadirMongo = findViewById(R.id.btnInsertarMYSQL);
        nombre = findViewById(R.id.nombrePalabraMYSQL);
        descripcion = findViewById(R.id.descripcionmysql);
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
    public void hacerToastPositivos(String clave){
        switch (clave){
            case "inserccion":
                Toast.makeText(this, "INSERCCION REALIZADA CON EXITO", Toast.LENGTH_LONG).show();
                break;
            case "borrado":
                Toast.makeText(this, "BORRADO REALIZADO CON EXITO", Toast.LENGTH_LONG).show();
                break;
            case "reseteo":
                Toast.makeText(this, "DESCRIPCION ACTUALIZADA  CON EXITO", Toast.LENGTH_LONG).show();
                break;
            case"busqueda":
                Toast.makeText(this, "LA PALABRA SE ENCUENTRA EN LA BD", Toast.LENGTH_LONG).show();
                break;
        }
    }
    public void hacerToastNegativos(String clave){
        switch (clave){
            case "inserccion":
                Toast.makeText(this, "INSERCCION NO PUDO REALIZARSE CORRECTAMENTE", Toast.LENGTH_LONG).show();
                break;
            case "borrado":
                Toast.makeText(this, "BORRADO NO PUDO REALIZARSE CORRECTAMENTE", Toast.LENGTH_LONG).show();
                break;
            case "reseteo":
                Toast.makeText(this, "NO PUDO ACTUALIZARSE LA DESCRIPCION CORRECTAMENTE..", Toast.LENGTH_LONG).show();
                break;
            case"busqueda":
                Toast.makeText(this, "LA PALABRA NO SE ENCUENTRA EN LA BD", Toast.LENGTH_LONG).show();
                break;
        }
    }

    public void GestionMongoDB(View vista){
       Button botonAnadirMongo = (Button) vista;
       String simboloMongo = botonAnadirMongo.getText().toString();
        System.out.println(simboloMongo);
        if(simboloMongo.equals("+")){
            opcion = 1;
           // Toast.makeText(this, "INSERCCION REALIZADA CON EXITO", Toast.LENGTH_LONG).show();
        }
        if(simboloMongo.equals("-")){
            opcion = 2;
            System.out.println(opcion);
        }
        if(simboloMongo.equals("R")){
            opcion = 3;
            System.out.println(opcion);
        }
        if(simboloMongo.equals("B")){
            opcion = 4;
            System.out.println(opcion);
        }
        // mongod --port 27017 --dbpath C:\MongoDB\data\db --bind_ip_all
        class GetMONGO extends AsyncTask<Void, Void, String> {
            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            //Document doc;
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String jsonStr) {
                super.onPostExecute(jsonStr);
            }

            //in this method we are fetching the json string
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            protected String doInBackground(Void... voids) {
                switch (opcion){
                    case 1:
                    try {
                        String uri = "mongodb://172.20.10.2:27017";
                        MongoClient mongoClient = MongoClients.create(uri);
                        MongoDatabase database = mongoClient.getDatabase("palabras");
                        MongoCollection<Document> collection = database.getCollection("palabras1");
                        String nombrePalabraInsertar = nombre.getText().toString();
                        String descripcionPalabraAInsertar = descripcion.getText().toString();

                        Document e = new Document();
                        e.put("nombre",nombrePalabraInsertar);
                        e.put("descripcion",descripcionPalabraAInsertar);
                        collection.insertOne(e);
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                Toast toast = Toast.makeText(getApplicationContext(), "Insercción realizada con éxito", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                        return uri;
                    } catch (Exception e) {
                        e.printStackTrace();
                        e.getMessage();
                        return null;
                    }
                    case 2:
                        try {
                            String uri = "mongodb://172.20.10.2:27017";;
                            MongoClient mongoClient = MongoClients.create(uri);
                            MongoDatabase database = mongoClient.getDatabase("palabras");
                            MongoCollection<Document> collection = database.getCollection("palabras1");
                            String nombrePalabraInsertar = nombre.getText().toString();
                            String descripcionPalabraAInsertar = descripcion.getText().toString();
                            DeleteResult result = collection.deleteOne(eq("nombre",nombrePalabraInsertar));
                            if (result.getDeletedCount()==0){
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast toast = Toast.makeText(getApplicationContext(), "Borrado no se pudo realizar correctamente...", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }else{
                                new Handler(Looper.getMainLooper()).post(new Runnable() {
                                    @Override
                                    public void run() {
                                        nombre.setText("");
                                        descripcion.setText("");
                                        Toast toast = Toast.makeText(getApplicationContext(), "Borrado realizado con éxito", Toast.LENGTH_SHORT);
                                        toast.show();
                                    }
                                });
                            }
                    return null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            e.getMessage();
                            return null;
                        }
                    case 3:
                        try {
                            String uri = "mongodb://172.20.10.2:27017";;
                            MongoClient mongoClient = MongoClients.create(uri);
                            MongoDatabase database = mongoClient.getDatabase("palabras");
                            MongoCollection<Document> collection = database.getCollection("palabras1");
                            String descripcionPalabraAInsertar = descripcion.getText().toString();
                            String nombreActualizar = nombre.getText().toString();
                            Document query = new Document().append("nombre",  nombreActualizar);
                            Bson updates = Updates.combine(
                                    Updates.set("descripcion", descripcionPalabraAInsertar)
                                    );
                            UpdateOptions options = new UpdateOptions().upsert(true);
                            try {
                                UpdateResult result = collection.updateOne(query, updates, options);
                                if(result.getModifiedCount()==0){
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Actualización no se pudo hacer correctamente. . .", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    });
                                }else{
                                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast toast = Toast.makeText(getApplicationContext(), "Actualización hecha correctamente. . .", Toast.LENGTH_SHORT);
                                            toast.show();
                                        }
                                    });
                                }
                               // System.out.println("Modified document count: " + result.getModifiedCount());
                                //System.out.println("Upserted id: " + result.getUpsertedId()); // only contains a value when an upsert is performed
                            } catch (MongoException me) {
                                System.err.println("Unable to update due to an error: " + me);
                            }

                            return uri;
                        } catch (Exception e) {
                            e.printStackTrace();
                            e.getMessage();
                            return null;
                        }
                    case 4:
                        String uri = "mongodb://172.20.10.2:27017";
                        MongoClient mongoClient = MongoClients.create(uri);
                        MongoDatabase database = mongoClient.getDatabase("palabras");
                        MongoCollection<Document> collection = database.getCollection("palabras1");
                        String nombreActualizar = nombre.getText().toString();
                        BasicDBObject whereQuery = new BasicDBObject();
                        whereQuery.put("nombre", nombreActualizar);
                        FindIterable<Document> cursor3 = collection.find(whereQuery);
                        if (cursor3.cursor().hasNext()) {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Esta palabra se encuentra en la base de datos", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        } else {
                            new Handler(Looper.getMainLooper()).post(new Runnable() {
                                @Override
                                public void run() {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Esta palabra no está en la base de datos", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                }


                return null;
            }
        }


        //creating asynctask object and executing it
        GetMONGO getMongo = new GetMONGO();
        getMongo.execute();
    }
}
//los Ficheros php esta en medio en la BD y el cliente.
