package com.example.vistas.RV_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.DTOs.Categoria;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.Interfaces.Inter__RVA__Item_CheckBox;
import com.example.vistas.R;

import java.util.ArrayList;
import java.util.EventListener;

public class RVA__CheckBox_Categoria extends RecyclerView.Adapter<RVA__CheckBox_Categoria.MyHolder> implements EventListener {

    Context context;

//    public static boolean[] lstCheckBox;

    ArrayList<Categoria> lst_AllCategoria = null;

    private Producto productoAux;
    private Inter__RVA__Item_CheckBox rvFrgCheckBox;

    public RVA__CheckBox_Categoria(Context context, ArrayList<Categoria> lst_AllCategoria, Producto productoAux, Inter__RVA__Item_CheckBox rvFrgCheckBox) {
        this.context = context;
        this.lst_AllCategoria = lst_AllCategoria;
        this.productoAux = productoAux;
        this.rvFrgCheckBox = rvFrgCheckBox;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_checkbox, viewGroup, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        final Categoria categoria = lst_AllCategoria.get(i);

        myHolder.lblItem.setText(categoria.getNombre());

        boolean chBxStatus = false;

        for (int j = 0; j < productoAux.getLstCategoria().size(); j++) {
            if (categoria.getIdCategoria().equals(productoAux.getLstCategoria().get(j).getIdCategoria())) {
                chBxStatus = true;
                break;
            }
        }

        myHolder.checkBox.setOnCheckedChangeListener(null);
        myHolder.checkBox.setChecked(chBxStatus);
        myHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rvFrgCheckBox.set_UpdateList(categoria, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lst_AllCategoria.size();
    }

    //
    // MyHolder ->
    //
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView lblItem;
        CheckBox checkBox;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            lblItem = itemView.findViewById(R.id.lblRV_ItemCheck);

            checkBox = itemView.findViewById(R.id.chcBx_rvItemCheck);
        }
    }

//    public interface Inter_RVA_frgCheckBox {
//        void update_lstCategoria(Categoria categoria, boolean isChecked);
//    }
}
