package com.example.vistas.DTOs;

import java.io.Serializable;
import java.util.ArrayList;

public class Lista implements Serializable {
    private String idLista;
    private String nombre;
    private double gasto;
    private String fechaComprado;

    private int estado;
    private String log_fecha_creado;
    private String log_fecha_modificado;

    private ArrayList<Producto> lstProductos;

    /**
     * CONSTRUCTORS ->
     */
    public Lista() {
    }

    public Lista(String idLista, String nombre) {
        this.idLista = idLista;
        this.nombre = nombre;
    }

    public Lista(String nombre, double gasto, String fechaComprado) {
        this.nombre = nombre;
        this.gasto = gasto;
        this.fechaComprado = fechaComprado;
    }

    public Lista(String idLista, String nombre, double gasto, String fechaComprado, int estado, String log_fecha_creado, String log_fecha_modificado) {
        this.idLista = idLista;
        this.nombre = nombre;
        this.gasto = gasto;
        this.fechaComprado = fechaComprado;
        this.estado = estado;
        this.log_fecha_creado = log_fecha_creado;
        this.log_fecha_modificado = log_fecha_modificado;
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

    public String getIdLista() {
        return idLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }

    public double getGasto() {
        return gasto;
    }

    public void setGasto(double gasto) {
        this.gasto = gasto;
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

    public String getFechaComprado() {
        return fechaComprado;
    }

    public void setFechaComprado(String fechaComprado) {
        this.fechaComprado = fechaComprado;
    }

    public ArrayList<Producto> getLstProductos() {
        return lstProductos;
    }

    public void setLstProductos(ArrayList<Producto> lstProductos) {
        this.lstProductos = lstProductos;
    }
}
