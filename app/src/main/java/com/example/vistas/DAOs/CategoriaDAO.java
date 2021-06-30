package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.DTOs.Categoria;
import com.example.vistas.DataBase.DB_Manager;
import com.example.vistas.Commons.Codes;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Load_Main;
import com.example.vistas.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CategoriaDAO implements Code_DB {

    // ->
    public void DummyData_insert() {
        insert(new Categoria("-1", "En Tottus", null, Codes.CATEGORIA_ESTADO_ACTIVO, "19-05-2021", ""), false);
        insert(new Categoria("-1", "Opcional", null, Codes.CATEGORIA_ESTADO_ACTIVO, "20-05-2021", ""), false);
        insert(new Categoria("-1", "Donde sea", null, Codes.CATEGORIA_ESTADO_ACTIVO, "20-05-2021", ""), false);
        insert(new Categoria("-1", "Solo en mercado", null, Codes.CATEGORIA_ESTADO_ACTIVO, "21-05-2021", ""), false);
        insert(new Categoria("-1", "Para el michi", null, Codes.CATEGORIA_ESTADO_ACTIVO, "22-05-2021", ""), false);
    }
    // <-

    String idUsuario;
    DataSnapshot dataSnapshot;

    DatabaseReference reference;

    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public CategoriaDAO(Context context) {
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
    public int insert(Categoria categoria, boolean justLocally ) {

        String id;
        if(!justLocally){
            reference = FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(idUsuario).child(TABLE_CATEGORIA);

            id = String.valueOf(System.currentTimeMillis());
            categoria.setIdCategoria(id);

            reference.child(id).setValue(categoria);
        }

        ContentValues values = new ContentValues();

        values.put("nombre", categoria.getNombre());
        values.put("idCategoria", categoria.getIdCategoria());
        values.put(ESTADO, categoria.getEstado());
        values.put(LOG_FECHA_CREADO, categoria.getLog_fecha_creado());
        values.put(LOG_FECHA_MODIFICADO, categoria.getLog_fecha_modificado());

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_CATEGORIA, null, values);

        closeConnection();

        return rspt;
    }

    // * SELECT ->
    public ArrayList<Categoria> select_all() {

        ArrayList<Categoria> lstRspt = new ArrayList<>();

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery("SELECT * FROM " + TABLE_CATEGORIA, null);

        while (cursor.moveToNext()) {
            Categoria categoriaAux = new Categoria(cursor.getString(0), cursor.getString(1), null,  cursor.getInt(2), cursor.getString(3), cursor.getString(4));
            lstRspt.add(categoriaAux);
        }

        closeConnection();

        return lstRspt;
    }

    public ArrayList<Categoria> select_lstCategoria_where_idProducto(String idProducto) {

        String queryString = "SELECT Categoria.idCategoria, Categoria.nombre FROM "
                + TABLE_CATEGORIA + " INNER JOIN " + TABLE_PRODUCTO_CATEGORIA
                + " ON " + TABLE_CATEGORIA + ".idCategoria = " + TABLE_PRODUCTO_CATEGORIA + ".idCategoria "
                + "INNER JOIN " + TABLE_PRODUCTO + " ON " + TABLE_PRODUCTO_CATEGORIA + ".idProducto = " + TABLE_PRODUCTO + ".idProducto "
                + "WHERE " + TABLE_PRODUCTO + ".idProducto = " + idProducto;

        ArrayList<Categoria> lstCategoria = new ArrayList<>();

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        Categoria categoriaAux = null;
        while (cursor.moveToNext()) {
            categoriaAux = new Categoria(cursor.getString(0), cursor.getString(1));
            lstCategoria.add(categoriaAux);
        }

        closeConnection();

        return lstCategoria;
    }

    public Categoria select_where_nombre(String nombre){

        Categoria categoriaRspt = null;

        startConnection();

        String queryString = "SELECT idCategoria, nombre FROM " + TABLE_CATEGORIA + " WHERE nombre = \'" + nombre + "\'";

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            categoriaRspt = new Categoria(cursor.getString(0), cursor.getString(1));
        }

        closeConnection();

        return categoriaRspt;
    }

    // * DELETE ->
    public int delete_where_idCategoria(String id) {
        String pa[] = {(id + "")};

        dataSnapshot.getRef().child(TABLE_CATEGORIA).child((id + "")).removeValue();

        startConnection();

        int rspt = dbConnection.delete(TABLE_CATEGORIA, "idCategoria = ?", pa);

        closeConnection();

        return rspt;
    }

    // * UPDATE ->
    public int update_where_idCategoria(Categoria categoria) {

        FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(idUsuario).child(TABLE_CATEGORIA).child(categoria.getIdCategoria()).setValue(categoria);

        ContentValues cv = new ContentValues();
        cv.put("nombre", categoria.getNombre()); //These Fields should be your String values of actual column names
        cv.put(LOG_FECHA_MODIFICADO, categoria.getLog_fecha_modificado());

        startConnection();

        int rspt = dbConnection.update(TABLE_CATEGORIA, cv, "idCategoria = ?", new String[]{(categoria.getIdCategoria() + "")});

        closeConnection();

        return rspt;
    }

}
