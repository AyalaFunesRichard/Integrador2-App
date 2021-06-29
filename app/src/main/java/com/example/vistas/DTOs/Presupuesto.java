package com.example.vistas.DTOs;

public class Presupuesto {

//    idPresupuesto TEXT, fecha TEXT, monto INTEGER, estado INT, log_fecha_creado TEXT, log_fecha_modificado TEXT

    private String idPresupuesto;
    private String fecha;
    private double presupuesto;

    private int estado;
    private String log_fecha_creado;
    private String log_fecha_modificado;

    public Presupuesto() {
    }

    public Presupuesto(String idPresupuesto, String fecha, double presupuesto, int estado) {
        this.idPresupuesto = idPresupuesto;
        this.fecha = fecha;
        this.presupuesto = presupuesto;
        this.estado = estado;
    }

    public Presupuesto(String idPresupuesto, String fecha, double presupuesto, int estado, String log_fecha_creado, String log_fecha_modificado) {
        this.idPresupuesto = idPresupuesto;
        this.fecha = fecha;
        this.presupuesto = presupuesto;
        this.estado = estado;
        this.log_fecha_creado = log_fecha_creado;
        this.log_fecha_modificado = log_fecha_modificado;
    }

    /**
     * GETTERS & SETTERS ->
     */
    public String getIdPresupuesto() {
        return idPresupuesto;
    }

    public void setIdPresupuesto(String idPresupuesto) {
        this.idPresupuesto = idPresupuesto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(double presupuesto) {
        this.presupuesto = presupuesto;
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
