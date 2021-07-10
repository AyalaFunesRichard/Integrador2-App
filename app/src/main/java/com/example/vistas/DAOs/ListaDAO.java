package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.DTOs.Lista;
import com.example.vistas.DataBase.DB_Manager;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Commons.Codes;
import com.example.vistas.Load_Main;
import com.example.vistas.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ListaDAO implements Code_DB {

    // ->
    public ArrayList<Lista> DummyData_insert() {
        ArrayList<Lista> lstProductos = new ArrayList<>();

        insert(new Lista("-1", "Tottus", -1, "", 1, "19-05-2021", ""), false);
        insert(new Lista("-1", "Veterinario", -1, "", 1, "10-05-2021", ""), false);
        insert(new Lista("-1", "Por si gana castillo", -1, "", 1, "19-04-2021", ""), false);
        insert(new Lista("-1", "Por si gana keiko", -1, "", 1, "19-04-2021", ""), false);
        insert(new Lista("-1", "Utiles escolares", 133, "", 2, "19-04-2021", ""), false);
        insert(new Lista("-1", "Mercado", 150, "", 2, "19-04-2021", ""), false);

        return lstProductos;
    }
    // <-

    String idUsuario;
    DataSnapshot dataSnapshot;

    DatabaseReference reference;

    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public ListaDAO(Context context) {
        dbManager = new DB_Manager(context);

        if (Load_Main.usuario.getIdUsario() != null) {
            idUsuario = Load_Main.usuario.getIdUsario();
            dataSnapshot = Load_Main.dataSnapshot;
        } else {
            if (MainActivity.usuario.getIdUsario() != null) {
                idUsuario = MainActivity.usuario.getIdUsario();
                dataSnapshot = MainActivity.dataSnapshot;
            }
        }
    }

    public void startConnection() {
        dbConnection = dbManager.getWritableDatabase();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    public int insert(Lista lista, boolean justLocally) {

        String id;
        if (!justLocally) {
            reference = FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(idUsuario).child(TABLE_LISTA);

            id = String.valueOf(System.currentTimeMillis());
            lista.setIdLista(id);

            reference.child(id).setValue(lista);
        }

        ContentValues values = new ContentValues();

        values.put("nombre", lista.getNombre());
        values.put("idLista", lista.getIdLista());
        values.put("gasto", lista.getGasto());
        values.put("fechaComprado", lista.getFechaComprado());
        values.put(ESTADO, lista.getEstado());
        values.put(LOG_FECHA_CREADO, lista.getLog_fecha_creado());
        values.put(LOG_FECHA_MODIFICADO, lista.getLog_fecha_modificado());

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_LISTA, null, values);

        closeConnection();

        return rspt;
    }

    // * SELECT ->

    public Lista select_where_nombre(String nombre) {

        Lista listaRspt = null;

        startConnection();

        String queryString = "SELECT idLista, nombre FROM " + TABLE_LISTA + " WHERE nombre = \'" + nombre + "\'";
        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            listaRspt = new Lista(cursor.getString(0), cursor.getString(1));
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
            Lista listaAux = new Lista(cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
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
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            Lista listaAux = new Lista(cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6));
            lstRspt.add(listaAux);
        }

        closeConnection();

        return lstRspt;
    }

    public ArrayList<Lista> select_all_Comprada() {

        ArrayList<Lista> lstRspt = new ArrayList<>();

        // TODO: falta especificar este mes
        String queryString = "SELECT nombre, gasto, fechaComprado  FROM " + TABLE_LISTA + " WHERE " + ESTADO + " = " + Codes.LISTA_ESTADO_COMPRADA;

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            Lista listaAux = new Lista(cursor.getString(0), cursor.getDouble(1), cursor.getString(2));
            lstRspt.add(listaAux);
        }

        closeConnection();

        return lstRspt;
    }

    public ArrayList<Lista> select_where_idProducto_estadoLista(String idProducto, int estadoLista) {

        ArrayList<Lista> lstRspt = new ArrayList<>();

        String queryString = "SELECT " + TABLE_LISTA + ".idLista, " + TABLE_LISTA + ".nombre  " +
                "FROM " + TABLE_LISTA + " INNER JOIN " + TABLE_PRODUCTO_LISTA +
                " ON " + TABLE_PRODUCTO_LISTA + ".idLista = " + TABLE_LISTA + ".idLista " +
                "INNER JOIN " + TABLE_PRODUCTO +
                " ON " + TABLE_PRODUCTO_LISTA + ".idProducto = " + TABLE_PRODUCTO + ".idProducto " +
                "WHERE " + TABLE_LISTA + "." + ESTADO + " = " + estadoLista +
                " AND " + TABLE_PRODUCTO + ".idProducto = " + idProducto;

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            Lista listaAux = new Lista(cursor.getString(0), cursor.getString(1));
            lstRspt.add(listaAux);
        }

        closeConnection();

        return lstRspt;
    }

    public Lista select_where_nombre_estadoNoComprada(String nombre) {

        Lista listaRspt = null;

        startConnection();

        String queryString = "SELECT idLista, nombre FROM " + TABLE_LISTA + " WHERE nombre = \'" + nombre + "\' AND estado = " + Codes.LISTA_ESTADO_NO_COMPRADA;
        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            listaRspt = new Lista(cursor.getString(0), cursor.getString(1));
        }

        closeConnection();

        return listaRspt;
    }

    // * UPDATE ->

    public int update_where_idLista(Lista lista) {

        FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(idUsuario).child(TABLE_LISTA).child(lista.getIdLista()).setValue(lista);

        ContentValues cv = new ContentValues();
        cv.put("nombre", lista.getNombre());
        cv.put("estado", lista.getEstado());
        cv.put("gasto", lista.getGasto());
        cv.put(LOG_FECHA_MODIFICADO, lista.getLog_fecha_modificado());
        cv.put("fechaComprado", lista.getFechaComprado());

        startConnection();

        int rspt = dbConnection.update(TABLE_LISTA, cv, "idLista = ?", new String[]{(lista.getIdLista() + "")});

        closeConnection();

        return rspt;
    }

    // * DELETE ->

    public int delete_where_idLista(String id) {
        String pa[] = {(id + "")};

        dataSnapshot.getRef().child(TABLE_LISTA).child((id + "")).removeValue();

        startConnection();

        int rspt = dbConnection.delete(TABLE_LISTA, "idLista = ?", pa);

        closeConnection();

        return rspt;
    }

}
