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

import com.example.vistas.DTOs.Producto;
import com.example.vistas.Interfaces.Inter__RVA__Item_CheckBox;
import com.example.vistas.R;

import java.util.ArrayList;
import java.util.EventListener;

public class RVA__CheckBox_Producto extends RecyclerView.Adapter<RVA__CheckBox_Producto.MyHolder> implements EventListener {

    Context context;

    ArrayList<Producto> lstProducto = null;

    private Inter__RVA__Item_CheckBox rvFrgCheckBox;

    public RVA__CheckBox_Producto(Context context, ArrayList<Producto> lstProducto, Inter__RVA__Item_CheckBox rvFrgCheckBox) {
        this.context = context;
        this.lstProducto = lstProducto;
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

        final Producto producto = lstProducto.get(i);

        myHolder.lblItem.setText(producto.getNombre());

        myHolder.checkBox.setOnCheckedChangeListener(null);
        myHolder.checkBox.setChecked(true);
        myHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                rvFrgCheckBox.set_UpdateList(producto, isChecked);
            }
        });

    }

    @Override
    public int getItemCount() {
        return lstProducto.size();
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
}
