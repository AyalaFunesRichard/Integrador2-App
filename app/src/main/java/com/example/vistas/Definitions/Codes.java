package com.example.vistas.Definitions;

public class Codes {

    /**
     * Listas ->
     */
    public static final int LISTA_ESTADO_NO_COMPRADA = 1;
    public static final int LISTA_ESTADO_COMPRADA = 2;
    public static final int LISTA_ESTADO_PARCIALMENTE_COMPRADA = 3;
    public static final int LISTA_ESTADO_NO_ACTIVA = 0;
    public static final int MAX_LISTA_NOMBRE = 48; // <- References in.... "string.xml"

    /**
     * Usuario ->
     */
    public static final int USUARIO_ESTADO_ACTIVO = 1;
    public static final int USUARIO_ESTADO_NO_ACTIVO = 0;
    public static final int USUARIO_ESTADO_POR_CONFIRMAR_CORREO = 2;

    /**
     * Producto ->
     */
    public static final int PRODUCTO_ESTADO_NO_ACTIVO = 0;
    public static final int PRODUCTO_ESTADO_ACTIVO = 1;
    public static final int MAX_PRODU_NOMBRE = 48; // <- References in.... "string.xml"

    /**
     * Presupuesto ->
     */
    public static final int PRESUPUESTO_ESTADO_USADO = 0;
    public static final int PRESUPUESTO_ESTADO_ACTIVO = 1;

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

    /**************
     * RECYCLER VIEW -> ->
     */
    /***/
    /* -> rv_ite,_option */
    public static final String RV_FOR_EDIT_PRODUCTO = "edtiProduct";
    public static final String RV_FOR_DELETE_PRODUCTO = "deleteProduct";
    public static final String RV_FOR_EDIT_LISTA = "editList";

    /**************
     * ARGUMENTs -> ->
     */ //  Argument when sending data
    /***/
    public static final String ARG_PRODUCT_CLASS = "product";
    public static final String ARG_NEXT_FRAGMENT_IS_FOR = "fragmentFor";


    /**************
     * ALERT DIALOGs -> ->
     */ //  Type of Alert Dialog
    /***/
    public static final String DIALOG_CONFIRM_EXITING = "dialog_confirm_exit";
    public static final String DIALOG_CONFIRM_BACK_MISSING_DATA = "dialog_confirm_back_missing_data";
    public static final String DIALOG_CONFIRM_DELETING = "dialog_confirm_delete";

}
