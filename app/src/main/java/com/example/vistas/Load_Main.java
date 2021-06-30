package com.example.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.vistas.Commons.Code_DB;
import com.example.vistas.Commons.Codes;
import com.example.vistas.Commons.Codes_Logs;
import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DAOs.CategoriaDAO;
import com.example.vistas.DAOs.PresupuestoDAO;
import com.example.vistas.DTOs.Presupuesto;
import com.example.vistas.DataBase.DataBaseQuery;
import com.example.vistas.DAOs.ListaDAO;
import com.example.vistas.DAOs.ProductoDAO;
import com.example.vistas.DAOs.Rela_ListaProductoDAO;
import com.example.vistas.DAOs.Rela_ProductoCategoriaDAO;
import com.example.vistas.DTOs.Categoria;
import com.example.vistas.DTOs.Lista;
import com.example.vistas.DTOs.Producto;
import com.example.vistas.DTOs.Rela_ProductoCategoria;
import com.example.vistas.DTOs.Rela_ProductoLista;
import com.example.vistas.DTOs.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Load_Main extends AppCompatActivity implements Codes_Logs, Code_DB {

    private CommonMethods cm;

    private FirebaseAuth mAuth;

    public static Usuario usuario;
    public static DataSnapshot dataSnapshot;

    private DatabaseReference dbReference;

    String idUsuario, newPresupuesto; //newPresupuesto <- is only use, when there is no Presupuesto setted

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_main);

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // * User already signed in ->

            idUsuario = mAuth.getUid();

            new DataBaseQuery(getApplicationContext()).deleteAll_butNotUsuario();

            Logged__recoverAndSet_firebaseData();

        } else {
            activity_goTo_SigIn();
        }
    }

    private void Logged__recoverAndSet_firebaseData() {

        cm = new CommonMethods(getApplicationContext());

        usuario = new Usuario();
        this.usuario.setIdUsario(idUsuario);

        dbReference = FirebaseDatabase.getInstance().getReference().child("Usuario");

        dbReference.child(usuario.getIdUsario()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> taskRspt) {
                if (taskRspt.isSuccessful()) {

                    Log.d(LOG_FIREBASE, String.valueOf(taskRspt.getResult().getValue()));

                    dataSnapshot = taskRspt.getResult();

                    usuario = dataSnapshot.getValue(Usuario.class);

                    setUp_Data__Productos();

                    setUp_Data__Listas();

                    setUp_Data__Categorias();

                    setUp_Data__RelaProductoCategorias();

                    setUp_Data__RelaProductoLista();

                    setUp_Data__Presupuesto();

//                    activity_goTo_Main();
                } else {
                    Log.e(LOG_FIREBASE, "Error getting data", taskRspt.getException());

                    cm.show_alert("Error recuperando tus datos. Vuelva a inciar sesión, por favor.");
                    activity_goTo_SigIn();
                }
            }
        });
    }

    private void setUp_Data__Presupuesto() {

        String localTime = cm.getMonthAndYear();

        boolean thisMonthIsSet = false;
        if (dataSnapshot.hasChild(TABLE_PRESUPUESTO)) {
            // * Presupuesto is already registered

            PresupuestoDAO dao = new PresupuestoDAO(getApplicationContext());
            Presupuesto aux = new Presupuesto();

            String timeSaved;
            for (DataSnapshot ds : dataSnapshot.child(TABLE_PRESUPUESTO).getChildren()) {

                aux = (Presupuesto) ds.getValue(Presupuesto.class);

                timeSaved = cm.getMonthAndYear(aux.getLog_fecha_creado());

                if (localTime.equals(timeSaved)) {
                    thisMonthIsSet = true;
                    dao.insert(aux, true);

                    activity_goTo_Main();
                    break;
                }
            }
        }

        // * Presupuesto must be registered ->
        if (!thisMonthIsSet) {
            LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
            View view = inflater.inflate(R.layout.alert_dialog__must_register_presupuesto, null);

            Button btnConfirm = view.findViewById(R.id.btnRegister);
            Button btnExit = view.findViewById(R.id.btnExit);
            TextView txtPresupuesto = view.findViewById(R.id.txPresupusto);

            AlertDialog alertDialog = new AlertDialog.Builder(Load_Main.this)
                    .setView(view)
                    .create();

            boolean ok = false;
            btnConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    newPresupuesto = txtPresupuesto.getText().toString();
                    double presu = cm.validate_Presupuesto(newPresupuesto);

                    if (presu == -1) {
                        return;
                    }

                    Presupuesto newPresupuesto = new Presupuesto();
                    newPresupuesto.setPresupuesto(presu);
                    newPresupuesto.setEstado(Codes.PRESUPUESTO_ESTADO_ACTIVO);
                    newPresupuesto.setLog_fecha_creado(cm.getTime_ForDataBase());

                    int rspt = new PresupuestoDAO(getApplicationContext()).insert(newPresupuesto, false);

                    if (rspt != Code_DB.SQLITE_ERROR) {
                        alertDialog.dismiss();
                        activity_goTo_Main();
                    }
                }
            });
            btnExit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    alertDialog.dismiss();
                    finish();
                    System.exit(0);
                }
            });

            alertDialog.show();
        }
    }

    private void setUp_Data__Productos() {

        if (dataSnapshot.hasChild(TABLE_PRODUCTO)) {

            ProductoDAO productoDAO = new ProductoDAO(getApplicationContext());
            Producto auxProducto = new Producto();

            for (DataSnapshot ds : dataSnapshot.child(TABLE_PRODUCTO).getChildren()) {

                auxProducto = (Producto) ds.getValue(Producto.class);

                productoDAO.insert(auxProducto, true);
            }
        }
    }

    private void setUp_Data__Listas() {

        if (dataSnapshot.hasChild(TABLE_LISTA)) {

            ListaDAO dao = new ListaDAO(getApplicationContext());
            Lista aux = new Lista();

            for (DataSnapshot ds : dataSnapshot.child(TABLE_LISTA).getChildren()) {

                aux = (Lista) ds.getValue(Lista.class);

                dao.insert(aux, true);
            }
        }
    }

    private void setUp_Data__Categorias() {

        if (dataSnapshot.hasChild(TABLE_CATEGORIA)) {

            CategoriaDAO dao = new CategoriaDAO(getApplicationContext());
            Categoria aux = new Categoria();

            for (DataSnapshot ds : dataSnapshot.child(TABLE_CATEGORIA).getChildren()) {

                aux = (Categoria) ds.getValue(Categoria.class);

                dao.insert(aux, true);
            }
        }
    }

    private void setUp_Data__RelaProductoCategorias() {

        if (dataSnapshot.hasChild(TABLE_PRODUCTO_CATEGORIA)) {

            Rela_ProductoCategoriaDAO dao = new Rela_ProductoCategoriaDAO(getApplicationContext());

            Rela_ProductoCategoria aux = new Rela_ProductoCategoria();
            for (DataSnapshot ds : dataSnapshot.child(TABLE_PRODUCTO_CATEGORIA).getChildren()) {

                aux = (Rela_ProductoCategoria) ds.getValue(Rela_ProductoCategoria.class);

                dao.insert(aux, true);
            }
        }
    }

    private void setUp_Data__RelaProductoLista() {

        if (dataSnapshot.hasChild(TABLE_PRODUCTO_LISTA)) {

            Rela_ListaProductoDAO dao = new Rela_ListaProductoDAO(getApplicationContext());

            Rela_ProductoLista aux = new Rela_ProductoLista();
            for (DataSnapshot ds : dataSnapshot.child(TABLE_PRODUCTO_LISTA).getChildren()) {

                aux = (Rela_ProductoLista) ds.getValue(Rela_ProductoLista.class);

                dao.insert(aux, true);
            }
        }
    }

    // * Move to other activity ->
    private void activity_goTo_Main() {
        Intent intent = new Intent(this, MainActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        intent.putExtra(Codes.ARG_FIREBASE_IDUSUARIO, usuario);
//
//        Intent i = new Intent(OldActivity.this, NewActivity.class);
//// set the new task and clear flags
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK)
//        startActivity(i);


        startActivity(intent);
    }

    private void activity_goTo_SigIn() {
        Intent intent = new Intent(this, LoginActivity.class);

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

}