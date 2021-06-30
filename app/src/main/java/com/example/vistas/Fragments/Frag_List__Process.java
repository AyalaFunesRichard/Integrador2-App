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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vistas.Commons.CommonMethods;
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
import com.example.vistas.MainActivity;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__CheckBox_Producto;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Frag_List__Process extends Fragment implements Inter__RVA__Item_CheckBox<Producto>,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        Code_Error {

    TextView lblNombre;
    EditText txtCosto;
    RecyclerView rvContainer;
    CheckBox checkBox;
    Button btnCancel, btnRegister;

    ArrayList<Producto> original_LstProducto;
    Lista lista;
    RVA__CheckBox_Producto rva_checkbox;

    View view;

    public Frag_List__Process() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_list__process, container, false);

        setUp_variables();
        setUp_name();
        setUp_reycleView();

        return view;
    }

    // ** Set up fragment ->
    private void setUp_variables() {
        lista = (Lista) getArguments().getSerializable(Codes.ARG_LISTA_CLASS);

        original_LstProducto = new ArrayList<>();
        original_LstProducto = (ArrayList<Producto>) lista.getLstProductos().clone();

        lblNombre = view.findViewById(R.id.lbl_frgListProcess__Nombre);

        txtCosto = view.findViewById(R.id.edTxt_FrgListProcess__price);

        rvContainer = view.findViewById(R.id.rv_frgListProcess__ProductsBought);

        btnCancel = view.findViewById(R.id.btn_frgListProcess__cancel);
        btnCancel.setOnClickListener(this);
        btnRegister = view.findViewById(R.id.btn_frgListProcess__register);
        btnRegister.setOnClickListener(this);

        checkBox = view.findViewById(R.id.chkBx_frgListProcess_checkAll);
        checkBox.setOnCheckedChangeListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista comprada");

    }

    private void setUp_name() {
        lblNombre.setText(lista.getNombre());
    }

    private void setUp_reycleView() {
        updateRVAdapter(original_LstProducto);
        areAllChecked();
    }

    // * Related to: Process list ->

    private void manage_ProcessList() {
        Double gasto = getGasto(true);
        if (gasto == null) return;

        if (getLstProducto() == null) return;

        // Set new information ->
        lista.setEstado(Codes.LISTA_ESTADO_COMPRADA);

        String modificado = new SimpleDateFormat(Code_DB.DATE_FORMART).format(new Date());
        lista.setLog_fecha_modificado(modificado);

        lista.setFechaComprado(modificado);

        lista.setGasto(gasto);

        // (update & create) or (update) ->
        if (isList_partially()) {

            Lista newLista = new Lista();

            newLista.setNombre(lista.getNombre());
            newLista.setLog_fecha_creado(lista.getLog_fecha_creado());
            newLista.setEstado(Codes.LISTA_ESTADO_NO_COMPRADA);

            ArrayList<Producto> newLstProducto = new ArrayList<>();
            boolean neverCompared;
            for (Producto p :
                    original_LstProducto) {

                neverCompared = true;
                for (int i = 0; i < lista.getLstProductos().size(); i++) {
                    if (p.getIdProducto() == lista.getLstProductos().get(i).getIdProducto()) {
                        neverCompared = false;
                        break;
                    }
                }

                if (neverCompared) {
                    newLstProducto.add(p);
                }
            }
            newLista.setLstProductos(newLstProducto);

            // * Lista
            // Update -> Lista
            deleteUnlinked_Productos(lista.getLstProductos(), lista.getIdLista());

            db_update_Lista(lista);

            // * newLista
            // Create -> newLista
            db_create_Lista(newLista);
            String id_newLista = new ListaDAO(getContext()).select_where_nombre_estadoNoComprada(newLista.getNombre()).getIdLista();

            // Create -> relaListaProducto (newLista)
            for (Producto p :
                    newLstProducto) {
                db_create_RelaListaProducto(id_newLista, p.getIdProducto());
            }

        } else {
            deleteUnlinked_Productos(lista.getLstProductos(), lista.getIdLista());

            db_update_Lista(lista);
        }

        fragment_done();
    }

    private void deleteUnlinked_Productos(ArrayList<Producto> lstProductos, String idLista) {

        ListaDAO listaDAO = new ListaDAO(getContext());

        ArrayList<Lista> lstAux;

        for (Producto p :
                lstProductos) {

            // Validate how many: Listas (no-compradas) are registered with idProducto
            lstAux = listaDAO.select_where_idProducto_estadoLista(p.getIdProducto(), Codes.LISTA_ESTADO_NO_COMPRADA);

            if (lstAux.size() == 1 && lstAux.get(0).getIdLista() == idLista) {
                db_delete_Producto_whereIdProducto(p.getIdProducto());
            }
        }
    }

    private Double getGasto(boolean showMessage) {

        String text = txtCosto.getText().toString();

        if (text.isEmpty()) {
            if (showMessage) showMessage(getString(R.string.error_txtCosto_empty));
            return null;
        }

        Double costo = null;
        try {
            costo = Double.parseDouble(text);

            if (costo < 0) {
                if (showMessage) showMessage(getString(R.string.error_txtCosto_only_positive));
                return null;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return costo;
    }

    private ArrayList<Producto> getLstProducto() {
        if (lista.getLstProductos().size() == 0 || lista.getLstProductos() == null) {
            showMessage(getString(R.string.error_lstProducto_anySelected));
            return null;
        }
        return lista.getLstProductos();
    }

    private boolean isList_partially() {
        return !(lista.getLstProductos().size() == original_LstProducto.size());
    }

    // * Data base communication ->
    private void db_update_Lista(Lista lista) {
        int rspt = new ListaDAO(getContext()).update_where_idLista(lista);

        if (rspt == Code_DB.SQLITE_ERROR) showMessage(getString(R.string.categoria_updated_fail));
    }

    private void db_create_Lista(Lista lista) {
        int rspt = new ListaDAO(getContext()).insert(lista, false);

        if (rspt == Code_DB.SQLITE_ERROR) showMessage(getString(R.string.categoria_updated_fail));
    }

    private void db_create_RelaListaProducto(String idLista, String idProducto) {
        Rela_ProductoLista rela = new Rela_ProductoLista(idProducto, idLista);
        int rspt = new Rela_ListaProductoDAO(getContext()).insert(rela, false);

        if (rspt == Code_DB.SQLITE_ERROR) showMessage(getString(R.string.categoria_updated_fail));
    }

    private void db_deleteAll_RelaListaProducto(String idLista) {
        int rspt = new Rela_ListaProductoDAO(getContext()).delete_where_idLista(idLista);

        if (rspt == Code_DB.SQLITE_ERROR) showMessage(getString(R.string.categoria_updated_fail));
    }

    private void db_delete_Producto_whereIdProducto(String idProducto) {
        int rspt = new ProductoDAO(getContext()).delete_where_idProducto(idProducto);

        if (rspt == Code_DB.SQLITE_ERROR) showMessage(getString(R.string.categoria_updated_fail));
    }

    // ***

    private void updateRVAdapter(ArrayList<Producto> lstChecked) {
        rva_checkbox = new RVA__CheckBox_Producto(getContext(), lstChecked, this);

        rvContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContainer.setAdapter(rva_checkbox);
    }

    private void checkAll() {
        updateRVAdapter(original_LstProducto);
    }

    private void areAllChecked() {
        checkBox.setChecked((original_LstProducto.size() == lista.getLstProductos().size()));
    }

    // ***

    private void manage_Return() {

        if (lista.getLstProductos().size() == original_LstProducto.size() &&
                getGasto(false) == null) {
            fragment_return();
        } else {
            dialog_confirmExit();
        }
    }

    private void dialog_confirmExit() {
        new AlertDialog.Builder(getContext())
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle(getResources().getString(R.string.dialog_title_back))
                .setMessage(getResources().getString(R.string.dialog_ques_back_missing_data))
                .setPositiveButton(getResources().getString(R.string.dialog_ans_confirm), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fragment_return();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void fragment_return() {
        getFragmentManager().popBackStack();
    }

    private void fragment_done() {

//        ((MainActivity)getActivity()).changefragment(new Frag_Economy__Main(), true);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, new Frag_Economy__Main()).addToBackStack("main").commit();
    }

    // ***

    private void showMessage(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
    }

    // * Inter__RVA__Item_CheckBox ->
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

        areAllChecked();
    }

    // * CompoundButton.OnCheckedChangeListener ->
    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        if (b) checkAll();
        areAllChecked();
    }

    // * View.OnClickListener ->
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_frgListProcess__register: {
                manage_ProcessList();
                break;
            }
            case R.id.btn_frgListProcess__cancel: {
                manage_Return();
                break;
            }
            default:
                showMessage(ERROR_IDENTIFING_ACTION_IN_BUTTON);
                break;
        }
    }

//    @Override
//    public boolean onBackPressed() {
//        new CommonMethods(getContext()).show_toast("alter product");
//        return false;
////        if (myCondition) {
////            //action not popBackStack
////            return true;
////        } else {
////            return false;
////        }
//    }
}