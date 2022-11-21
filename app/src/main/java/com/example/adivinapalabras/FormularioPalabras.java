package com.example.adivinapalabras;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;

public class FormularioPalabras extends AppCompatActivity implements Serializable {
    EditText nombre;
    EditText descripcion;
    Partida p;
    int position = 0;
    baseDatosHelper mdHelper;
    String nombrevariable;
    String descripcionvariable;
    SQLiteDatabase db;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.formulario);
        nombre = findViewById(R.id.nombrePalabra);
        descripcion = findViewById(R.id.descripcionText);
        /**RECOGER PARITDA DE LA OTRA ACTIVIDAD**/
        Bundle datos = getIntent().getExtras(); //BUNDLE
            p = (Partida) datos.getSerializable("partida"); //RECOGER PARTIDA
            position = datos.getInt("posicion");
            if(p.getPalabras().size()==0){
                mdHelper = new baseDatosHelper(this);
            }else{
                nombre.setText(p.getPalabras().get(position).getNombrePalabra());
                descripcion.setText(p.getPalabras().get(position).getDescripcion());
                boolean booleano = datos.getBoolean("booleano");
                if (booleano) {
                    nombre.setText("");
                    descripcion.setText("");
                }
                mdHelper = new baseDatosHelper(this);

            }


        }




    public void borrar(View vista) {
        nombrevariable = nombre.getText().toString();
        for (int i = 0; i < p.getPalabras().size(); i++) {
            if (p.getPalabras().get(i).getNombrePalabra().equals(nombrevariable)) {
                p.getPalabras().remove(i);
                Toast.makeText(this, "La palabra: " + nombrevariable + " ha sido eliminada del array", Toast.LENGTH_SHORT).show();
            }
            nombre.setText("");
            descripcion.setText("");
        }
    }

    public void anadir(View vista) {
        nombrevariable = nombre.getText().toString();
        descripcionvariable = descripcion.getText().toString();
        if (nombrevariable.equals("")) {
            Toast.makeText(this, "INTRODUCE UNA PALABRA", Toast.LENGTH_LONG).show();
        } else {
            if (descripcionvariable.equals("")) {
                p.getPalabras().add(new Palabra(nombrevariable, "SIN DESCRIPCION"));
                Toast.makeText(this, "INSERCCION REALIZADA CON EXITO", Toast.LENGTH_LONG).show();
                nombre.setText("");
                descripcion.setText("");
            } else {
                p.getPalabras().add(new Palabra(nombrevariable, descripcionvariable));
                Toast.makeText(this, "INSERCCION REALIZADA CON EXITO", Toast.LENGTH_LONG).show();
            }

        }

    }

    public void volver(View vista) {
        Intent i = new Intent(this, mostrarPalabras.class);
        i.putExtra("partida", p);
        startActivity(i);
    }

    public void resetear(View vista) {
        nombrevariable = nombre.getText().toString();
        descripcionvariable = descripcion.getText().toString();
        if (nombrevariable.equals("")) {
            Toast.makeText(this, "Introduce una palabra", Toast.LENGTH_SHORT).show();
        } else {
            nombre.setText(nombrevariable);
            descripcion.setText(descripcionvariable);
            //FUNCIONAL
            p.getPalabras().get(position).setNombrePalabra(nombrevariable);
            p.getPalabras().get(position).setDescripcion(descripcionvariable);
            Toast.makeText(this, "CAMBIO REALIZADO CON ÉXITO", Toast.LENGTH_SHORT).show();
        }

    }


//SQ LITE
    public void insertarSQLIte(View vista) {
        db = mdHelper.getWritableDatabase();
// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        //values.put(estructuraBaseDeDatos.ID_PALABRA, );
        values.put(estructuraBaseDeDatos.Nombre_Palabra, nombre.getText().toString());
        values.put(estructuraBaseDeDatos.Descripcion_Palabra, descripcion.getText().toString());

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(estructuraBaseDeDatos.TABLE_NAME, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "INSERTADO CORRECTAMENTE " + newRowId, Toast.LENGTH_LONG).show();
            nombre.setText("");
            descripcion.setText("");
        } else {
            Toast.makeText(this, "NO SE A INSERTADO CORRECTAMENTE " + newRowId, Toast.LENGTH_LONG).show();
        }

    }

    public void buscarSQLITE(View vista) {
        if (nombre.getText().toString().equals("")) {
            Toast.makeText(this, "Introduce una palabra para buscar", Toast.LENGTH_LONG).show();
        } else {
             db = mdHelper.getReadableDatabase();
// Define a projection that specifies which columns from the database
// you will actually use after this query.
            String[] projection = {
                    estructuraBaseDeDatos.Descripcion_Palabra //SELECT
            };
// Filter results WHERE "title" = 'My Title'
            String selection = estructuraBaseDeDatos.Nombre_Palabra + " = ?"; //WHERE
            String[] selectionArgs = {nombre.getText().toString()}; // = A
// How you want the results sorted in the resulting Cursor
            //   String sortOrder =
            //  FeedEntry.COLUMN_NAME_SUBTITLE + " DESC";
            Cursor cursor = db.query(
                    estructuraBaseDeDatos.TABLE_NAME,   // The table to query
                    projection,             // The array of columns to return (pass null to get all)
                    selection,              // The columns for the WHERE clause
                    selectionArgs,          // The values for the WHERE clause
                    null,                   // don't group the rows
                    null,                   // don't filter by row groups
                    null              // The sort order
            );
            cursor.moveToFirst();
            try {
                descripcion.setText(cursor.getString(0));
            } catch (CursorIndexOutOfBoundsException ex) {
                Toast.makeText(this, "Esa palabra no se encuentra en la base de datos", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void eliminarSQLite(View vista) {
      SQLiteDatabase  db = mdHelper.getWritableDatabase();
        // Define 'where' part of query.
        String selection = estructuraBaseDeDatos.Nombre_Palabra + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {nombre.getText().toString()};
// Issue SQL statement.
        int deletedRows = db.delete(estructuraBaseDeDatos.TABLE_NAME, selection, selectionArgs);
        if(deletedRows>0){
            Toast.makeText(this,"Registro borrado correctamente", Toast.LENGTH_LONG).show();
            nombre.setText("");
            descripcion.setText("");
        }else{
            Toast.makeText(this,"Esa palabra no se encuentra en la base de datos", Toast.LENGTH_LONG).show();
        }
       // El valor que se muestra del método delete () indica el número de filas que se borraron de la
        //base de datos.
    }
    public void resetearSQlite(View vista){
        SQLiteDatabase db = mdHelper.getWritableDatabase();
// New value for one column
        ContentValues values = new ContentValues();
        values.put(estructuraBaseDeDatos.Descripcion_Palabra, descripcion.getText().toString());
        descripcionvariable=descripcion.getText().toString();
// Which row to update, based on the title
        String selection = estructuraBaseDeDatos.Nombre_Palabra + " LIKE ?";
        String[] selectionArgs = {nombre.getText().toString()};

        int count = db.update(
                estructuraBaseDeDatos.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        if(count>0){
            Toast.makeText(this,"ACTUALIZACION HECHA CORRECTAMENTE", Toast.LENGTH_LONG).show();
            descripcion.setText(descripcionvariable);
        }else{
            Toast.makeText(this,"Esa palabra no se encuentra en la base de datos", Toast.LENGTH_LONG).show();
        }
    }

}




