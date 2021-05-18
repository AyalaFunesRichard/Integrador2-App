package com.example.vistas.DTOs;

import java.util.ArrayList;
import java.util.List;

public class Producto {
    private int idProducto;
    private String nombre;

    private ArrayList<Lista> lstLista;
    private ArrayList<Categoria> lstCategoria;

    private int estado;
    private String log_fecha_moficado;
    private String log_fecha_creado;

    /**
     * CONSTRUCTORS ->
     */
    public Producto(int idProducto, String nombre) {
        this.idProducto = idProducto;
        this.nombre = nombre;
    }

    /**
     * GETTERS & SETTERS ->
     */
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public ArrayList<Lista> getLstLista() {
        return lstLista;
    }

    public void setLstLista(ArrayList<Lista> lstLista) {
        this.lstLista = lstLista;
    }

    public ArrayList<Categoria> getLstCategoria() {
        return lstCategoria;
    }

    public void setLstCategoria(ArrayList<Categoria> lstCategoria) {
        this.lstCategoria = lstCategoria;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getLog_fecha_moficado() {
        return log_fecha_moficado;
    }

    public void setLog_fecha_moficado(String log_fecha_moficado) {
        this.log_fecha_moficado = log_fecha_moficado;
    }

    public String getLog_fecha_creado() {
        return log_fecha_creado;
    }

    public void setLog_fecha_creado(String log_fecha_creado) {
        this.log_fecha_creado = log_fecha_creado;
    }
}
