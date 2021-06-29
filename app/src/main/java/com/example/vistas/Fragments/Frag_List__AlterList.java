package com.example.vistas.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.vistas.DAOs.ListaDAO;
import com.example.vistas.DAOs.ProductoDAO;
import com.example.vistas.DAOs.Rela_ListaProductoDAO;
import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Commons.Code_Error;
import com.example.vistas.Commons.Codes;
import com.example.vistas.DTOs.Rela_ProductoLista;
import com.example.vistas.Interfaces.Inter_OnBackPressed;
import com.example.vistas.Interfaces.Inter__RVA__Item_CheckBox;
import com.example.vistas.Interfaces.Inter__RVA__One_Item;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__CheckBox_Producto;
import com.example.vistas.RV_Adapters.RVA__OneItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class Frag_List__AlterList extends Fragment implements Code_Error, Inter__RVA__Item_CheckBox<Producto>,
        Inter__RVA__One_Item, SearchView.OnQueryTextListener,
        SearchView.OnCloseListener, View.OnFocusChangeListener,
        Inter_OnBackPressed {

    private String fragmentFor = null; // recognize this fragment

    RecyclerView rvProductos_selected;
    RecyclerView rvProductos_searched;
    RVA__CheckBox_Producto rva_checkbox;
    RVA__OneItem rva_oneItem;

    private EditText txtNombre;
    private Button btnCancel, btnConfirm;
    private ImageButton iBtnCancelEdit, iBtnConfirmEdit, iBtnDelete, iBtnListReady;
    private SearchView txtSearch;

    Lista lista = null;

    // Compare when return
    private ArrayList<Producto> lstProductoOld;
    private String nombreOld;  // <-Compare when return

    // For searching ->
    ArrayList<Producto> lstAllProducto;

    public Frag_List__AlterList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list__alter_list, container, false);

        txtNombre = view.findViewById(R.id.eTxt_FragListAlter_name);

        rvProductos_selected = view.findViewById(R.id.rv_FragAlterList);
        rvProductos_searched = view.findViewById(R.id.rv_searchResult);

        txtSearch = view.findViewById(R.id.srchVw_FragAlterList_findProduct);
        txtSearch.setOnQueryTextListener(this);
        txtSearch.setOnQueryTextFocusChangeListener(this);
        txtSearch.setOnCloseListener(this);
        DB_update_searchProductList();

        lista = new Lista();
        lista.setLstProductos(new ArrayList<>());

        //
        detectThisFragment();
        //
        setUp_ActionBarTitle();
        //
        setUp_Buttons(view);

        if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)) {
            setUp_EditText(view);

            setUp_Productos();
        }

        return view;
    }

    private final View.OnClickListener buttonListener = new View.OnClickListener() {
        public void onClick(View view) {
            String nombre;
            switch (view.getId()) {
                // *********************************
                // * For REGISTER list -> -> ->
                //
                case R.id.btn_FrgAltrList__create_cancel:
                    popup_return();
                    break;
                case R.id.btn_FrgAltrList__create_confirm:
                    nombre = getNombreLista(true);
                    if (nombre.equals(ERROR_EMPTY) || nombre.equals(ERROR_LONG) || nombre.equals(ERROR_REPEATED)) {
                        return;
                    }

                    double gasto = -1;
                    String fechaComprado = "";
                    int estado = Codes.LISTA_ESTADO_NO_COMPRADA;
                    String fechaCreado = new SimpleDateFormat(Code_DB.DATE_FORMART).format(new Date());
                    String fechaEditado = "";

                    Lista newLista = new Lista("-1", nombre, gasto, fechaComprado, estado, fechaCreado, fechaEditado);
                    DB_insert_Lista(newLista);
                    // <- Register the Lista (no, Producto)

                    // Recover new Lista
                    newLista = new ListaDAO(getContext()).select_where_nombre(newLista.getNombre());

                    boolean productoOk;
                    if (lista.getLstProductos() != null) {
                        for (int i = 0; i < lista.getLstProductos().size(); i++) {

                            productoOk = DB_insert_RelaListaProducto(lista.getLstProductos().get(i).getIdProducto(), newLista.getIdLista());

                            if (!productoOk) return;
                        }
                    }

                    returnFrame();
                    break;

                // *********************************
                // * For UPDATING list -> -> ->
                //
                case R.id.iBtn_FragAlterList__edit_cancel:
                    popup_return();
                    break;

                case R.id.iBtn_FragAlterList__edit_delete:
                    popup_delete();
                    break;

                case R.id.iBtn_FragAlterList__edit_confirm:// * For CONFIRM EDITED list ->

                    // Detect if something was modified
                    nombre = getNombreLista(true);
                    if (nombre.equals(ERROR_EMPTY) || nombre.equals(ERROR_LONG) || nombre.equals(ERROR_REPEATED))
                        return;

                    boolean isNombreModified = !nombre.equals(nombreOld);

                    boolean isProductoModified = isProductoEdited();
                    // <- Detect if something was modified

                    String message2 = getString(R.string.list_updated_fail); // <- this (text) error should never appear
                    if (!isNombreModified && !isProductoModified) {
                        // * No modified ->
                        message2 = getString(R.string.list_updated);
                    } else {
                        boolean nombreOk = true, categoriaOk = true;

                        String newFechaModificado = new SimpleDateFormat(Code_DB.DATE_FORMART).format(new Date());
                        lista.setLog_fecha_modificado(newFechaModificado);

                        // * Producto modified ->
                        if (isNombreModified) {
                            lista.setNombre(nombre);
                            nombreOk = DB_update_Lista(false);
                        }

                        if (isProductoModified) {
//                            String idLista = lista.getIdLista();
                            int lstNewSize = lista.getLstProductos().size(), lstOldSize = lstProductoOld.size();
                            String idOld, idNew;
                            boolean done;
                            // * DELETE the old ones
                            for (int i = 0; i < lstOldSize; i++) {
                                idOld = lstProductoOld.get(i).getIdProducto();

                                done = false;
                                idNew = "-1";
                                for (int j = 0; j < lstNewSize; j++) {
                                    idNew = lista.getLstProductos().get(j).getIdProducto();

                                    if (idOld == idNew) {
                                        done = true;
                                        break;
                                    }
                                }

                                if (!done) {
                                    boolean rspt = DB_delete_RelaListProducto(idOld);
                                    if (!rspt) { // this is in case "DB_delete_RelaListProducto(idOld);" goes wrong
                                        categoriaOk = false;
                                        break;
                                    }
                                }
                            }

                            // * REGISTER the new ones
                            for (int i = 0; i < lstNewSize && categoriaOk; i++) {
                                idNew = lista.getLstProductos().get(i).getIdProducto();

                                done = false;
                                idOld = "-1";
                                for (int j = 0; j < lstOldSize; j++) {
                                    idOld = lstProductoOld.get(j).getIdProducto();

                                    if (idOld == idNew) {
                                        done = true;
                                        break;
                                    }
                                }

                                if (!done) {
                                    boolean rspt = DB_insert_RelaListaProducto(idNew, lista.getIdLista());
                                    if (!rspt) { // this is in case "DB_delete_RelaListProducto(idOld);" goes wrong
                                        categoriaOk = false;
                                        break;
                                    }
                                }
                            }
                        }

                        if (nombreOk && categoriaOk) message2 = getString(R.string.product_updated);
                    }
                    Toast.makeText(getContext(), message2, Toast.LENGTH_SHORT).show();

                    returnFrame();
                    break;
                case R.id.iBtn_FragAlterList__edit_ready:

                    // Validate if there are Productos checked
                    if(lista.getLstProductos().isEmpty()) {
                        String msg = lista.getNombre() + " debe de tener por lo menos, un producto asociado.";
                        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Frag_List__Process nextFragment = new Frag_List__Process();

                    Bundle bundle = new Bundle();

                    bundle.putSerializable(Codes.ARG_LISTA_CLASS, lista);
                    nextFragment.setArguments(bundle);

                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    transaction.replace(R.id.main_fragment_container, nextFragment).addToBackStack("tag").commit();
                    break;

                default:
                    Toast.makeText(getContext(), ERROR_IDENTIFING_ACTION_IN_BUTTON, Toast.LENGTH_SHORT).show();
                    break;
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
            lista = (Lista) getArguments().getSerializable(Codes.ARG_PRODUCT_CLASS);
        }
    }

    private void setUp_ActionBarTitle() {

        String actionBarTitle = "";

        if (fragmentFor.equals(Codes.FRAGMENT_FOR_CREATE)) {
            actionBarTitle = getResources().getString(R.string.actionBarTitle_AlterList_create);
        } else {
            if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)) {
                actionBarTitle = getResources().getString(R.string.actionBarTitle_AlterList_edit);
            } else {
                actionBarTitle = "Er. detecting title";
            }
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(actionBarTitle);
    }

    private void setUp_Buttons(View view) {
        if (fragmentFor.equals(Codes.FRAGMENT_FOR_CREATE)) {
            // Bottom buttons ->
            btnCancel = view.findViewById(R.id.btn_FrgAltrList__create_cancel);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(buttonListener);

            btnConfirm = view.findViewById(R.id.btn_FrgAltrList__create_confirm);
            btnConfirm.setVisibility(View.VISIBLE);
            btnConfirm.setOnClickListener(buttonListener);

            // Gone the others
            view.findViewById(R.id.iBtn_FragAlterList__edit_confirm).setVisibility(View.GONE);
            view.findViewById(R.id.iBtn_FragAlterList__edit_delete).setVisibility(View.GONE);
            view.findViewById(R.id.iBtn_FragAlterList__edit_ready).setVisibility(View.GONE);
            view.findViewById(R.id.iBtn_FragAlterList__edit_cancel).setVisibility(View.GONE);
        } else {
            // Bottom buttons ->
            iBtnCancelEdit = view.findViewById(R.id.iBtn_FragAlterList__edit_cancel);
            iBtnCancelEdit.setVisibility(View.VISIBLE);
            iBtnCancelEdit.setOnClickListener(buttonListener);

            iBtnConfirmEdit = view.findViewById(R.id.iBtn_FragAlterList__edit_confirm);
            iBtnConfirmEdit.setVisibility(View.VISIBLE);
            iBtnConfirmEdit.setOnClickListener(buttonListener);

            iBtnDelete = view.findViewById(R.id.iBtn_FragAlterList__edit_delete);
            iBtnDelete.setVisibility(View.VISIBLE);
            iBtnDelete.setOnClickListener(buttonListener);

            iBtnDelete = view.findViewById(R.id.iBtn_FragAlterList__edit_ready);
            iBtnDelete.setVisibility(View.VISIBLE);
            iBtnDelete.setOnClickListener(buttonListener);

            // Gone the others
            view.findViewById(R.id.btn_FrgAltrList__create_cancel).setVisibility(View.GONE);
            view.findViewById(R.id.btn_FrgAltrList__create_confirm).setVisibility(View.GONE);
        }
    }

    private void setUp_Productos() {

        ProductoDAO productoDAO = new ProductoDAO(getContext());

        lista.setLstProductos(productoDAO.select_where_idLista_estadoLista(lista.getIdLista()));
        lstProductoOld = productoDAO.select_where_idLista_estadoLista(lista.getIdLista());

        update_ProductListed(lista.getLstProductos());
    }

    private void setUp_EditText(View v) {
        EditText etAux = v.findViewById(R.id.eTxt_FragListAlter_name);
        etAux.setText(lista.getNombre());
        nombreOld = etAux.getText().toString();
    }

    private void update_ProductListed(ArrayList<Producto> lstProductoListed) {
        rva_checkbox = new RVA__CheckBox_Producto(getContext(), lstProductoListed, this);

        rvProductos_selected.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductos_selected.setAdapter(rva_checkbox);
    }

    // * /

    private void popup_return() {

        boolean wasEdited = isListaEdited();

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

    private void popup_delete() {

        String QUESTION = getResources().getString(R.string.dialog_ques_deleting) + "  " + lista.getNombre() + "\n¿Continuar?";
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.dialog_title_delete))
                .setMessage(QUESTION)
                .setPositiveButton(getResources().getString(R.string.dialog_ans_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DB_delete_Lista();

                        returnFrame();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void returnFrame() {
        getFragmentManager().popBackStack();
    }

    // * Validation to change view ->  /
    private boolean isListaEdited() {
        String nombre;

        if (fragmentFor.equals(Codes.FRAGMENT_FOR_CREATE)) {
            // Detect if TXT_NOMBRE was edited ->
            nombre = getNombreLista(false);
            if (!nombre.equals(ERROR_EMPTY)) {
                return true;
            }

            // Detect if PRODUCTO was edited ->
            if (lista.getLstProductos() != null) {
                if (lista.getLstProductos().size() > 0) {
                    return true;
                }
            }
        } else {
            // Detect if TXT_NOMBRE was edited ->
            nombre = txtNombre.getText().toString().trim();
            if (!nombre.equals(nombreOld)) {
                return true;
            }

            return isProductoEdited();
        }

        return false;
    }

    private boolean isProductoEdited() {

        boolean productosEdited = false;

        if (lista.getLstProductos() != null || lstProductoOld != null) {
            int sizeOld = lstProductoOld.size();
            int sizeNew = lista.getLstProductos().size();

            if (sizeNew != sizeOld) {
                productosEdited = true;
            } else {
                // Same size, but different content ->
                boolean sameId;
                for (int i = 0; i < sizeOld; i++) {
                    String idStart = lstProductoOld.get(i).getIdProducto();

                    sameId = false;
                    for (int j = 0; j < sizeNew; j++) {
                        if (idStart.equals(lista.getLstProductos().get(j).getIdProducto())) {
                            sameId = true;
                            break;
                        }
                    }

                    if (!sameId) {
                        productosEdited = true;
                        break;
                    }
                }
            }

        } else productosEdited = true;

        return productosEdited;
    }

    // * Search producto ->  /
    private void searchProducto(String searchString) {

        if (searchString.isEmpty()) {
            update_searchViewAdapter(new ArrayList<Producto>());
            return;
        }

        update_searchViewAdapter(filter(lstAllProducto, searchString));
    }

    private ArrayList<Producto> filter(ArrayList<Producto> lstSearched, final String searchString) {

        ArrayList<Producto> lstRspt = new ArrayList<>();

        List<Producto> auxLst = lstSearched.stream()
                .filter(i -> i.getNombre().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());

        lstRspt.clear();
        lstRspt.addAll(auxLst);

        return lstRspt;
    }

    private void update_searchViewAdapter(ArrayList<Producto> lstProductoReady) {

        rva_oneItem = new RVA__OneItem(getContext(), lstProductoReady, this);

        rvProductos_searched.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProductos_searched.setAdapter(rva_oneItem);
    }

    // * DataBase Communication */
    private boolean DB_update_Lista(boolean showToast) {
        boolean rspt = true;
        int code = new ListaDAO(getContext()).update_where_idLista(lista);

        String message;
        if (code == Code_DB.SQLITE_ERROR) {
            rspt = false;
            message = getString(R.string.list_updated_fail);
        } else {
            message = getString(R.string.list_updated);
        }

        if (showToast) Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

        return rspt;
    }

    private void DB_insert_Lista(Lista newLista) {
        int code = new ListaDAO(getContext()).insert(newLista, false);

        String message;
        if (code == Code_DB.SQLITE_ERROR) {
            message = getString(R.string.list_updated_fail);
        } else {
            message = getString(R.string.list_updated);
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean DB_insert_RelaListaProducto(String idProducto, String idLista) {
        boolean ok = true;
        Rela_ProductoLista rela = new Rela_ProductoLista(idProducto, idLista);
        int code = new Rela_ListaProductoDAO(getContext()).insert(rela, false);

        if (code == Code_DB.SQLITE_ERROR) {
            Toast.makeText(getContext(), getString(R.string.relaClass_insert_fail), Toast.LENGTH_SHORT).show();
            ok = false;
        }

        return ok;
    }

    private void DB_delete_Lista() {
        int code = new ListaDAO(getContext()).delete_where_idLista(this.lista.getIdLista());

        String message;
        if (code == Code_DB.SQLITE_ERROR) {
            message = getString(R.string.list_deteled_fail);
        } else {
            message = getString(R.string.product_deleted);
        }
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private boolean DB_delete_RelaListProducto(String idProducto) {
        boolean ok = true;
        int code = new Rela_ListaProductoDAO(getContext()).delete_where_idP_idL(idProducto, lista.getIdLista());

        if (code == Code_DB.SQLITE_ERROR) {
            Toast.makeText(getContext(), getString(R.string.relaClass_delete_fail), Toast.LENGTH_SHORT).show();
            ok = false;
        }

        return ok;
    }

    private void DB_update_searchProductList() {
        lstAllProducto = DB_select_Producto();
    }

    private ArrayList<Producto> DB_select_Producto() {
        return new ProductoDAO(getContext()).select_all();
    }

    // * Validation
    private String getNombreLista(boolean showToast) {
        String rspt = txtNombre.getText().toString().trim();

        // for Edting and Creating
        if (rspt.isEmpty() || rspt == null) {
            if (showToast)
                Toast.makeText(getContext(), getText(R.string.error_txtNombre_empty), Toast.LENGTH_SHORT).show();
            return ERROR_EMPTY;
        }

        // for Edting and Creating
        if (rspt.length() > Codes.MAX_LISTA_NOMBRE) {
            if (showToast)
                Toast.makeText(getContext(), getText(R.string.error_txtNombre_long), Toast.LENGTH_SHORT).show();
            return ERROR_LONG;
        }

        // Just for edting
        if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT) && rspt.equals(lista.getNombre()))
            return rspt;

        // for Edting and Creating
        Lista categoriaAux = new ListaDAO(getContext()).select_where_nombre(rspt);
        if (categoriaAux != null) {
            if (showToast)
                Toast.makeText(getContext(), getText(R.string.error_txtNombre_lista_repeated), Toast.LENGTH_SHORT).show();
            return ERROR_REPEATED;
        }

        return rspt;
    }

    // * INTERFACE -> Inter__RVA__Item_CheckBox<Producto> ->
    @Override
    public void set_UpdateList(Producto producto, boolean isChecked) {
        ArrayList<Producto> lstAux = lista.getLstProductos();
        if (isChecked) {
            lstAux.add(producto);
        } else {
            for (int j = 0; j < lstAux.size(); j++) {
                if (producto.getIdProducto() == lista.getLstProductos().get(j).getIdProducto()) {

                    lstAux.remove(j);
                    break;
                }
            }
        }
        lista.setLstProductos(lstAux);
    }

    // * INTERFACE -> Inter__RVA__One_Item ->
    @Override
    public void productSelected(Producto productoSelected) {
        set_UpdateList(productoSelected, true);

        update_ProductListed(lista.getLstProductos());

        txtSearch.setIconified(true);
    }

    // * SearchView.OnQueryTextListener ->
    @Override
    public boolean onQueryTextSubmit(String s) {

        searchProducto(s);

        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {

        searchProducto(s);

        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {

        if (!b) {
            update_searchViewAdapter(new ArrayList<>());
        } else {
            searchProducto(txtSearch.getQuery().toString());
        }
    }

    @Override
    public boolean onClose() {
        txtSearch.clearFocus();
        update_searchViewAdapter(new ArrayList<>());

        return false;
    }

    // * Inter_OnBackPressed ->
    @Override
    public boolean onBackPressed() {
//        popup_return();
        return true;
    }
}