package com.example.vistas.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.vistas.Definitions.Code_DB;


public class DB_Manager extends SQLiteOpenHelper implements Code_DB {

    static int DATABASE_VERSION = 1;

    static private final String CREATE_TABLE_PRODUCTO = "CREATE TABLE " + TABLE_PRODUCTO + " (idProducto INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, estado INTEGER, log_fecha_creado TEXT, log_fecha_modificado TEXT)";
    static private final String CREATE_IF_TABLE_PRODUCTO = "DROP TABLE IF EXISTS " + TABLE_PRODUCTO;
    //
    static private final String CREATE_TABLE_LISTA = "CREATE TABLE " + TABLE_LISTA + " (idLista INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, gasto REAL, fechaComprado TEXT, estado INTEGER, log_fecha_creado TEXT, log_fecha_modificado TEXT)";
    static private final String CREATE_IF_TABLE_LISTA = "DROP TABLE IF EXISTS " + TABLE_LISTA;
    //
    static private final String CREATE_TABLE_USUARIO = "CREATE TABLE " + TABLE_USUARIO + " (idUsuario INTEGER PRIMARY KEY AUTOINCREMENT, correo TEXT, nombreFamiliar TEXT, estado INT, log_fecha_creado TEXT, log_fecha_modificado TEXT, log_fecha_ultimaSesion TEXT)";
    static private final String CREATE_IF_TABLE_USUARIO = "DROP TABLE IF EXISTS " + TABLE_USUARIO;
    //
    static private final String CREATE_TABLE_MONTO = "CREATE TABLE " + TABLE_MONTO + " (idMonto INTEGER PRIMARY KEY AUTOINCREMENT, fecha TEXT, monto INTEGER, estado INT, log_fecha_creado TEXT, log_fecha_modificado TEXT)";
    static private final String CREATE_IF_TABLE_MONTO = "DROP TABLE IF EXISTS " + TABLE_MONTO;
    //
    static private final String CREATE_TABLE_CATEGORIA = "CREATE TABLE " + TABLE_CATEGORIA + " (idCategoria INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, estado INTEGER, log_fecha_creado TEXT, log_fecha_modificado TEXT)";
    static private final String CREATE_IF_TABLE_CATEGORIA = "DROP TABLE IF EXISTS " + TABLE_CATEGORIA;
    //
    static private final String CREATE_TABLE_PRODUCTO_LISTA = "CREATE TABLE " + TABLE_PRODUCTO_LISTA + " (idProducto INTEGER, idLista INTEGER, PRIMARY KEY(idProducto, idLista))";
    static private final String CREATE_IF_TABLE_PRODUCTO_LISTA = "DROP TABLE IF EXISTS " + TABLE_PRODUCTO_LISTA;
    //
    static private final String CREATE_TABLE_PRODUCTO_CATEGORIA = "CREATE TABLE " + TABLE_PRODUCTO_CATEGORIA + " (idProducto INTEGER, idCategoria INTEGER, PRIMARY KEY(idProducto, idCategoria))";
    static private final String CREATE_IF_TABLE_PRODUCTO_CATEGORIA = "DROP TABLE IF EXISTS " + TABLE_PRODUCTO_CATEGORIA;


    public DB_Manager(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PRODUCTO);
        db.execSQL(CREATE_TABLE_LISTA);
        db.execSQL(CREATE_TABLE_USUARIO);
        db.execSQL(CREATE_TABLE_MONTO);
        db.execSQL(CREATE_TABLE_CATEGORIA);
        db.execSQL(CREATE_TABLE_PRODUCTO_LISTA);
        db.execSQL(CREATE_TABLE_PRODUCTO_CATEGORIA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(CREATE_IF_TABLE_PRODUCTO);
        db.execSQL(CREATE_IF_TABLE_LISTA);
        db.execSQL(CREATE_IF_TABLE_USUARIO);
        db.execSQL(CREATE_IF_TABLE_MONTO);
        db.execSQL(CREATE_IF_TABLE_CATEGORIA);
        db.execSQL(CREATE_IF_TABLE_PRODUCTO_LISTA);
        db.execSQL(CREATE_IF_TABLE_PRODUCTO_CATEGORIA);
        Log.w("Message: ", "actualizando " + oldVersion + " con " + newVersion);
    }


}
