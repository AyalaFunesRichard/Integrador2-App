package com.example.vistas.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vistas.DAOs.CategoriaDAO;
import com.example.vistas.DTOs.Categoria;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Commons.Codes;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__frgCategoria;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Frag_Categorias extends Fragment implements RVA__frgCategoria.Inter_RVA_frgCategoria, View.OnClickListener {

    ArrayList<Categoria> lstCategoria;

    RecyclerView recyclerView;
    Button btnRegister;

    private String dialogAnswer = "";

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

    public void alerDialog_Register() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_title_inputCategoiriaName).setMessage(getString(R.string.dialog_ques_inputCategoiriaName));

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton(getString(R.string.dialog_ans_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.setNegativeButton(getString(R.string.dialog_ans_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean cloaseDialog = false;

                dialogAnswer = input.getText().toString().trim();

                if (!dialogAnswer.isEmpty()) {
                    if (dialogAnswer.length() < 40) {
                        cloaseDialog = true;
                    } else {
                        showMessage(getString(R.string.error_txtNombre_long));
                    }
                } else {
                    showMessage(getString(R.string.error_txtNombre_empty));
                }

                if (cloaseDialog) {
                    manageRegister();
                    dialog.dismiss();
                }
            }
        });

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