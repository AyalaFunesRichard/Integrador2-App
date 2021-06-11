package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.DataBase.DB_Manager;
import com.example.vistas.Definitions.Code_DB;
import com.example.vistas.Definitions.Codes;

import java.util.ArrayList;

public class ListaDAO implements Code_DB {

    // ->
    public ArrayList<Lista> DummyData_insert() {
        ArrayList<Lista> lstProductos = new ArrayList<>();

        insert(new Lista(-1, "Tottus", -1, "", 1, "19-05-2021", ""));
        insert(new Lista(-1, "Veterinario", -1, "", 1, "10-05-2021", ""));
        insert(new Lista(-1, "Por si gana castillo", -1, "", 1, "19-04-2021", ""));
        insert(new Lista(-1, "Por si gana keiko", -1, "", 1, "19-04-2021", ""));
        insert(new Lista(-1, "Utiles escolares", -1, "", 2, "19-04-2021", ""));
        insert(new Lista(-1, "Mercado", -1, "", 2, "19-04-2021", ""));

        return lstProductos;
    }
    // <-

    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public ListaDAO(Context context) {
        dbManager = new DB_Manager(context);
    }

    public void startConnection() {
        dbConnection = dbManager.getWritableDatabase();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    public int insert(Lista lista) {
        ContentValues values = new ContentValues();

        values.put("nombre", lista.getNombre());
        values.put("gasto", lista.getFechaComprado());
        values.put("fechaComprado", lista.getFechaComprado());
        values.put(ESTADO, lista.getEstado());
        values.put(LOG_FECHA_CREADO, lista.getLog_fecha_creado());
        values.put(LOG_FECHA_MODIFICADO, lista.getLog_fecha_modificado());

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_LISTA, null, values);

        closeConnection();

        return rspt;
    }

    public Lista select_where_nombre(String nombre){

        Lista listaRspt = null;

        startConnection();

        String queryString = "SELECT idLista, nombre FROM " + TABLE_LISTA + " WHERE nombre = \'" + nombre + "\'";
        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            listaRspt = new Lista(cursor.getInt(0), cursor.getString(1));
        }

        closeConnection();

        return listaRspt;
    }

    public ArrayList<Lista> select_all() {

        ArrayList<Lista> lstRspt = new ArrayList<>();

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery("SELECT * FROM " + TABLE_LISTA, null);

        while (cursor.moveToNext()) {
            Lista listaAux = new Lista(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
            lstRspt.add(listaAux);
        }

        closeConnection();

        return lstRspt;
    }

    public ArrayList<Lista> select_all_NoComprada() {

        ArrayList<Lista> lstRspt = new ArrayList<>();

        String queryString = "SELECT * FROM " + TABLE_LISTA + " WHERE " + ESTADO + " = " + Codes.LISTA_ESTADO_NO_COMPRADA;

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null );

        while (cursor.moveToNext()) {
            Lista listaAux = new Lista(cursor.getInt(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
            lstRspt.add(listaAux);
        }

        closeConnection();

        return lstRspt;
    }

    public ArrayList<Lista> select_all_Comprada() {

        ArrayList<Lista> lstRspt = new ArrayList<>();

        // TODO: hacer q solo traiga las listas q ya fueron comprada, en este mes
        String queryString = "SELECT nombre, gasto, fechaComprado  FROM " + TABLE_LISTA + " WHERE " + ESTADO + " = " + Codes.LISTA_ESTADO_COMPRADA;

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null );

        while (cursor.moveToNext()) {
            Lista listaAux = new Lista(cursor.getString(0), cursor.getDouble(2), cursor.getString(2));
            lstRspt.add(listaAux);
        }

        closeConnection();

        return lstRspt;
    }

    public int update_where_idLista(Lista lista) {

        ContentValues cv = new ContentValues();
        cv.put("nombre", lista.getNombre()); //These Fields should be your String values of actual column names
        cv.put(LOG_FECHA_MODIFICADO, lista.getLog_fecha_modificado());

        startConnection();

        int rspt = dbConnection.update(TABLE_LISTA, cv, "idLista = ?", new String[]{(lista.getIdLista() + "")});

        closeConnection();

        return rspt;
    }

//    public Producto select_where_idCategoria(String idCategoria) {
//
//        Producto productoRspt = null;
//
//        SQLiteDatabase sql = dbManager.getReadableDatabase();
//
//        String queryString = "SELECT idProduct, nombre FROM " + TABLE_PRODUCTO + " INNER JOIN " + TABLE_PRODUCTO_CATEGORIA + " ON " + TABLE_PRODUCTO_CATEGORIA + ".idCategoria = " + idCategoria;
//        Cursor cursor = null;
//        cursor = sql.rawQuery(queryString, null);
//
//        while (cursor.moveToNext()) {
//            productoRspt = new Producto(cursor.getInt(0), cursor.getString(1));
//        }
//        sql.close();
//
//        return productoRspt;
//    }

    public int delete_where_idLista(int id) {
        String pa[] = {(id + "")};

        startConnection();

        int rspt = dbConnection.delete(TABLE_LISTA, "idLista = ?", pa);

        closeConnection();

        return rspt;
    }

}
