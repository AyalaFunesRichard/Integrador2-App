package com.example.vistas.DTOs;

public class Presupuesto {

    private int idMonto;
    private String fecha;
    private double monto;

    private int estado;
    private String log_fecha_creado;
    private String log_fecha_modificado;

    /**
     * CONSTRUCTORS ->
     */
    public Presupuesto(int idMonto, String fecha, double monto, int estado) {
        this.idMonto = idMonto;
        this.fecha = fecha;
        this.monto = monto;
        this.estado = estado;
    }

    public Presupuesto(int idMonto, String fecha, double monto, int estado, String log_fecha_creado, String log_fecha_modificado) {
        this.idMonto = idMonto;
        this.fecha = fecha;
        this.monto = monto;
        this.estado = estado;
        this.log_fecha_creado = log_fecha_creado;
        this.log_fecha_modificado = log_fecha_modificado;
    }

    /**
     * GETTERS & SETTERS ->
     */
    public int getIdMonto() {
        return idMonto;
    }

    public void setIdMonto(int idMonto) {
        this.idMonto = idMonto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
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
