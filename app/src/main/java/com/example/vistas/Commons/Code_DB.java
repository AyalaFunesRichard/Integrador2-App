package com.example.vistas.Commons;

public interface Code_DB {

    // DATA BASE
    String DATABASE_NAME = "DB_MeFalta";

    // TABLEs
    String TABLE_PRODUCTO = "Producto";
    String TABLE_USUARIO = "Usuario";
    String TABLE_LISTA = "Lista";
    String TABLE_PRESUPUESTO = "Presupuesto";
    String TABLE_CATEGORIA = "Categoria";
    // Tables many-to-many
    String TABLE_PRODUCTO_LISTA = "Producto_Lista";
    String TABLE_PRODUCTO_CATEGORIA = "Producto_Categoria";

    // Common attributes
    String ESTADO = "estado";
    String LOG_FECHA_MODIFICADO = "log_fecha_modificado";
    String LOG_FECHA_CREADO = "log_fecha_creado";
    String LOG_FECHA_ULTIMA_SESION = "log_fecha_ultimaSesion";

    // Date
    String DATE_FORMART = "YYYY-MM-DD HH:MM:SS";

    // Result and Error codes
    int SQLITE_ERROR = -1;
}
