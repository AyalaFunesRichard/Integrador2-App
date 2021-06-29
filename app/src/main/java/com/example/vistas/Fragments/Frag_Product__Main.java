package com.example.vistas.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vistas.DAOs.ProductoDAO;
import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.Commons.Codes;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__ItemOption;

import java.util.ArrayList;

//public class Frag_Product__Main extends Fragment implements InterfaceFragment_AlterProduct {
public class Frag_Product__Main extends Fragment implements RVA__ItemOption.Inter_RVA_ItemOption {

    Button btnRegistrar;

    ArrayList<Producto> lstProductos;

    RecyclerView rvItemOption;

    public Frag_Product__Main() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product__main, container, false);

        /* Setting up for next frame*/
        btnRegistrar = view.findViewById(R.id.btn_FrgProMain_registerProduct);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Frag_Product__AlterProduct nextFragment = new Frag_Product__AlterProduct();
                openFragment(nextFragment, Codes.FRAGMENT_FOR_CREATE, null);
            }
        });

        /* Getting list */
        lstProductos = new ArrayList<>();
        rvItemOption = view.findViewById(R.id.rv_FragProdMain_ProductList);
        updateProductList(view);

        return view;
    }


    private void updateProductList(View view) {

        lstProductos = new ProductoDAO(getContext()).select_all();

        Object lstObject = lstProductos;

        RVA__ItemOption rva_listado = new RVA__ItemOption(getContext(), lstObject, this, Codes.RV_FOR_EDIT_PRODUCTO);

        rvItemOption.setLayoutManager(new LinearLayoutManager(getContext()));
        rvItemOption.setAdapter(rva_listado);
    }


    // * Moving to other fragment ->
    private void openFragment(Fragment nextFragment, String fragmentFor, Producto producto) {

        Bundle bundle = new Bundle();

        // Set type of fragment
        bundle.putString(Codes.ARG_NEXT_FRAGMENT_IS_FOR, fragmentFor);

        // Sent product
        if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)) {
            bundle.putSerializable(Codes.ARG_PRODUCT_CLASS, producto);
        }

        nextFragment.setArguments(bundle);

//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.main_fragment_container, nextFragment).addToBackStack("tag").commit();

        FragmentManager fragmentManager = getFragmentManager ();
        fragmentManager.beginTransaction()
                .replace(R.id.main_fragment_container, nextFragment, null)
                .setReorderingAllowed(true)
                .addToBackStack("tag")
                .commit();



//        FragmentTransaction transaction = fragmentManager.beginTransaction();
//        transaction.add()
    }

    @Override
    public void openFragment(Producto producto) {

        Frag_Product__AlterProduct nextFragment = new Frag_Product__AlterProduct();

        openFragment(nextFragment, Codes.FRAGMENT_FOR_EDIT, producto);
    }


    @Override
    public void openFragment(Lista lista) {
        /*Keep this empty, its just for the Interface from RVA__ItemOption*/
    }

    @Override
    public void removeProduct(Producto producto) {
        /*Keep this empty, its just for the Interface from RVA__ItemOption*/

    }

}