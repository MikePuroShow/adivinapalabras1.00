package com.example.adivinapalabras;

public class estructuraBaseDeDatos {
    public static final String TABLE_NAME = "datosPartida"; //

    public static final String Nombre_Palabra = "nombre_Palabra";
    public static final String Descripcion_Palabra = "descripcion_Palabra";

    private estructuraBaseDeDatos() {

    }
    protected static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + estructuraBaseDeDatos.TABLE_NAME + " (" +
                    estructuraBaseDeDatos.Nombre_Palabra + " VARCHAR PRIMARY KEY," + //POR ESTA INSTRUCCION ES EL ID
                    estructuraBaseDeDatos.Descripcion_Palabra + " TEXT)";

    protected static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + estructuraBaseDeDatos.TABLE_NAME;
}
