package com.example.vistas.DTOs;

public class Usuario {

    private int idUsario;
    private String correo;
    private String nombreFamiliar;

    private int estado;
    private String log_fecha_creado;
    private String log_fecha_modificado;
    private String log_fecha_ultimaSesion;

    /**
     * CONSTRUCTORS ->
     */
    public Usuario() {
    }

    /**
     * GETTERS & SETTERS ->
     */
    public int getIdUsario() {
        return idUsario;
    }

    public void setIdUsario(int idUsario) {
        this.idUsario = idUsario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombreFamiliar() {
        return nombreFamiliar;
    }

    public void setNombreFamiliar(String nombreFamiliar) {
        this.nombreFamiliar = nombreFamiliar;
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

    public String getLog_fecha_ultimaSesion() {
        return log_fecha_ultimaSesion;
    }

    public void setLog_fecha_ultimaSesion(String log_fecha_ultimaSesion) {
        this.log_fecha_ultimaSesion = log_fecha_ultimaSesion;
    }
}
