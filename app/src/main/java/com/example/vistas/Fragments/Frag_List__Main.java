package com.example.vistas.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.DAOs.ListaDAO;
import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.Commons.Codes;
import com.example.vistas.MainActivity;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__ItemOption;

import java.util.ArrayList;

//public class Frag_Product__Main extends Fragment implements InterfaceFragment_AlterProduct {
public class Frag_List__Main extends Fragment implements RVA__ItemOption.Inter_RVA_ItemOption {

    Button btnRegistrar;

    ArrayList<Lista> lstListas;

    RecyclerView rvItemOption;

    public Frag_List__Main() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_list__main, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Lista de compras");

        /* Setting up for next frame*/
        btnRegistrar = view.findViewById(R.id.btn_FrgListMain_createList);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Frag_List__AlterList nextFragment = new Frag_List__AlterList();
                openFragment(nextFragment, Codes.FRAGMENT_FOR_CREATE, null);
            }
        });

        /* Getting list */
        lstListas = new ArrayList<>();
        rvItemOption = view.findViewById(R.id.rv_FragListMain_ProductList);
        updateProductList();

        return view;
    }


    private void updateProductList() {
        lstListas = new ListaDAO(getContext()).select_all_NoComprada();

        Object lstObject = lstListas;

        RVA__ItemOption rva_listado = new RVA__ItemOption(getContext(), lstObject, this, Codes.RV_FOR_EDIT_LISTA);

        rvItemOption.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItemOption.setAdapter(rva_listado);
    }

    //============
    // Moving to other fragment ->
    //============
    private void openFragment(Fragment nextFragment, String fragmentFor, Lista lista) {

        Bundle bundle = new Bundle();

        // Set type of fragment
        bundle.putString(Codes.ARG_NEXT_FRAGMENT_IS_FOR, fragmentFor);

        // Sent product
        if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)) {
            bundle.putSerializable(Codes.ARG_PRODUCT_CLASS, lista);
        }

        nextFragment.setArguments(bundle);

//        ((MainActivity)getActivity()).changefragment(nextFragment, false);

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, nextFragment).addToBackStack("main").commit();
    }
    @Override
    public void openFragment(Lista lista) {
        Frag_List__AlterList nextFragment = new Frag_List__AlterList();

        openFragment(nextFragment, Codes.FRAGMENT_FOR_EDIT, lista);
        
    }

    // Ignore ->
    @Override
    public void removeProduct(Producto producto) {
        /*Keep this empty, its just for the Interface from RVA__ItemOption*/
    }
    @Override
    public void openFragment(Producto producto) {
        /*Keep this empty, its just for the Interface from RVA__ItemOption*/
    }
}