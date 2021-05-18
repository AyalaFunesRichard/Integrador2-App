package com.example.vistas.DTOs;

import java.util.ArrayList;

public class Lista {
    private int idLista;
    private double gasto;
    private String fechaComprado;

    private int estado;
    private String log_fecha_modificado;
    private String log_fecha_creado;

    private ArrayList<Producto> lstProductos;

    /**
     * CONSTRUCTORS ->
     */
    public Lista() {
    }

    /**
     * GETTERS & SETTERS ->
     */
    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
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
