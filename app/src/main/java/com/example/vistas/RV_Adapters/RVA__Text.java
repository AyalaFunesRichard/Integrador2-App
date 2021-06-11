package com.example.vistas.RV_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.Interfaces.Inter__RVA__Item_CheckBox;
import com.example.vistas.R;

import java.util.ArrayList;
import java.util.EventListener;

public class RVA__Text extends RecyclerView.Adapter<RVA__Text.MyHolder> implements EventListener {

    Context context;

    ArrayList<Lista> listas = null;

    private Inter__RVA__Item_CheckBox rvFrgCheckBox;

    public RVA__Text(Context context, ArrayList<Lista> lista) {
        this.context = context;
        this.listas = lista;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_checkbox, viewGroup, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        Lista lista = listas.get(i);

        myHolder.lblNombre.setText(lista.getNombre() + ":");
        myHolder.lblCosto.setText("S/ " + lista.getGasto());

    }

    @Override
    public int getItemCount() {
        return listas.size();
    }

    //
    // MyHolder ->
    //
    public class MyHolder extends RecyclerView.ViewHolder {
        TextView lblNombre, lblCosto;

        public MyHolder(@NonNull final View itemView) {
            super(itemView);

            lblNombre = itemView.findViewById(R.id.lbl_rvText_nombre);

            lblCosto = itemView.findViewById(R.id.lbl_rvText_costo);
        }
    }
}
