package com.example.vistas.RV_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.Definitions.Codes;
import com.example.vistas.R;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class RVA__ItemOption extends RecyclerView.Adapter<RVA__ItemOption.MyHolder> implements EventListener {

    Context context;
    String thisIsFor;
    // For Frag_Product__Main.java ->
    ArrayList<Producto> lstProductos = null;
    Inter_RVA_ItemOption interItemOption = null;

    // For FragmentListMain.java ->
    List<Lista> lstListas = null;
//    Inter_RVA_ItemOption interItemOption = null;

    public RVA__ItemOption(Context context, Object lstObjects, Inter_RVA_ItemOption interItemOption, String thisIsFor) {
        this.context = context;

        this.thisIsFor = thisIsFor;

        if (thisIsFor.equals(Codes.RV_FOR_EDIT_LISTA)) {
            this.lstListas = (ArrayList<Lista>) lstObjects;
        } else {
            this.lstProductos = (ArrayList<Producto>) lstObjects;
        }

        this.interItemOption = interItemOption;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_option, viewGroup, false);
        if (lstListas == null) {
            return new MyHolder(v, interItemOption);
        } else {
            return new MyHolder(v, interItemOption);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {
        if (lstListas == null) {
            final Producto productoAux = lstProductos.get(i);
            myHolder.lblItem.setText(productoAux.getNombre());
            myHolder.producto = productoAux;
        } else {
            final Lista listaAux = lstListas.get(i);
            myHolder.lblItem.setText(listaAux.getNombre());
            myHolder.lista = listaAux;
        }

    }

    @Override
    public int getItemCount() {
//        if (lstProductos == null || lstProductos.isEmpty()) {
//            Toast.makeText(context, "No hay registros realizados", Toast.LENGTH_LONG).show();
//        }

        // TODO TODO TODO
        // TODO      TODO
        // TODO TODO TODO
        // * Activar un lbl que diga que aun no hay productos/listas registradas
        //

        if (thisIsFor.equals(Codes.RV_FOR_EDIT_LISTA)) {
            return lstListas.size();
        } else {
            return lstProductos.size();
        }

    }

    //
    // MyHolder ->
    //
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView lblItem;
        ImageButton iBtnOption;

        // For: Frag_Product__Main.java ->
        Producto producto;
        Lista lista;
        Inter_RVA_ItemOption inter_RVAItemOption;

        public MyHolder(@NonNull final View itemView, final Inter_RVA_ItemOption inter_RVAItemOption) {
            super(itemView);

            this.inter_RVAItemOption = inter_RVAItemOption;

            lblItem = itemView.findViewById(R.id.lblRV_ItemName);

            /* Set Buttons in the rv_item_option.xml    order:
             * ---->Setting visible or gone
             * ---->linking buttons
             * */
            if (thisIsFor.equals(Codes.RV_FOR_DELETE_PRODUCTO)) {
                itemView.findViewById(R.id.iBtn_rvItemOptio_edit).setVisibility(View.GONE);
                itemView.findViewById(R.id.iBtn_rvItemOptio_delete).setVisibility(View.VISIBLE);
                //
                iBtnOption = itemView.findViewById(R.id.iBtn_rvItemOptio_delete);
            } else {
                itemView.findViewById(R.id.iBtn_rvItemOptio_delete).setVisibility(View.GONE);
                itemView.findViewById(R.id.iBtn_rvItemOptio_edit).setVisibility(View.VISIBLE);
                //
                iBtnOption = itemView.findViewById(R.id.iBtn_rvItemOptio_edit);
            }

            iBtnOption.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (thisIsFor.equals(Codes.RV_FOR_DELETE_PRODUCTO)) {

                        // TODO TODO TODO
                        // TODO      TODO
                        // TODO TODO TODO descomentar ->
//                        removeProduct(producto);
                    } else {
                        if (thisIsFor.equals(Codes.RV_FOR_EDIT_LISTA)) {
                            inter_RVAItemOption.openFragment(lista);
                        } else {
                            // Edit product ->
                            inter_RVAItemOption.openFragment(producto);
                        }
                    }
                }
            });
        }
    }

    public interface Inter_RVA_ItemOption {

        void openFragment(Producto producto);

        void openFragment(Lista lista);

        //
        void removeProduct(Producto producto);
    }
}
