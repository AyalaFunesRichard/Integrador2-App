package com.example.vistas;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.example.vistas.Commons.Codes;
import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DAOs.CategoriaDAO;
import com.example.vistas.DAOs.ListaDAO;
import com.example.vistas.DAOs.ProductoDAO;
import com.example.vistas.DAOs.Rela_ListaProductoDAO;
import com.example.vistas.DAOs.Rela_ProductoCategoriaDAO;
import com.example.vistas.DTOs.Usuario;
import com.example.vistas.Interfaces.Inter_FragmentManager;
import com.example.vistas.Interfaces.Inter_OnBackPressed;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private NavigationView navigationView;

    public static Usuario usuario;
    public static DataSnapshot dataSnapshot;

    //    android.app.FragmentTransaction transaction;
    FragmentManager fragmentManager;

    DatabaseReference dbReference;

    CommonMethods cm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        create_dummyData();

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


        fragmentManager = getSupportFragmentManager();

        setUp_familyName();

        setUp_firebaseListener();
    }

    private void create_dummyData() {
        new ListaDAO(this).DummyData_insert();
        new ProductoDAO(this).DummyData_insert();
        new CategoriaDAO(this).DummyData_insert();
        new Rela_ProductoCategoriaDAO(this).DummyData_insert();
        new Rela_ListaProductoDAO(this).DummyData_insert();
    }

    public void setUp_familyName() {
        View headerLayout = navigationView.getHeaderView(0);
        TextView lblHeader = headerLayout.findViewById(R.id.lbl_navHeader_nombreFamiliar);
        lblHeader.setText(usuario.getNombreFamiliar());
    }

    private void setUp_firebaseListener() {

        dbReference = FirebaseDatabase.getInstance().getReference().child("Usuario").child(usuario.getIdUsario());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                // TODO aplicar los cambios a las pantallas desde aqui
//                cm.show_toast("hubo cambios");
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

//    @Override
//    public void onBackPressed() {
//        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.main_fragment_container);
//        if (!(fragment instanceof Inter_OnBackPressed) || !((Inter_OnBackPressed) fragment).onBackPressed()) {
//            cm.show_toast("salir");
//            super.onBackPressed();
//        } else {
//            cm.show_toast("quedarse...");
//        }
//    }

    //    @Override
    public void changefragment(Fragment fragment, boolean clearStack) {
//        transaction = fragmentManager
//                .beginTransaction()
//                .replace(R.id.main_fragment_container, fragment)
//                .addToBackStack("main ");
//
//        for (int entry = 0; entry < getSupportFragmentManager().getBackStackEntryCount(); entry++) {
//            Log.i("FRAGMENT MANAGER", "Found fragment: " + getSupportFragmentManager().getBackStackEntryAt(entry).getName());
//        }
//        transaction.commit();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_fragment_container, fragment).addToBackStack("main").commit();
    }
//
//    @Override
//    public void onBackPressed() {
//
//
//        cm.show_log("FRAGMENT", "getFragmentManager:  " + getFragmentManager().getBackStackEntryCount());
//        cm.show_log("FRAGMENT", "getSupportFragmentManager:  " + getSupportFragmentManager().getBackStackEntryCount());
//
//
//        if(getFragmentManager().getBackStackEntryCount() == 0){
//            cm.show_toast("Salir");
//        }else{
//            getFragmentManager().popBackStack();
//        }
//
//    }


//    @Override
//    public void onBackPressed() {
////
////        if (getFragmentManager().getBackStackEntryCount() > 0) {
////            getFragmentManager().popBackStack();
////        } else {
////            if (getFragmentManager().getBackStackEntryCount() == 0) {
//////                new AlertDialog.Builder(this)
//////                        .setMessage("Are you sure you want to exit?")
//////                        .setCancelable(false)
//////                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//////                            public void onClick(DialogInterface dialog, int id) {
//////                                MainActivity.this.finish();
//////                            }
//////                        })
//////                        .setNegativeButton("No", null)
//////                        .show();
////
////            } else {
////                super.onBackPressed();
////            }
////        }
//
//        int count = getFragmentManager().getBackStackEntryCount();
////        int count = getSupportFragmentManager().getBackStackEntryCount();
//
//        if (count == 0) {
//            cm.show_toast("salir");
//            Log.i("FRAGMENT MANAGER", "Intenta salir");
//            //            this.finish();
////            System.exit(0);
//
//        } else {
//            Log.i("FRAGMENT MANAGER", "Retroceder frament");
//            getFragmentManager().popBackStack();
//        }
//    }


}