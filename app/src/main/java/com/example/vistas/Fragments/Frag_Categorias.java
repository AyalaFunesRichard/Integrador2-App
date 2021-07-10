package com.example.vistas.Fragments;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DAOs.CategoriaDAO;
import com.example.vistas.DAOs.PresupuestoDAO;
import com.example.vistas.DTOs.Categoria;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Commons.Codes;
import com.example.vistas.DTOs.Presupuesto;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__frgCategoria;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Frag_Categorias extends Fragment implements RVA__frgCategoria.Inter_RVA_frgCategoria, View.OnClickListener {

    ArrayList<Categoria> lstCategoria;

    RecyclerView recyclerView;
    Button btnRegister;

    private String dialogAnswer = "";

    CommonMethods cm;

    public Frag_Categorias() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_categorias, container, false);

        cm = new CommonMethods(getContext());

        btnRegister = view.findViewById(R.id.btn_fragCategoria);
        btnRegister.setOnClickListener(this);

        lstCategoria = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_FragCategoria);
        updateRecyclerView();

        return view;
    }

    public void updateRecyclerView() {

        lstCategoria = new CategoriaDAO(getContext()).select_all();

        RVA__frgCategoria rva_Categoria = new RVA__frgCategoria(getContext(), lstCategoria, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(rva_Categoria);
    }

    String newName;

    public void alerDialog_Register() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog__update_name, null);

        TextView lblQuestion = view.findViewById(R.id.lblQuestion);
        Button btnConfirm = view.findViewById(R.id.btnRegister);
        btnConfirm.setText("Registrar");
        Button btnExit = view.findViewById(R.id.btnExit);
        TextInputLayout inputLayout = view.findViewById(R.id.txtLytNombre);
        TextView txtName = view.findViewById(R.id.txtNombre);

        lblQuestion.setText("Ingrese el nombre de la categoria:");

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = txtName.getText().toString();

                dialogAnswer = txtName.getText().toString();
                String name = cm.validate_Nombre(dialogAnswer, false, inputLayout);

                if (name != null) {
                    manageRegister();
                    alertDialog.dismiss();
                }
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    private void manageRegister() {
        String fechaCreado = new SimpleDateFormat(Code_DB.DATE_FORMART).format(new Date());

        Categoria categoria = new Categoria("-1", dialogAnswer, null, Codes.CATEGORIA_ESTADO_ACTIVO, fechaCreado, "");

        db_regiter_Categoria(categoria);
    }

    private void showMessage(String m) {
        Toast.makeText(getContext(), m, Toast.LENGTH_SHORT).show();
    }

    // * Database communication ->
    private void db_regiter_Categoria(Categoria categoria) {
        int rspt = new CategoriaDAO(getContext()).insert(categoria, false);

        if (rspt == Code_DB.SQLITE_ERROR) showMessage("Error registrando Categoria");
        else updateRecyclerView();
    }

    // * View.OnClickListener ->
    @Override
    public void onClick(View view) {
        alerDialog_Register();
    }
}