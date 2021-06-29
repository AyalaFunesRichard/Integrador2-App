package com.example.vistas.RV_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.DTOs.Lista;
import com.example.vistas.R;

import java.util.ArrayList;
import java.util.EventListener;

public class RVA__Economy_TwoText extends RecyclerView.Adapter<RVA__Economy_TwoText.MyHolder> implements EventListener {

    Context context;

    ArrayList<Lista> lstListas = null;

    public RVA__Economy_TwoText(Context context, ArrayList<Lista> lstListas) {
        this.context = context;
        this.lstListas = lstListas;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_economy_two_text, viewGroup, false);
        return new MyHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        Lista lista = lstListas.get(i);

        myHolder.lblNombre.setText(lista.getNombre() + ":");
        myHolder.lblCosto.setText("S/ " + lista.getGasto());
    }

    @Override
    public int getItemCount() {
        return lstListas.size();
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
