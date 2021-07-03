package com.example.vistas.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DTOs.Presupuesto;
import com.example.vistas.DTOs.Usuario;
import com.example.vistas.DataBase.DB_Manager;
import com.example.vistas.Load_Main;
import com.example.vistas.MainActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class PresupuestoDAO implements Code_DB {

    String idUsuario;
    DataSnapshot dataSnapshot;

    DatabaseReference reference;

    //
    SQLiteDatabase dbConnection;
    SQLiteOpenHelper dbManager;

    public PresupuestoDAO(Context context) {
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
    public int insert(Presupuesto presupuesto, boolean justLocally) {

        String id;
        if (!justLocally) {
            reference = FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(idUsuario).child(TABLE_PRESUPUESTO);

            id = String.valueOf(System.currentTimeMillis());
            presupuesto.setIdPresupuesto(id);

            reference.child(id).setValue(presupuesto);
        }

        ContentValues cv = new ContentValues();

        cv.put("idPresupuesto", presupuesto.getIdPresupuesto());
        cv.put("fecha", presupuesto.getFecha());
        cv.put("monto", presupuesto.getPresupuesto());
        cv.put(ESTADO, presupuesto.getEstado());
        cv.put(LOG_FECHA_CREADO, presupuesto.getLog_fecha_creado());
        cv.put(LOG_FECHA_MODIFICADO, presupuesto.getLog_fecha_modificado());

        startConnection();

        int rspt = (int) dbConnection.insert(TABLE_PRESUPUESTO, null, cv);

        closeConnection();

        return rspt;
    }

    // * DELETE ->
    public int disable_where_Idpresupuesto() {

        String queryString = "DELETE FROM " + TABLE_PRESUPUESTO;
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
        cursor = dbConnection.rawQuery("SELECT * FROM " + TABLE_PRESUPUESTO, null);

        while (cursor.moveToNext()) {
            Usuario usuarioAux = new Usuario(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5), cursor.getString(6));
            lstRspt.add(usuarioAux);
        }

        closeConnection();

        return lstRspt;
    }

    // * UPDATE ->
    public int update_where_idPresupuesto(Presupuesto presupuesto) {

        DatabaseReference dbreference = FirebaseDatabase.getInstance().getReference(TABLE_USUARIO).child(idUsuario).child(TABLE_PRESUPUESTO).child(presupuesto.getIdPresupuesto());

        dbreference.child("presupuesto").setValue(presupuesto.getPresupuesto());
        dbreference.child(LOG_FECHA_MODIFICADO).setValue(presupuesto.getLog_fecha_modificado());

        ContentValues cv = new ContentValues();
        cv.put("fecha", presupuesto.getFecha());
        cv.put("monto", presupuesto.getPresupuesto());
        cv.put(ESTADO, presupuesto.getEstado());
        cv.put(LOG_FECHA_MODIFICADO, presupuesto.getLog_fecha_modificado());

        startConnection();

        int rspt = dbConnection.update(TABLE_PRESUPUESTO, cv, "idPresupuesto = ?", new String[]{(presupuesto.getIdPresupuesto() + "")});

        closeConnection();

        return rspt;
    }

    public Presupuesto select_where_thisMonth(Context context) {

        Presupuesto rspt = null;

        startConnection();

        Cursor cursor = null;
        cursor = dbConnection.rawQuery("SELECT * FROM " + TABLE_PRESUPUESTO, null);

        CommonMethods cm = new CommonMethods(context);
        String localMonthYear = cm.getMonthAndYear();
        String monthYearSaved;
        while (cursor.moveToNext()) {
            Presupuesto aux = new Presupuesto(cursor.getString(0), cursor.getString(1), cursor.getDouble(2), cursor.getInt(3), cursor.getString(4), cursor.getString(5));

            monthYearSaved = cm.getMonthAndYear(aux.getLog_fecha_creado());
            if (localMonthYear.equals(monthYearSaved)) {
                rspt = aux;
                break;
            }
        }

        closeConnection();

        return rspt;
    }
}
