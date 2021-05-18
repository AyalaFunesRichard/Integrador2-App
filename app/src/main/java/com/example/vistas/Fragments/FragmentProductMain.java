package com.example.vistas.Fragments;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.vistas.DTOs.Producto;
import com.example.vistas.Definitions.Codes;
import com.example.vistas.InterfaceFragment_AlterProduct;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA_ItemOption;

import java.util.ArrayList;

public class FragmentProductMain extends Fragment implements InterfaceFragment_AlterProduct {

    Button btnRegistrar;

    ArrayList<Producto> lstProductos;

    RecyclerView rvProducts;

    public FragmentProductMain() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_main, container, false);

        /* Setting up for next frame*/
        btnRegistrar = view.findViewById(R.id.btn_FrgProMain_registerProduct);
        btnRegistrar.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentProduct_AlterProduct nextFragment = new FragmentProduct_AlterProduct();
                openFragment(nextFragment, Codes.FRAGMENT_FOR_CREATE);
            }
        });

        /* Getting list */
        lstProductos = new ArrayList<>();
        rvProducts = view.findViewById(R.id.rv_FragProdMain_ProductList);
        updateProductList(view);

        return view;
    }

    /*
    * ir otra frag
    *   -> si CREAR ->
    *   -> si EDITAR -> productoId
    *   enviar tipo frag
    * */
    // Moving to another fragment
    @Override
    private void openFragment(Fragment nextFragment, String fragmentFor, Producto producto) {

        Bundle bundle = new Bundle();
        bundle.putString("fragmentFor", fragmentFor);
//        if(fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)){
//            bundle.putString("product", );
//        }

        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        nextFragment.setArguments(bundle);
        transaction.replace(R.id.main_fragment_container, nextFragment).commit();

        //
        //

//        nextFragment.setArguments(bundle);
//
//        FragmentManager fragmentManager = ((AppCompatActivity)).getSupportFragmentManager();
//        fragmentManager.beginTransaction()
//                .replace(R.id.main_fragment_container, FragmentProduct_AlterProduct.class, null)
//                .setReorderingAllowed(true)
//                .addToBackStack("name") // name can be null
//                .commit();
    }

    private void updateProductList(View view){
        lstProductos.add(new Producto(1, "Manzanas"));
        lstProductos.add(new Producto(2, "Peras"));
        lstProductos.add(new Producto(3, "Platanos"));
        lstProductos.add(new Producto(4, "Arroz"));
        lstProductos.add(new Producto(5, "Menestra"));
        lstProductos.add(new Producto(6, "Galletas perro"));
        lstProductos.add(new Producto(7, "Pastilla dolor cabeza"));
        lstProductos.add(new Producto(8, "Ibuprofeno"));
        lstProductos.add(new Producto(9, "Galleta gato"));
        lstProductos.add(new Producto(10, "Maiz tordo"));
        lstProductos.add(new Producto(11, "Queso"));
        lstProductos.add(new Producto(9, "Galleta gato"));
        lstProductos.add(new Producto(10, "Maiz tordo"));
        lstProductos.add(new Producto(11, "Queso"));

        RVA_ItemOption rva_listado = new RVA_ItemOption(getContext(), lstProductos);

        rvProducts.setLayoutManager(new LinearLayoutManager(getContext()));
        rvProducts.setAdapter(rva_listado);
    }
}