package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.DTOs.Producto;
import com.example.vistas.DTOs.Rela_ProductoCategoria;
import com.example.vistas.DTOs.Rela_ProductoLista;
import com.example.vistas.DataBase.DB_Manager;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Load_Main;
import com.example.vistas.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Rela_ListaProductoDAO implements Code_DB {


    public ArrayList<Producto> DummyData_insert() {
        ArrayList<Producto> lstProductos = new ArrayList<>();

//        insert( 1, 1);
//        insert( 2, 1);
//        insert( 3, 1);
//        insert( 4, 1);
//        insert( 2, 2);
//        insert( 3, 2);
//        insert( 4, 2);
//        insert( 8, 3);
//        insert( 9, 3);
//        insert( 7, 3);
//        insert( 11, 3);
//        insert( 12, 3);
//        insert( 13, 3);
//        insert( 14, 3);
//        insert( 11, 4);
//        insert( 12, 4);
//        insert( 13, 4);
//        insert( 3, 5);
//        insert( 4, 5);
//        insert( 2, 5);
//        insert( 2, 5);
//        insert( 14, 5);

        return lstProductos;
    }

    String idUsuario;
    DataSnapshot dataSnapshot;

    DatabaseReference reference;

    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public Rela_ListaProductoDAO(Context context) {
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

    // * INSERT ->
    public int insert(Rela_ProductoLista rela, boolean justLocally) {

        String id;
        if(!justLocally){
            reference = FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(idUsuario).child(TABLE_PRODUCTO_LISTA);

            id = String.valueOf(System.currentTimeMillis());
            rela.setIdRelaPL(id);

            reference.child(id).setValue(rela);
        }

        ContentValues values = new ContentValues();

        values.put("idRelaPL", rela.getIdRelaPL());
        values.put("idProducto", rela.getIdProducto());
        values.put("idLista", rela.getIdLista());

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_PRODUCTO_LISTA, null, values);

        closeConnection();

        return rspt;
    }

    // * DELETE ->
    public int delete_where_idP_idL(String idProducto, String idLista) {
        String whereArgs[] = {(idProducto + ""), (idLista + "")};
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

    public int delete_where_idLista(String idLista) {// * Mostly used when a LISTA is deleted

        String queryString = "SELECT idRelaPL FROM " + TABLE_PRODUCTO_LISTA + " WHERE idLista = " + idLista;

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null );

        while (cursor.moveToNext()) {
//            Rela_ProductoCategoria listaAux = new Rela_ProductoCategoria(cursor.getString(0), cursor.getString(1), cursor.getString(2));
//            lstRspt.add(listaAux);

            dataSnapshot.getRef().child(TABLE_PRODUCTO_LISTA).child(cursor.getString(0)).removeValue();
        }

        String arg[] = {(idLista + "")};

        int rspt = dbConnection.delete(TABLE_PRODUCTO_LISTA, "idLista = ?", arg);

        closeConnection();

        return rspt;
    }



}
