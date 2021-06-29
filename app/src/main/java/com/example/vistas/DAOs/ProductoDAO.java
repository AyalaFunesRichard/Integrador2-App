package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.DTOs.Producto;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.DataBase.DB_Manager;
import com.example.vistas.Load_Main;
import com.example.vistas.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;

public class ProductoDAO implements Code_DB {

    // ->
    public ArrayList<Producto> DummyData_insert() {
        ArrayList<Producto> lstProductos = new ArrayList<>();

//        insert(new Producto(1, "Manzanas", 1, "19-05-2021", ""));
//        insert(new Producto(2, "Peras", 1, "19-05-2021", ""));
//        insert(new Producto(3, "Platanos", 1, "19-05-2021", ""));
//        insert(new Producto(4, "Arroz", 1, "19-05-2021", ""));
//        insert(new Producto(5, "Menestra", 1, "19-05-2021", ""));
//        insert(new Producto(6, "Galletas perro", 1, "19-05-2021", ""));
//        insert(new Producto(7, "Pastilla dolor cabeza", 1, "19-05-2021", ""));
//        insert(new Producto(8, "Ibuprofeno", 1, "19-05-2021", ""));
//        insert(new Producto(9, "Galleta gato", 1, "19-05-2021", ""));
//        insert(new Producto(10, "Maiz tordo", 1, "19-05-2021", ""));
//        // <- 10
//        insert(new Producto(11, "Queso", 1, "19-05-2021", ""));
//        insert(new Producto(9, "Galleta gato", 1, "19-05-2021", ""));
//        insert(new Producto(10, "Maiz tordo", 1, "19-05-2021", ""));
//        insert(new Producto(11, "Aceituna", 1, "19-05-2021", ""));
        // <- 14
        return lstProductos;
    }
    // <-

    String idUsuario;
    DataSnapshot dataSnapshot;

    DatabaseReference reference;
    //
    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public ProductoDAO(Context context) {
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
    public int insert(Producto producto, boolean justLocally) {

        String newIdProducto;
        if(!justLocally){
            reference = FirebaseDatabase.getInstance().getReference("Usuario").child(idUsuario).child(TABLE_PRODUCTO);

            newIdProducto = String.valueOf(System.currentTimeMillis());
            producto.setIdProducto(newIdProducto);

            reference.child(newIdProducto).setValue(producto);
        }

        ContentValues values = new ContentValues();
        values.put("idProducto", producto.getIdProducto());
        values.put("nombre", producto.getNombre());
        values.put(ESTADO, producto.getEstado());
        values.put(LOG_FECHA_CREADO, producto.getLog_fecha_creado());
        values.put(LOG_FECHA_MODIFICADO, producto.getLog_fecha_moficado());

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_PRODUCTO, null, values);

        closeConnection();

        return rspt;
    }

    // * SELECT ->
    public ArrayList<Producto> select_all() {

        ArrayList<Producto> lstRspt = new ArrayList<>();

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery("SELECT * FROM " + TABLE_PRODUCTO, null);

        while (cursor.moveToNext()) {
            Producto productoAux = new Producto(cursor.getString(0), cursor.getString(1), cursor.getInt(2), cursor.getString(3), cursor.getString(4));
            lstRspt.add(productoAux);
        }

        closeConnection();

        return lstRspt;
    }

    public Producto select_where_idCategoria(String idCategoria) {

        Producto productoRspt = null;

        startConnection();

        String queryString = "SELECT idProducto, nombre FROM " +
                TABLE_PRODUCTO + " INNER JOIN " + TABLE_PRODUCTO_CATEGORIA + " " +
                "ON " + TABLE_PRODUCTO_CATEGORIA + ".idCategoria = " + idCategoria; // TODO <-  FALTA COMPLETAR ESTE CODIGO

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            productoRspt = new Producto(cursor.getString(0), cursor.getString(1));
        }

        closeConnection();

        return productoRspt;
    }

    public ArrayList<Producto> select_where_idLista_estadoLista(String idLista) {

        String queryString = "SELECT Producto.idProducto, Producto.nombre FROM "
                + TABLE_PRODUCTO + " INNER JOIN " + TABLE_PRODUCTO_LISTA
                + " ON " + TABLE_PRODUCTO + ".idProducto = " + TABLE_PRODUCTO_LISTA + ".idProducto "
                + "INNER JOIN " + TABLE_LISTA + " ON " + TABLE_LISTA + ".idLista = " + TABLE_PRODUCTO_LISTA + ".idLista "
                + "WHERE " + TABLE_PRODUCTO_LISTA + ".idLista = " + idLista;

        ArrayList<Producto> lstProducto = new ArrayList<>();

        Cursor cursor = null;

        startConnection();

        cursor = dbConnection.rawQuery(queryString, null);

        Producto categoriaAux = null;
        while (cursor.moveToNext()) {
            categoriaAux = new Producto(cursor.getString(0), cursor.getString(1));
            lstProducto.add(categoriaAux);
        }

        closeConnection();

        return lstProducto;
    }

    public Producto select_where_nombre(String nombre) {

        Producto productoRspt = null;

        startConnection();

        String queryString = "SELECT idProducto, nombre FROM " + TABLE_PRODUCTO + " WHERE nombre = \'" + nombre + "\'";

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);

        while (cursor.moveToNext()) {
            productoRspt = new Producto(cursor.getString(0), cursor.getString(1));
        }

        closeConnection();

        return productoRspt;
    }

    // * DELETE ->
    public int delete_where_idProducto(String id) {
        String pa[] = {(id + "")};

        dataSnapshot.getRef().child(TABLE_PRODUCTO).child((id + "")).removeValue();

        startConnection();

        int rspt = dbConnection.delete(TABLE_PRODUCTO, "idProducto = ?", pa);

        closeConnection();

        return rspt;
    }

    // * UPDATE ->
    public int update_where_idProductto(Producto producto) {

        FirebaseDatabase.getInstance().getReference("Usuario").child(idUsuario).child(TABLE_PRODUCTO).child(producto.getIdProducto()).setValue(producto);

        ContentValues cv = new ContentValues();
        cv.put("nombre", producto.getNombre()); //These Fields should be your String values of actual column names
        cv.put("log_fecha_modificado", producto.getLog_fecha_moficado());

        startConnection();

        int rspt = dbConnection.update(TABLE_PRODUCTO, cv, "idProducto = ?", new String[]{(producto.getIdProducto() + "")});

        closeConnection();

        return rspt;
    }


}
