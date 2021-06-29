package com.example.vistas;

import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.vistas.Commons.Codes;
import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DTOs.Usuario;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity  {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;

    public static Usuario usuario;
    public static DataSnapshot dataSnapshot;

    DatabaseReference dbReference;

    CommonMethods cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        createLocal_dummyData();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_product, R.id.nav_list, R.id.nav_economy, R.id.nav_category, R.id.nav_user)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.main_fragment_container);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        usuario = (Usuario) getIntent().getSerializableExtra(Codes.ARG_FIREBASE_IDUSUARIO);

        cm = new CommonMethods(this);

        setUp_familyName();

        setUp_firebaseListener();
    }

    private void createLocal_dummyData() {
        //! ->
//        //! DUMMY DATA
//        new ListaDAO(this).DummyData_insert();
//        new ProductoDAO(this).DummyData_insert();
//        new CategoriaDAO(this).DummyData_insert();
//        new Rela_ProductoCategoriaDAO(this).DummyData_insert();
//        new Rela_ListaProductoDAO(this).DummyData_insert();
    }

    public void setUp_familyName() {
        View headerLayout = navigationView.getHeaderView(0);
        TextView lblHeader = headerLayout.findViewById(R.id.lbl_navHeader_nombreFamiliar);
        lblHeader.setText(usuario.getNombreFamiliar());
    }

    private void setUp_firebaseListener(){

        dbReference = FirebaseDatabase.getInstance().getReference().child("Usuario").child(usuario.getIdUsario());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                cm.show_toast("hubo cambios");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                cm.show_toast("No se puso establecer conexion con la BD");
            }
        };
        dbReference.addValueEventListener(postListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.main_fragment_container);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {

        int count = getSupportFragmentManager().getBackStackEntryCount();

        if (count == 0) {
//            super.onBackPressed();
            //additional code
//            this.finish();
//            System.exit(0);
            cm.show_toast("0");
            getSupportFragmentManager().popBackStack();
        } else {
            getSupportFragmentManager().popBackStack();
            cm.show_toast("no es cero");
        }


    }


}