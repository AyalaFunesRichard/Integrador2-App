package com.example.vistas.RV_Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.DTOs.Producto;
import com.example.vistas.Interfaces.Inter__RVA__One_Item;
import com.example.vistas.R;

import java.util.EventListener;
import java.util.List;

public class RVA__OneItem extends RecyclerView.Adapter<RVA__OneItem.MyHolder> implements EventListener {

    Context context;

    List<Producto> lstSearched = null;

    Inter__RVA__One_Item inter;

    public RVA__OneItem(Context context, List<Producto> lstSearched, Inter__RVA__One_Item inter) {
        this.context = context;
        this.lstSearched = lstSearched;
        this.inter = inter;
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_one_item, viewGroup, false);
        return new MyHolder(v, inter);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        Producto producto = lstSearched.get(i);

        myHolder.lblNombre.setText(producto.getNombre());

        myHolder.producto = producto;
    }

    @Override
    public int getItemCount() {
        return lstSearched.size();
    }

    // *
    // * MyHolder ->
    // *
    public class MyHolder extends RecyclerView.ViewHolder {

        TextView lblNombre;
        ConstraintLayout layout;

        Producto producto;
        Inter__RVA__One_Item inter;

        public MyHolder(@NonNull final View itemView, Inter__RVA__One_Item inter) {
            super(itemView);

            this.inter = inter;

            lblNombre = itemView.findViewById(R.id.lbl_rvOneItem_itemName);
            layout = itemView.findViewById(R.id.lyt_Item);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    inter.productSelected(producto);
                }
            });
        }
    }
}
