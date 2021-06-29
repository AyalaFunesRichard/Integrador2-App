package com.example.vistas.RV_Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.DAOs.CategoriaDAO;
import com.example.vistas.DTOs.Categoria;
import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Commons.Code_Error;
import com.example.vistas.Commons.Codes;
import com.example.vistas.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EventListener;
import java.util.List;

public class RVA__frgCategoria extends RecyclerView.Adapter<RVA__frgCategoria.MyHolder> implements EventListener {

    Context context;

    private final int BUTTONS_NORMAL = 0; // when buttons are:   delete & edit
    private final int BUTTONS_EDITING = 1; // when buttons are:   cancel & cofirm

    int buttonStatus = BUTTONS_NORMAL;

    List<Categoria> lstCategoria;
    Inter_RVA_frgCategoria inter_fragCategoria = null;

    public RVA__frgCategoria(Context context, ArrayList<Categoria> lstCategoria, Inter_RVA_frgCategoria inter_fragCategoria) {
        this.context = context;
        this.lstCategoria = lstCategoria;
        this.inter_fragCategoria = inter_fragCategoria;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rv_item_two_options, viewGroup, false);
        return new RVA__frgCategoria.MyHolder(view, inter_fragCategoria);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder myHolder, int i) {

        Categoria categoriaAux = this.lstCategoria.get(i);

        myHolder.categoria = categoriaAux;

        myHolder.txtNombre.setText(categoriaAux.getNombre());
        myHolder.txtNombre.setFocusable(false);
        myHolder.txtNombre.setEnabled(false);
        myHolder.txtNombre.setClickable(false);
        myHolder.txtNombre.setFocusableInTouchMode(false);
    }

    @Override
    public int getItemCount() {
        return lstCategoria.size();
    }

    //
    // * MyHolder ->
    //
    public class MyHolder extends RecyclerView.ViewHolder implements Code_Error {
        EditText txtNombre;

        // left-button = delete/cancel
        // right-button = edit/confirm
        ImageButton iBtn_Left, iBtn_Right;

        Categoria categoria;
        Inter_RVA_frgCategoria inter_fragCategoria;

        public MyHolder(@NonNull final View itemView, final Inter_RVA_frgCategoria inter_fragCategoria) {
            super(itemView);

            this.inter_fragCategoria = inter_fragCategoria;

            txtNombre = itemView.findViewById(R.id.etRV_itemName);

            iBtn_Left = itemView.findViewById(R.id.iBtn_rvItemTwoOptio_left);
            iBtn_Left.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (buttonStatus == BUTTONS_NORMAL) {
                        confirmDelete();
                    } else {
                        alter_ButtonStatus();

                        alter_EditText();

                        txtNombre.setText(categoria.getNombre());
                    }
                }
            });

            iBtn_Right = itemView.findViewById(R.id.iBtn_rvItemTwoOptio_right);
            iBtn_Right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (buttonStatus == BUTTONS_NORMAL) {
                        alter_ButtonStatus();

                        alter_EditText();
                    } else {

                        alter_ButtonStatus();

                        alter_EditText();

                        updateCategoria(itemView);
                    }
                }
            });
        }

        private void confirmDelete() {
            String QUESTION = itemView.getResources().getString(R.string.dialog_ques_deleting) + "  " + categoria.getNombre() + "\n¿Continuar?";
            new AlertDialog.Builder(itemView.getContext())
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(itemView.getResources().getString(R.string.dialog_title_delete))
                    .setMessage(QUESTION)
                    .setPositiveButton(itemView.getResources().getString(R.string.dialog_ans_confirm), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            deleteItem();

                            inter_fragCategoria.updateRecyclerView();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        private void alter_ButtonStatus() {
            if (buttonStatus == BUTTONS_NORMAL) {
                iBtn_Left.setImageResource(R.drawable.ic_button_close);
                iBtn_Right.setImageResource(R.drawable.ic_button_check);

                buttonStatus = BUTTONS_EDITING;
            }else{
                iBtn_Left.setImageResource(R.drawable.ic_button_delete);
                iBtn_Right.setImageResource(R.drawable.ic_button_edit);

                buttonStatus = BUTTONS_NORMAL;
            }
        }

        private void alter_EditText(){
            if(buttonStatus == BUTTONS_NORMAL){
                txtNombre.setFocusable(false);
                txtNombre.setEnabled(false);
                txtNombre.setClickable(false);
                txtNombre.setFocusableInTouchMode(false);
            }else{
                txtNombre.setFocusable(true);
                txtNombre.setEnabled(true);
                txtNombre.setClickable(true);
                txtNombre.setFocusableInTouchMode(true);
            }
        }

        private void updateCategoria(View itemView){

            String nombre = getNombreProducto(itemView);
            if (nombre.equals(ERROR_EMPTY) || nombre.equals(ERROR_LONG) || nombre.equals(ERROR_REPEATED)) {
                return;
            }

            String fechaModificado = new SimpleDateFormat(Code_DB.DATE_FORMART).format(new Date());

            categoria.setNombre(nombre);
            categoria.setLog_fecha_modificado(fechaModificado);

            int rspt = new CategoriaDAO(itemView.getContext()).update_where_idCategoria(categoria);

            String message = "null";
            if (rspt == Code_DB.SQLITE_ERROR) {
                message = itemView.getContext().getString(R.string.categoria_updated_fail);
            } else {
                message = itemView.getContext().getString(R.string.categoria_updated);
            }

            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();

            inter_fragCategoria.updateRecyclerView();

        }

        private void deleteItem() {
            int rspt  = new CategoriaDAO(itemView.getContext()).delete_where_idCategoria(this.categoria.getIdCategoria());

            String message = "null";
            if (rspt == Code_DB.SQLITE_ERROR) {
                message = itemView.getContext().getString(R.string.categoria_deteled_fail);
            } else {
                message = itemView.getContext().getString(R.string.categoria_deleted);
            }

            Toast.makeText(itemView.getContext(), message, Toast.LENGTH_SHORT).show();
        }

        // Validation
        private String getNombreProducto(View itemView) {
            String rspt = txtNombre.getText().toString().trim();

            if (rspt.isEmpty() || rspt == null) {
                Toast.makeText(itemView.getContext(), itemView.getContext().getText(R.string.error_txtNombre_empty), Toast.LENGTH_SHORT).show();
                return ERROR_EMPTY;
            }

            if (rspt.length() > Codes.MAX_PRODU_NOMBRE) {
                Toast.makeText(itemView.getContext(), itemView.getContext().getText(R.string.error_txtNombre_long), Toast.LENGTH_SHORT).show();
                return ERROR_LONG;
            }

            Categoria categoria = new CategoriaDAO(itemView.getContext()).select_where_nombre(rspt);
            if (categoria != null) {
                Toast.makeText(itemView.getContext(), itemView.getContext().getText(R.string.error_txtNombre_categoria_repeated), Toast.LENGTH_SHORT).show();
                return ERROR_REPEATED;
            }

            return rspt;
        }
    }

    public interface Inter_RVA_frgCategoria {

        void updateRecyclerView();
    }
}
