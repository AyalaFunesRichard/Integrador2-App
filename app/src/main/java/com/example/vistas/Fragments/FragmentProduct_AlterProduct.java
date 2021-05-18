package com.example.vistas.Fragments;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.vistas.Definitions.Codes;
import com.example.vistas.R;

public class FragmentProduct_AlterProduct extends Fragment {

    private Button btnCancel, btnConfirm;
    private ImageButton iBtnCancelEdit, iBtnConfirmEdit, iBtnDelete;

    private String fragmentFor = null; // recognize this fragment


    public FragmentProduct_AlterProduct() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_product__alter_product, container, false);

        detectThisFragment();

        setActionBarTitle();

        setButtons(v);

//        v.findViewById(R.id.btn)

        return v;
    }

    /*
     * DETECTAR TIPO DE FRAGMENT
     *   SETTEAR EL TITULO-------
     *   SETTEAR TEXT_EDIT
     *   SETTER LOS BOTONES
     *   SETTEAR EIQUETAS
     *       SI editar
     *            VERIFICAR SI EL PRODUCTO CAMBIO
     *            RECIBIR PRODUCTO
     *            RECIBIR ETIQUETAS SELECCIONADAS
     * */


    private void detectThisFragment() {
        fragmentFor = getArguments().getString("fragmentFor");

        if (fragmentFor.isEmpty() || fragmentFor == null) {
            fragmentFor = Codes.FRAGMENT_FOR_CREATE; // <- Default, in case of error when detecting
        }
    }

    private void setActionBarTitle() {

        String actionBarTitle = "";

        if (fragmentFor.equals(Codes.FRAGMENT_FOR_CREATE)) {
            actionBarTitle = getResources().getString(R.string.actionBarTitle_AlterProduct_create);
        } else {
            if (fragmentFor.equals(Codes.FRAGMENT_FOR_EDIT)) {
                actionBarTitle = getResources().getString(R.string.actionBarTitle_AlterProduct_edit);
            } else {
                actionBarTitle = "Er. detecting title";
            }
        }

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(actionBarTitle);
    }

    private void setButtons(View v) {
        if (fragmentFor.equals(Codes.FRAGMENT_FOR_CREATE)) {

            btnCancel = v.findViewById(R.id.btn_FrgAltrProd_cancelar);
            btnCancel.setVisibility(View.VISIBLE);

            btnConfirm = v.findViewById(R.id.btn_FrgAltrProd_registrar);
            btnConfirm.setVisibility(View.VISIBLE);

            // Gone the others
            v.findViewById(R.id.iBtn_FrgAltrProd_cancel).setVisibility(View.GONE);
            v.findViewById(R.id.iBtn_FrgAltrProd_register).setVisibility(View.GONE);
            v.findViewById(R.id.iBtn_FrgAltrProd_delete).setVisibility(View.GONE);
        }else{
            iBtnCancelEdit =  v.findViewById(R.id.iBtn_FrgAltrProd_cancel);
            iBtnCancelEdit.setVisibility(View.VISIBLE);

            iBtnConfirmEdit =  v.findViewById(R.id.iBtn_FrgAltrProd_register);
            iBtnConfirmEdit.setVisibility(View.VISIBLE);

            iBtnDelete =  v.findViewById(R.id.iBtn_FrgAltrProd_delete);
            iBtnDelete.setVisibility(View.VISIBLE);

            // Gone the others
            v.findViewById(R.id.btn_FrgAltrProd_cancelar).setVisibility(View.GONE);
            v.findViewById(R.id.btn_FrgAltrProd_registrar).setVisibility(View.GONE);
        }
    }

//    private final View.OnClickListener mListener = new View.OnClickListener() {
//        public void onClick(View view) {
//            switch (view.getId()) {
//                case R.id.button1:
//                    // do stuff
//                    break;
//                case R.id.button2:
//                    // do stuff
//                    break;
//                case R.id.button3:
//                    // do stuff
//                    break;
//            }
//        }
//    }
}