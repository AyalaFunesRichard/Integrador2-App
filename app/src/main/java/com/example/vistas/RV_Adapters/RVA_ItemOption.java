package com.example.vistas.RV_Adapters;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.DTOs.Producto;
import com.example.vistas.Definitions.Codes;
import com.example.vistas.Fragments.FragmentProductMain;
import com.example.vistas.Fragments.FragmentProduct_AlterProduct;
import com.example.vistas.R;

import java.util.List;

public class RVA_ItemOption extends RecyclerView.Adapter<RVA_ItemOption.MyHolder> {
    List<Producto> lstProductos;
    Context context;

    public RVA_ItemOption(Context context, List<Producto> lstProductos) {
        this.context = context;
        this.lstProductos = lstProductos;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_option, viewGroup, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        final Producto productoAux = lstProductos.get(i);

        myHolder.lblProduct.setText(productoAux.getNombre());

        myHolder.producto = productoAux;
    }

    @Override
    public int getItemCount() {
        if (lstProductos == null || lstProductos.isEmpty()) {
            Toast.makeText(context, "No hay registros realizados", Toast.LENGTH_LONG).show();
        }

        return lstProductos.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView lblProduct;
        ImageButton iBtnEdit;
        Producto producto;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            lblProduct = itemView.findViewById(R.id.lblRV_ItemName);

            iBtnEdit = itemView.findViewById(R.id.iBtnRV_edit);
            iBtnEdit.findViewById(R.id.iBtnRV_edit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    Log.d("deno", "NOMBRE: "  + producto.getNombre());


//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//
//                    nextFragment.setArguments(bundle);
//                    transaction.replace(R.id.main_fragment_container, nextFragment).commit();


//                    Toast.makeText(itemView.getContext(), "Clicked Card...", Toast.LENGTH_LONG).show();
//
//                    ShareFragment newFragment = new ShareFragment();
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.viewFragments, newFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
//                    /**/
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Fragment myFragment = new MyFragment();
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();

//                    Toast.makeText(itemView.getContext(), "Clicked Card...", Toast.LENGTH_LONG).show();

//                    FragmentProduct_AlterProduct newFragment = new FragmentProduct_AlterProduct();
//                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.viewFragments, newFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();


//                    FragmentManager fragmentManager = getSupportFragmentManager();
//                    FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
//                    fragmentManager.beginTransaction()
//                            .replace(R.id.main_fragment_container, FragmentProduct_AlterProduct.class, null)
//                            .setReorderingAllowed(true)
//                            .addToBackStack("name") // name can be null
//                            .commit();
                    /**/
//                    AppCompatActivity activity = (AppCompatActivity) view.getContext();
//                    Fragment myFragment = new MyFragment();
//                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, myFragment).addToBackStack(null).commit();
                    //
                    //
                    //
                    //        FragmentTransaction transaction = getFragmentManager().beginTransaction();

                    //////////////////////////////////////////////////////////////////////////////////////////////////////////
//                    Bundle bundle = new Bundle();
//                    bundle.putString("fragmentFor", Codes.FRAGMENT_FOR_EDIT);
//                    FragmentProduct_AlterProduct nextFragment = new FragmentProduct_AlterProduct();
//                    nextFragment.setArguments(bundle);
//
//                    FragmentTransaction transaction = ((AppCompatActivity) itemView.getActivity()).getFragmentManager().beginTransaction();
//                    transaction.replace(R.id.main_fragment_container, nextFragment).commit();
//
//                    Toast.makeText(itemView.getContext(), "boton " + producto.getIdProducto(), Toast.LENGTH_SHORT).show();
                    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
                    FragmentManager manager = itemView.getContext().getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.bodyfragment, AnotherFragment.newInstance()); // newInstance() is a static factory method.
                    transaction.commit();
                }
            });
        }
    }

}
