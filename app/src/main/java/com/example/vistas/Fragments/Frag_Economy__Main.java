package com.example.vistas.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DAOs.ListaDAO;
import com.example.vistas.DAOs.PresupuestoDAO;
import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Presupuesto;
import com.example.vistas.Commons.Codes;
import com.example.vistas.R;
import com.example.vistas.RV_Adapters.RVA__Economy_TwoText;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Locale;

public class Frag_Economy__Main extends Fragment {

    RecyclerView rvContainer;
    RVA__Economy_TwoText rva_list;

    Presupuesto presupuesto = null;

    ArrayList<Lista> lstListas;

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

        rvContainer = view.findViewById(R.id.rv_frgEconomy);

        lstListas = get_listas();

        setUp_Presupuesto();
        //
        setUp_Estimado();
        //
        setUp_Mes();
        //
        update_Restante();
        //
        update_Listas();

        return view;
    }

    private void setUp_Presupuesto() {
        this.presupuesto = new PresupuestoDAO(getContext()).select_where_thisMonth(getContext());
        if (presupuesto == null){
            new CommonMethods(getContext()).show_alert("Parece que no has registrado el presupuesto de este mes...");
        }
    }

    private ArrayList<Lista> get_listas() {
        ArrayList<Lista> rspt = new ListaDAO(getContext()).select_all_Comprada();

        if (rspt == null) rspt = new ArrayList<>();

        return rspt;
    }

    private void setUp_Estimado() {
        String text = "S/ " + presupuesto.getPresupuesto();
        lblEstimado.setText(text);
    }

    private void setUp_Mes() {
        String monthName = "Er.Mes";

        try {
            Month month = LocalDate.now().getMonth();

            monthName = month.getDisplayName(TextStyle.FULL, new Locale("es", "ES"));

            monthName = (monthName.charAt(0) + "").toUpperCase() + monthName.substring(1);

        } catch (Exception ex) {
            System.out.println(ex);
        }
        lblMes.setText(monthName);
    }

    private void update_Restante() {

        double listSum = 0;

        for (int i = 0; i < lstListas.size(); i++) {
            listSum += lstListas.get(i).getGasto();
        }

        double differnce = presupuesto.getPresupuesto() - listSum;

        String msg = differnce + "";

        if (differnce < 0) {
            lblRestante.setTextColor(ContextCompat.getColor(getContext(), R.color.wraning));
            msg = "- " + (differnce * -1);
        }

        lblRestante.setText(msg);
    }

    private void update_Listas() {
        rva_list = new RVA__Economy_TwoText(getContext(), lstListas);

        rvContainer.setLayoutManager(new LinearLayoutManager(getContext()));
        rvContainer.setAdapter(rva_list);
    }
}