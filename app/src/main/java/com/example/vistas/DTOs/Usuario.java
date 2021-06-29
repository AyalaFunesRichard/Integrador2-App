package com.example.vistas.DTOs;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String idUsario;
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

    public Usuario(String idUsario, String correo, String nombreFamiliar, int estado, String log_fecha_creado, String log_fecha_modificado, String log_fecha_ultimaSesion) {
        this.idUsario = idUsario;
        this.correo = correo;
        this.nombreFamiliar = nombreFamiliar;
        this.estado = estado;
        this.log_fecha_creado = log_fecha_creado;
        this.log_fecha_modificado = log_fecha_modificado;
        this.log_fecha_ultimaSesion = log_fecha_ultimaSesion;
    }

    /**
     * GETTERS & SETTERS ->
     */
    public String getIdUsario() {
        return idUsario;
    }

    public void setIdUsario(String idUsario) {
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
