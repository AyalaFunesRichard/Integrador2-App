package com.example.vistas.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.fragment.app.Fragment;

import com.example.vistas.DAOs.ListaDAO;
import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Presupuesto;
import com.example.vistas.Definitions.Code_DB;
import com.example.vistas.Definitions.Codes;
import com.example.vistas.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Frag_Economy__Main extends Fragment {

    Presupuesto presupuesto = null;

    ArrayList<Lista> listas;

    TextView lblMes, lblEstimado, lblRestante;

    public Frag_Economy__Main() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_economy__main, container, false);

        lblMes = view.findViewById(R.id.lbl_frgEconomy_Mes);
        lblEstimado = view.findViewById(R.id.lbl_frgEconomy_Monto);
        lblRestante = view.findViewById(R.id.lbl_frgEconomy_Restante);

        listas = get_listas();

        setUp_Presupuesto();
        //
        setUp_Estimado();
        //
        setUp_Mes();
        //
        update_Restante();

        return view;
    }

    private void setUp_Presupuesto() {
        // TODO capturar el presupuesto de este mes DAO
        this.presupuesto = new Presupuesto(1, "19-05-2021", 1200, Codes.PRESUPUESTO_ESTADO_ACTIVO);
    }

    private ArrayList<Lista> get_listas() {
        ArrayList<Lista> rspt = new ListaDAO(getContext()).select_all_Comprada();

        if (rspt == null) rspt = new ArrayList<>();

        return rspt;
    }

    private void setUp_Estimado() {
        lblEstimado.setText("S/ " + presupuesto.getMonto());
    }

    private void setUp_Mes() {
        String mes = "Er.Mes";

        try {
//            Date date = new SimpleDateFormat("MMM");
//            DateFormat dateFormat = new SimpleDateFormat(Code_DB.DATE_FORMART);
//            Date date = dateFormat.parse(presupuesto.getFecha());
//            lblMes.setText(mes);
//
//            DateFormat fmt = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
//            Date d = fmt.parse("June 27,  2007");

        }catch (Exception ex){
            System.out.println(ex);
        }
        lblMes.setText(mes);
    }

    private void update_Restante() {

        double listSum = 0;

        for (int i = 0; i < listas.size(); i++) {
            listSum += listas.get(i).getGasto();
        }

        double differnce = presupuesto.getMonto() - listSum;

        String msg = differnce + "";

        if (differnce < 0) {
            lblEstimado.setTextColor(Color.RED);
        }

        lblEstimado.setText(msg);
    }

    private void update_Listas() {

    }
}