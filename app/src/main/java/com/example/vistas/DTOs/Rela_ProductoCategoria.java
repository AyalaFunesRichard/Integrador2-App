package com.example.vistas.DTOs;

public class Rela_ProductoCategoria {

    String idRelaPC, idProducto, idCategoria;

    public Rela_ProductoCategoria() {
    }

    public Rela_ProductoCategoria(String idRelaPC, String idProducto, String idCategoria) {
        this.idRelaPC = idRelaPC;
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
    }

    public Rela_ProductoCategoria(String idProducto, String idCategoria) {
        this.idProducto = idProducto;
        this.idCategoria = idCategoria;
    }

    public String getIdRelaPC() {
        return idRelaPC;
    }

    public void setIdRelaPC(String idRelaPC) {
        this.idRelaPC = idRelaPC;
    }

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(String idCategoria) {
        this.idCategoria = idCategoria;
    }
}
