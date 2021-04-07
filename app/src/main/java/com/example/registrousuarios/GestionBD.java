package com.example.registrousuarios;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class GestionBD extends SQLiteOpenHelper {

    private static final String DATABASEUSERS = "dbusuarios";
    private static final int VERSION = 1;
    private static final String TABLA_USUARIOS = "usuarios";

    private static final String CREARTABLA = "CREATE TABLE " + TABLA_USUARIOS + " (USU_DOCUMENTO INTEGER PRIMARY KEY, " +
           "USU_NOMBRES varchar(100) NOT NULL, USU_APELLIDOS varchar(100) NOT NULL, USU_USUARIO varchar(30) NOT NULL, " +
           "USU_CONTRA varchar(20) NOT NULL);";
    private static final String ELIMINARTABLA = "DROP TABLE IF EXISTS " + TABLA_USUARIOS;

    public GestionBD(Context context){
        super(context, DATABASEUSERS, null, VERSION);
        Log.i(null, "Se creo: "+ DATABASEUSERS);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(CREARTABLA);
        Log.i(null, "Se creo la tabla: "+ CREARTABLA);
    }
    //Se ejecuta cuando cambia la versión de la BD es decir estructura
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(ELIMINARTABLA);
        onCreate(db);
    }
}
