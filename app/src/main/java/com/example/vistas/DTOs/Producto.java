package com.example.vistas.DTOs;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Producto implements Serializable {
    private String idProducto;
    private String nombre;

    private ArrayList<Lista> lstLista;
    private ArrayList<Categoria> lstCategoria;

    private int estado;
    private String log_fecha_creado;
    private String log_fecha_moficado;

    /**
     * CONSTRUCTORS ->
     */

    public Producto() {
    }

    public Producto(String idProducto, String nombre) {
        this.idProducto = idProducto;
        this.nombre = nombre;
    }

    public Producto(String idProducto, String nombre, int estado, String log_fecha_creado, String log_fecha_moficado) {
        this.idProducto = idProducto;
        this.nombre = nombre;
        this.estado = estado;
        this.log_fecha_creado = log_fecha_creado;
        this.log_fecha_moficado = log_fecha_moficado;
    }


    /**
     * GETTERS & SETTERS ->
     */

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
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
