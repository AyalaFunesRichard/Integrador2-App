package com.example.vistas.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DAOs.CategoriaDAO;
import com.example.vistas.DAOs.ProductoDAO;
import com.example.vistas.DAOs.Rela_ProductoCategoriaDAO;
import com.example.vistas.DTOs.Categoria;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.Commons.Code_Error;
import com.example.vistas.Commons.Codes;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.DTOs.Rela_ProductoCategoria;
import com.example.vistas.Interfaces.Inter_OnBackPressed;
import com.example.vistas.Interfaces.Inter__RVA__Item_CheckBox;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__CheckBox_Categoria;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Frag_Product__AlterProduct extends Fragment implements Code_Error, Inter__RVA__Item_CheckBox<Categoria>, Inter_OnBackPressed {

    EditText txtNombre;

    RecyclerView recyclerView;
    RVA__CheckBox_Categoria rva_checkbox;

    private ArrayList<Categoria> lstCategoriaOld;
    private String nombreOld;

    private Button btnCancel, btnConfirm;
    private ImageButton iBtnCancelEdit, iBtnConfirmEdit, iBtnDelete;

    private String fragmentFor = null; // recognize this fragment

    private Producto producto = new Producto("-1", "ERROR");

    public Frag_Product__AlterProduct() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product__alter_product, container, false);

        txtNombre = view.findViewById(R.id.eText_fragAlteProd_name);

        recyclerView = view.findViewById(R.id.rv_frgAlterProduc_forCategoria);

        //
        detectThisFragment();
        //
        setUp_ActionBarTitle();
        //
        setUp_Buttons(view);
        //
        setUp_Categorias(view);

        if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)) {
            setUp_EditText(view);
        }

        return view;
    }

    private final View.OnClickListener buttonListener = new View.OnClickListener() {

        public void onClick(View view) {
            String nombre;
            switch (view.getId()) {
                /* ********************************
                 *  For REGISTER product -> -> ->
                 */
                case R.id.btn_FrgAltrProd__register_cancel:
                    popup_return();
                    break;

                case R.id.btn_FrgAltrProd__register_confirm:
                    nombre = getNombreProducto(true);
                    if (nombre.equals(ERROR_EMPTY) || nombre.equals(ERROR_LONG) || nombre.equals(ERROR_REPEATED)) {
                        return;
                    }

                    int estado = Codes.PRODUCTO_ESTADO_ACTIVO;
                    String fechaCreado = new SimpleDateFormat(Code_DB.DATE_FORMART).format(new Date());
                    String fechaEditado = "";

                    Producto newProducto = new Producto("-1", nombre, estado, fechaCreado, fechaEditado);
                    DB_insert_Producto(newProducto);

                    newProducto = new ProductoDAO(getContext()).select_where_nombre(newProducto.getNombre());

                    boolean categoriasOk;
                    for (int i = 0; i < producto.getLstCategoria().size(); i++) {

                        categoriasOk = DB_insert_RelaProductoCategoria(newProducto.getIdProducto(), producto.getLstCategoria().get(i).getIdCategoria());

                        if (!categoriasOk) return;
                    }

                    returnFrame();
                    break;

                /* ********************************
                 * For EDITING product -> -> ->
                 */
                case R.id.iBtn_FrgAltrProd__edit_cancel:

                    popup_return();
                    break;

                case R.id.iBtn_FrgAltrProd__edit_delete:

                    popup_delete();
                    break;

                case R.id.iBtn_FrgAltrProd__edit_confirm:// * For CONFIRM EDITED product ->

                    // * Detect if something was modified ->
                    nombre = getNombreProducto(true);
                    if (nombre.equals(ERROR_EMPTY) || nombre.equals(ERROR_LONG) || nombre.equals(ERROR_REPEATED)) {
                        return;
                    }
                    boolean isNombreModified = !nombre.equals(nombreOld);

                    boolean isCategoriaModified = isCategoriaEdited();
                    // <- Detect if something was modified

                    String message2 = getString(R.string.product_updated_fail); // <- this (text) error should never appear
                    if (!isNombreModified && !isCategoriaModified) {
                        // * No modified ->
                        message2 = getString(R.string.product_updated);
                    } else {
                        boolean nombreOk = true, categoriaOk = true;

                        String newFechaModificado = new SimpleDateFormat(Code_DB.DATE_FORMART).format(new Date());
                        producto.setLog_fecha_moficado(newFechaModificado);

                        // * Producto modified ->
                        if (isNombreModified) {
                            producto.setNombre(nombre);
                            nombreOk = DB_update_Producto(false);
                        }

                        if (isCategoriaModified) {

                            String idProducto = producto.getIdProducto();
                            DB_delete_RelaProdCate(idProducto);

                            for (int i = 0; i < producto.getLstCategoria().size(); i++) {

                                categoriaOk = DB_insert_RelaProductoCategoria(idProducto, producto.getLstCategoria().get(i).getIdCategoria());

                                if (!categoriaOk) return;
                            }
                        }

                        if (nombreOk && categoriaOk) message2 = getString(R.string.product_updated);
                    }
                    Toast.makeText(getContext(), message2, Toast.LENGTH_SHORT).show();

                    returnFrame();
                    break;

                default: {
                    Toast.makeText(getContext(), ERROR_IDENTIFING_ACTION_IN_BUTTON, Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        }
    };

    private void detectThisFragment() {
        fragmentFor = getArguments().getString(Codes.ARG_NEXT_FRAGMENT_IS_FOR);

        if (fragmentFor.isEmpty() || fragmentFor == null) {
            fragmentFor = Codes.FRAGMENT_FOR_CREATE; // <- Default, in case of error when detecting
            return;
        }

        if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)) {
            producto = (Producto) getArguments().getSerializable(Codes.ARG_PRODUCT_CLASS);
            producto.setLstCategoria(new CategoriaDAO(getContext()).select_lstCategoria_where_idProducto(this.producto.getIdProducto()));
        }
    }

    private void setUp_ActionBarTitle() {

        String actionBarTitle = "";

        if (fragmentFor.equals(Codes.FRAGMENT_FOR_CREATE)) {
            actionBarTitle = getResources().getString(R.string.actionBarTitle_AlterProduct_create);
        } else {
            if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)) {
                actionBarTitle = getResources().getString(R.string.actionBarTitle_AlterProduct_edit);
            } else {
                actionBarTitle = ERROR_DETECTING_TITLE_FOR_FRAGMENT;
            }
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(actionBarTitle);
    }

    private void setUp_Buttons(View v) {
        if (fragmentFor.equals(Codes.FRAGMENT_FOR_CREATE)) {

            btnCancel = v.findViewById(R.id.btn_FrgAltrProd__register_cancel);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(buttonListener);

            btnConfirm = v.findViewById(R.id.btn_FrgAltrProd__register_confirm);
            btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setOnClickListener(buttonListener);

            // Gone the others
            v.findViewById(R.id.iBtn_FrgAltrProd__edit_cancel).setVisibility(View.GONE);
            v.findViewById(R.id.iBtn_FrgAltrProd__edit_confirm).setVisibility(View.GONE);
            v.findViewById(R.id.iBtn_FrgAltrProd__edit_delete).setVisibility(View.GONE);
        } else {
            iBtnCancelEdit = v.findViewById(R.id.iBtn_FrgAltrProd__edit_cancel);
            iBtnCancelEdit.setVisibility(View.VISIBLE);
            iBtnCancelEdit.setOnClickListener(buttonListener);

            iBtnConfirmEdit = v.findViewById(R.id.iBtn_FrgAltrProd__edit_confirm);
            iBtnConfirmEdit.setVisibility(View.VISIBLE);
            iBtnConfirmEdit.setOnClickListener(buttonListener);

            iBtnDelete = v.findViewById(R.id.iBtn_FrgAltrProd__edit_delete);
            iBtnDelete.setVisibility(View.VISIBLE);
            iBtnDelete.setOnClickListener(buttonListener);

            // Gone the others
            v.findViewById(R.id.btn_FrgAltrProd__register_cancel).setVisibility(View.GONE);
            v.findViewById(R.id.btn_FrgAltrProd__register_confirm).setVisibility(View.GONE);
        }
    }

    private void setUp_Categorias(View v) {

        CategoriaDAO categoriaDAO = new CategoriaDAO(v.getContext());

        producto.setLstCategoria(categoriaDAO.select_lstCategoria_where_idProducto(producto.getIdProducto()));
        lstCategoriaOld = categoriaDAO.select_lstCategoria_where_idProducto(producto.getIdProducto());

        ArrayList<Categoria> lstAllCategoria = categoriaDAO.select_all();
        //TODO: ordenar aqui primero los productos ya seleccionados y luego los sobrantes
        // TODO: hacer que los check boxes de producto aparezcan arriba, ordenandolos aqui
        //TODO

        rva_checkbox = new RVA__CheckBox_Categoria(getContext(), lstAllCategoria, this.producto, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(rva_checkbox);
    }

    private void setUp_EditText(View v) {
        EditText etAux = v.findViewById(R.id.eText_fragAlteProd_name);
        etAux.setText(producto.getNombre());
        nombreOld = etAux.getText().toString();
    }

    // * /

    private boolean isItemEdited() {
        String nombre;

        if (fragmentFor.equals(Codes.FRAGMENT_FOR_CREATE)) {
            // Detect if TXT_NOMBRE was edited ->
            nombre = getNombreProducto(false);
            if (!nombre.equals(ERROR_EMPTY)) {
                return true;
            }

            // Detect if CATEGORIA was edited ->
            if (producto.getLstCategoria() != null) {
                if (producto.getLstCategoria().size() > 0) {
                    return true;
                }
            }
        } else {
            // Detect if TXT_NOMBRE was edited ->
            nombre = txtNombre.getText().toString().trim();
            if (!nombre.equals(producto.getNombre())) {
                return true;
            }

            return isCategoriaEdited();
        }

        return false;
    }

    private boolean isCategoriaEdited() {

        boolean categoriaEdited = false;

        if (producto.getLstCategoria() != null || lstCategoriaOld != null) {
            int sizeOld = lstCategoriaOld.size();
            int sizeNew = producto.getLstCategoria().size();

            if (sizeNew != sizeOld) {
                categoriaEdited = true;
            } else {
                // Same size, but different content ->
                boolean sameId;
                for (int i = 0; i < sizeOld; i++) {
                    String idStart = lstCategoriaOld.get(i).getIdCategoria();

                    sameId = false;
                    for (int j = 0; j < sizeNew; j++) {
                        if (Objects.equals(idStart, producto.getLstCategoria().get(j).getIdCategoria())) {
                            sameId = true;
                            break;
                        }
                    }

                    if (!sameId) {
                        categoriaEdited = true;
                        break;
                    }
                }
            }

        } else categoriaEdited = true;

        return categoriaEdited;
    }

    // * /

    private void popup_delete() {

        String QUESTION = getResources().getString(R.string.dialog_ques_deleting) + "  " + producto.getNombre() + "\n¿Continuar?";
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.dialog_title_delete))
                .setMessage(QUESTION)
                .setPositiveButton(getResources().getString(R.string.dialog_ans_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DB_delete_Producto();

                        returnFrame();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void popup_return() {

        boolean wasEdited = isItemEdited();

        if (wasEdited) {
            new AlertDialog.Builder(getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(getResources().getString(R.string.dialog_title_back))
                    .setMessage(getResources().getString(R.string.dialog_ques_back_missing_data))
                    .setPositiveButton(getResources().getString(R.string.dialog_ans_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            returnFrame();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        } else {
            returnFrame();
        }
    }

    private void returnFrame() {
        getFragmentManager().popBackStack();
    }

    /* * - - - -
     * DataBase Communication */
    private boolean DB_update_Producto(boolean showToast) {
        boolean rspt = true;
        int code = new ProductoDAO(getContext()).update_where_idProductto(producto);

        String message;
        if (code == Code_DB.SQLITE_ERROR) {
            rspt = false;
            message = getString(R.string.product_updated_fail);
        } else {
            message = getString(R.string.product_updated);
        }

        if (showToast) Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

        return rspt;
    }

    private void DB_delete_Producto() {
        int code = new ProductoDAO(getContext()).delete_where_idProducto(this.producto.getIdProducto());

        String message;
        if (code == Code_DB.SQLITE_ERROR) {
            message = getString(R.string.product_deteled_fail);
        } else {
            message = getString(R.string.product_deleted);
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void DB_insert_Producto(Producto newProducto) {
        int code = new ProductoDAO(getContext()).insert(newProducto, false);

        String message;
        if (code == Code_DB.SQLITE_ERROR) {
            message = getString(R.string.product_registered_fail);
        } else {
            message = getString(R.string.product_registered);
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean DB_insert_RelaProductoCategoria(String idProducto, String idCategoria) {
        boolean ok = true;
        Rela_ProductoCategoria rela = new Rela_ProductoCategoria(idProducto, idCategoria);
        int code = new Rela_ProductoCategoriaDAO(getContext()).insert(rela, false);

        if (code == Code_DB.SQLITE_ERROR) {
            String message = getString(R.string.relaClass_insert_fail);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            ok = false;
        }

        return ok;
    }

    private boolean DB_delete_RelaProdCate(String idProducto) {
        boolean ok = true;
        int code = new Rela_ProductoCategoriaDAO(getContext()).deleteAll_where_idProducto(idProducto);

        if (code == Code_DB.SQLITE_ERROR) {
            String message = getString(R.string.relaClass_delete_fail);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            ok = false;
        }

        return ok;
    }

    // * Validation
    private String getNombreProducto(boolean showToast) {
        String rspt = txtNombre.getText().toString().trim();

        // for Edting and Creating
        if (rspt.isEmpty() || rspt == null) {
            if (showToast)
                Toast.makeText(getContext(), getText(R.string.error_txtNombre_empty), Toast.LENGTH_SHORT).show();
            return ERROR_EMPTY;
        }

        // for Edting and Creating
        if (rspt.length() > Codes.MAX_PRODU_NOMBRE) {
            if (showToast)
                Toast.makeText(getContext(), getText(R.string.error_txtNombre_long), Toast.LENGTH_SHORT).show();
            return ERROR_LONG;
        }

        // Just for edting
        if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT) && rspt.equals(nombreOld)) return rspt;

        // for Edting and Creating
        Producto productoAux = new ProductoDAO(getContext()).select_where_nombre(rspt);
        if (productoAux != null) {
            if (showToast)
                Toast.makeText(getContext(), getText(R.string.error_txtNombre_producto_repeated), Toast.LENGTH_SHORT).show();
            return ERROR_REPEATED;
        }

        return rspt;
    }

    // * INTERFACE ->
    @Override
    public void set_UpdateList(Categoria categoria, boolean isChecked) {
        ArrayList<Categoria> lstAux = producto.getLstCategoria();
        if (isChecked) {
            lstAux.add(categoria);
        } else {
            for (int j = 0; j < lstAux.size(); j++) {
                if (categoria.getIdCategoria() == producto.getLstCategoria().get(j).getIdCategoria()) {

                    lstAux.remove(j);
                    break;
                }
            }
        }
        producto.setLstCategoria(lstAux);
    }

    @Override
    public boolean onBackPressed() {
        new CommonMethods(getContext()).show_toast("alter product");
        return false;
//        if (myCondition) {
//            //action not popBackStack
//            return true;
//        } else {
//            return false;
//        }
    }

}