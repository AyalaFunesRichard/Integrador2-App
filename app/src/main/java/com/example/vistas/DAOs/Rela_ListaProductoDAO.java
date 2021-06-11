package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.DTOs.Producto;
import com.example.vistas.DataBase.DB_Manager;
import com.example.vistas.Definitions.Code_DB;

import java.util.ArrayList;

public class Rela_ListaProductoDAO implements Code_DB {


    public ArrayList<Producto> DummyData_insert() {
        ArrayList<Producto> lstProductos = new ArrayList<>();

        insert( 1, 1);
        insert( 2, 1);
        insert( 3, 1);
        insert( 4, 1);
        insert( 2, 2);
        insert( 3, 2);
        insert( 4, 2);
        insert( 8, 3);
        insert( 9, 3);
        insert( 7, 3);
        insert( 11, 3);
        insert( 12, 3);
        insert( 13, 3);
        insert( 14, 3);
        insert( 11, 4);
        insert( 12, 4);
        insert( 13, 4);
        insert( 3, 5);
        insert( 4, 5);
        insert( 2, 5);
        insert( 2, 5);
        insert( 14, 5);

        return lstProductos;
    }


    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public Rela_ListaProductoDAO(Context context) {
        dbManager = new DB_Manager(context);
    }

    public void startConnection() {
        dbConnection = dbManager.getWritableDatabase();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    // * INSERT ->
    public int insert(int idProducto, int idLista) {
        ContentValues values = new ContentValues();

        values.put("idProducto", idProducto);
        values.put("idLista", idLista);

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_PRODUCTO_LISTA, null, values);

        closeConnection();

        return rspt;
    }

    // * DELETE ->
    public int delete_where_idP_idL(int idProducto, int idCategoria) {
        String whereArgs[] = {(idProducto + ""), (idCategoria + "")};
        String whereClause = "idProducto = ? AND idLista = ?";

        startConnection();

        int rspt = dbConnection.delete(TABLE_PRODUCTO_LISTA, whereClause, whereArgs);

        closeConnection();

        return rspt;
    }

    public int delete_where_idProducto(int idProducto) {// * Mostly used when a PRODUCTO is deleted
        String arg[] = {(idProducto + "")};

        startConnection();

        int rspt = dbConnection.delete(TABLE_PRODUCTO_LISTA, "idProducto = ?", arg);

        closeConnection();

        return rspt;
    }

    public int delete_where_idLista(int idLista) {// * Mostly used when a LISTA is deleted
        String arg[] = {(idLista + "")};

        startConnection();

        int rspt = dbConnection.delete(TABLE_PRODUCTO_LISTA, "idLista = ?", arg);

        closeConnection();

        return rspt;
    }

}
