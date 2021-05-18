package com.example.vistas.Definitions;

public class Codes {

    /**
     * Listas ->
     */
    public static final int LISTA_ESTADO_NO_COMPRADA = 1;
    public static final int LISTA_ESTADO_COMPRADA = 2;
    public static final int LISTA_ESTADO_PARCIALMENTE_COMPRADA = 3;
    public static final int LISTA_ESTADO_NO_ACTIVA = 0;

    /**
     * Usuario ->
     */
    public static final int USUARIO_ESTADO_ACTIVO = 1;
    public static final int USUARIO_ESTADO_NO_ACTIVO = 0;
    public static final int USUARIO_ESTADO_POR_CONFIRMAR_CORREO = 2;

    /**
     * Listas ->
     */
    public static final int PRODUCTO_ESTADO_NO_ACTIVO = 0;
    public static final int PRODUCTO_ESTADO_ACTIVO = 1;

    /**
     * Monto ->
     */
    public static final int MONTO_ESTADO_USADO = 0;
    public static final int MONTO_ESTADO_ACTIVO = 1;

    /**
     * Categorias ->
     */
    public static final int CATEGORIA_ESTADO_USADO = 0;
    public static final int CATEGORIA_ESTADO_ACTIVO = 1;

    /**
     * Log in ->
     */
    public static final int LOGIN_ERROR_WRONG_USERNAME = 0;
    public static final int LOGIN_ERROR_WRONG_PASSWORD = 1;
    public static final int LOGIN_ERROR_USER_BLOCKED = 2;
    public static final int LOGIN_ERROR_USER_NOT_CONFIRMED = 3;

    /**************
     * FRAGMENTs -> ->
     */
    /***/
    /* -> FramentProduct_AlterProduct */
    public static final String FRAGMENT_FOR_EDIT = "edit";
    public static final String FRAGMENT_FOR_CREATE = "create";
}
