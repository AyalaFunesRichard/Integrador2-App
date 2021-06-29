package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.Commons.Code_DB;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.DTOs.Usuario;
import com.example.vistas.DataBase.DB_Manager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class UsuarioDAO implements Code_DB {

    FirebaseDatabase rootode;
    DatabaseReference dbReference;
    //
    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public UsuarioDAO(Context context) {
        dbManager = new DB_Manager(context);
    }


    public void startConnection() {
        dbConnection = dbManager.getWritableDatabase();
    }

    public void closeConnection() {
        dbConnection.close();
    }

    // * INSERT ->
    public int insert(Usuario usuario) {
        ContentValues cv = new ContentValues();

        cv.put("nombreFamiliar", usuario.getNombreFamiliar());
        cv.put("idUsuario", usuario.getIdUsario());
        cv.put("correo", usuario.getCorreo());
        cv.put(ESTADO, usuario.getEstado());
        cv.put(LOG_FECHA_CREADO, usuario.getLog_fecha_creado());
        cv.put(LOG_FECHA_ULTIMA_SESION, usuario.getLog_fecha_ultimaSesion());

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_USUARIO, null, cv);

        closeConnection();

        return rspt;
    }

    // * DELETE ->
    public int deleteAll() {

        String queryString = "DELETE FROM " + TABLE_USUARIO;
        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery(queryString, null);


        closeConnection();

        return 2; // TODO mejorar este metodo
    }

    // * SELECT ->
    public ArrayList<Usuario> select_all() {

        ArrayList<Usuario> lstRspt = new ArrayList<>();

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery("SELECT * FROM " + TABLE_USUARIO, null);

        while (cursor.moveToNext()) {
            Usuario usuarioAux = new Usuario(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            lstRspt.add(usuarioAux);
        }

        closeConnection();

        return lstRspt;
    }

    // * UPDATE ->
    public int update_NombreFamiliar_where_IdUsuario(Usuario usuario) {

        FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(usuario.getIdUsario()).child("nombreFamiliar").setValue(usuario.getNombreFamiliar());
        FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(usuario.getIdUsario()).child(LOG_FECHA_MODIFICADO).setValue(usuario.getLog_fecha_modificado());

        ContentValues cv = new ContentValues();
        cv.put("nombreFamiliar", usuario.getNombreFamiliar());
        cv.put(LOG_FECHA_MODIFICADO, usuario.getLog_fecha_modificado());

        startConnection();

        int rspt = dbConnection.update(TABLE_USUARIO, cv, "idUsuario = ?", new String[]{(usuario.getIdUsario() + "")});

        closeConnection();

        return rspt;
    }


}
