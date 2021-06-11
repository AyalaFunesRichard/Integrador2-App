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

public class Rela_ProductoCategoriaDAO implements Code_DB {


    public ArrayList<Producto> DummyData_insert() {
        ArrayList<Producto> lstProductos = new ArrayList<>();

        insert(1, 3);
        insert(1, 1);
        insert(1, 2);
        insert(1, 4);
        insert(2, 3);
        insert(2, 2);
        insert(3, 1);
        insert(3, 4);
        insert(4, 2);
        insert(5, 1);
        insert(6, 1);
        insert(7, 4);
        insert(8, 2);
        insert(9, 1);
        insert(10, 1);
        insert(11, 4);
        insert(12, 2);

        return lstProductos;
    }


    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public Rela_ProductoCategoriaDAO(Context context) {
        dbManager = new DB_Manager(context);
    }

    public void startConnection() {
        dbConnection = dbManager.getWritableDatabase();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    public int insert(int idProducto, int idCategoria) {
        ContentValues values = new ContentValues();

        values.put("idProducto", idProducto);
        values.put("idCategoria", idCategoria);

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_PRODUCTO_CATEGORIA, null, values);

        closeConnection();

        return rspt;
    }

    public int delete_where_idP_idC(int idProducto, int idCategoria) {
        String arg[] = {(idProducto + ""), (idCategoria + "")};

        startConnection();

        int rspt = dbConnection.delete(TABLE_PRODUCTO_CATEGORIA, "idProducto = ? AND idCategoria = ?", arg);

        closeConnection();

        return rspt;
    }

    public int delete_where_idProducto(int idProducto) {
        String arg[] = {(idProducto + "")};

        startConnection();

        int rspt = dbConnection.delete(TABLE_PRODUCTO_CATEGORIA, "idProducto = ?", arg);

        closeConnection();

        return rspt;
    }

}
