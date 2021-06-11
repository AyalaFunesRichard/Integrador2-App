package com.example.vistas.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.vistas.DAOs.CategoriaDAO;
import com.example.vistas.DTOs.Categoria;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__frgCategoria;

import java.util.ArrayList;

public class Frag_Categorias extends Fragment implements RVA__frgCategoria.Inter_RVA_frgCategoria{

    ArrayList<Categoria> lstCategoria;

    RecyclerView recyclerView;

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

        lstCategoria = new ArrayList<>();
        recyclerView = view.findViewById(R.id.rv_FragCategoria);
        updateRecyclerView();

        return view;
    }



    public void updateRecyclerView() {

        lstCategoria = new CategoriaDAO(getContext()).select_all();



//        String txt = "";
//        for (int i = 0; i < lstCategoria.size(); i++) {
//            txt = lstCategoria.get(i).getNombre() + "  " + lstCategoria.get(i).getIdCategoria();
//            Toast.makeText(getContext(), txt, Toast.LENGTH_SHORT).show();
//        }

        RVA__frgCategoria rva_Categoria = new RVA__frgCategoria(getContext(), lstCategoria, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(rva_Categoria);
    }

}