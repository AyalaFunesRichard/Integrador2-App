package com.example.vistas.DTOs;

import java.util.ArrayList;

public class Categoria {

    private String idCategoria;
    private String nombre;
    private ArrayList<Producto> lstProductos;

    private int estado;
    private String log_fecha_creado;
    private String log_fecha_modificado;


    /**
     * CONSTRUCTORS ->
     */

    public Categoria() {
    }

    public Categoria(String idCategoria, String nombre) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
    }

    public Categoria(String idCategoria, String nombre, ArrayList<Producto> lstProductos, int estado, String log_fecha_creado, String log_fecha_modificado) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.lstProductos = lstProductos;
        this.estado = estado;
        this.log_fecha_creado = log_fecha_creado;
        this.log_fecha_modificado = log_fecha_modificado;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }

    /**
     * GETTERS & SETTERS ->
     */


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Producto> getLstProductos() {
        return lstProductos;
    }

    public void setLstProductos(ArrayList<Producto> lstProductos) {
        this.lstProductos = lstProductos;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getLog_fecha_modificado() {
        return log_fecha_modificado;
    }

    public void setLog_fecha_modificado(String log_fecha_modificado) {
        this.log_fecha_modificado = log_fecha_modificado;
    }

    public String getLog_fecha_creado() {
        return log_fecha_creado;
    }

    public void setLog_fecha_creado(String log_fecha_creado) {
        this.log_fecha_creado = log_fecha_creado;
    }
}
