package com.example.vistas.DTOs;

public class Rela_ProductoLista {

    String idRelaPL, idProducto, idLista;

    public Rela_ProductoLista() {
    }

    public String getIdRelaPL() {
        return idRelaPL;
    }

    public void setIdRelaPL(String idRelaPL) {
        this.idRelaPL = idRelaPL;
    }

    public Rela_ProductoLista(String idProducto, String idLista) {
        this.idProducto = idProducto;
        this.idLista = idLista;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getIdLista() {
        return idLista;
    }

    public void setIdLista(String idLista) {
        this.idLista = idLista;
    }
}
