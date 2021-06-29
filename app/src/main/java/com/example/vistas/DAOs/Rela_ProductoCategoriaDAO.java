package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.Commons.Codes;
import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.DTOs.Rela_ProductoCategoria;
import com.example.vistas.DataBase.DB_Manager;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Load_Main;
import com.example.vistas.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Rela_ProductoCategoriaDAO implements Code_DB {


    public ArrayList<Producto> DummyData_insert() {
        ArrayList<Producto> lstProductos = new ArrayList<>();

//        insert(1, 3);
//        insert(1, 1);
//        insert(1, 2);
//        insert(1, 4);
//        insert(2, 3);
//        insert(2, 2);
//        insert(3, 1);
//        insert(3, 4);
//        insert(4, 2);
//        insert(5, 1);
//        insert(6, 1);
//        insert(7, 4);
//        insert(8, 2);
//        insert(9, 1);
//        insert(10, 1);
//        insert(11, 4);
//        insert(12, 2);

        return lstProductos;
    }

    String idUsuario;
    DataSnapshot dataSnapshot;

    DatabaseReference reference;

    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public Rela_ProductoCategoriaDAO(Context context) {
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

    public int insert(Rela_ProductoCategoria rela, boolean justLocally) {

        String id;
        if(!justLocally){
            reference = FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(idUsuario).child(TABLE_PRODUCTO_CATEGORIA);

            id = String.valueOf(System.currentTimeMillis());
            rela.setIdRelaPC(id);

            reference.child(id).setValue(rela);
        }

        ContentValues values = new ContentValues();

        values.put("idRelaPC", rela.getIdRelaPC());
        values.put("idProducto", rela.getIdProducto());
        values.put("idCategoria", rela.getIdCategoria());

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

    // * DELETE ->

    public int deleteAll_where_idProducto(String idProducto) {

        String queryString = "SELECT idRelaPC FROM " + TABLE_PRODUCTO_CATEGORIA + " WHERE idProducto = " + idProducto;

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null );

        while (cursor.moveToNext()) {
//            Rela_ProductoCategoria listaAux = new Rela_ProductoCategoria(cursor.getString(0), cursor.getString(1), cursor.getString(2));
//            lstRspt.add(listaAux);

            dataSnapshot.getRef().child(TABLE_PRODUCTO_CATEGORIA).child(cursor.getString(0)).removeValue();
        }

        String arg[] = {(idProducto + "")};

        int rspt = dbConnection.delete(TABLE_PRODUCTO_CATEGORIA, "idProducto = ?", arg);

        closeConnection();

        return rspt;
    }

}
