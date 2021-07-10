package com.example.vistas.DataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.Commons.Code_DB;
import com.example.vistas.DataBase.DB_Manager;

public class DataBaseQuery implements Code_DB {

    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public DataBaseQuery(Context context) {
        dbManager = new DB_Manager(context);
    }

    public void startConnection() {
        dbConnection = dbManager.getWritableDatabase();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    public void deleteAll_tablesData() {
        startConnection();

        dbConnection.execSQL("delete from " + TABLE_PRODUCTO);
        //
        dbConnection.execSQL("delete from " + TABLE_CATEGORIA);
        //
        dbConnection.execSQL("delete from " + TABLE_LISTA);
        //
        dbConnection.execSQL("delete from " + TABLE_PRESUPUESTO);
        //
        dbConnection.execSQL("delete from " + TABLE_PRODUCTO_CATEGORIA);
        //
        dbConnection.execSQL("delete from " + TABLE_PRODUCTO_LISTA);
        //
        dbConnection.execSQL("delete from " + TABLE_USUARIO);

        closeConnection();
    }

    public void deleteAll_butNotUsuario() {
        startConnection();

        dbConnection.execSQL("delete from " + TABLE_PRODUCTO);
        //
        dbConnection.execSQL("delete from " + TABLE_CATEGORIA);
        //
        dbConnection.execSQL("delete from " + TABLE_LISTA);
        //
        dbConnection.execSQL("delete from " + TABLE_PRESUPUESTO);
        //
        dbConnection.execSQL("delete from " + TABLE_PRODUCTO_CATEGORIA);
        //
        dbConnection.execSQL("delete from " + TABLE_PRODUCTO_LISTA);

        closeConnection();
    }

}
