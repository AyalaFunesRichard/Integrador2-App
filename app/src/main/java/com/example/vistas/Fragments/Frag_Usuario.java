package com.example.vistas.Fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DAOs.UsuarioDAO;
import com.example.vistas.DataBase.DataBaseQuery;
import com.example.vistas.DTOs.Usuario;
import com.example.vistas.LoginActivity;
import com.example.vistas.MainActivity;
import com.example.vistas.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Frag_Usuario extends Fragment implements View.OnClickListener {

    Button btnEditName, btnEditPass, btnLogOut;
    TextView lblName, lblEmail;

    private String dialog_name = "";
    private String dialog_pass1 = "";
    private String dialog_pass2 = "";

    private CommonMethods cm;

    private Usuario usuario;

    View view;

    public Frag_Usuario() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_usuario, container, false);

        setUp_Variables();

        setUp_Labels();

        return view;
    }

    private void setUp_Labels() {
        lblName.setText(MainActivity.usuario.getNombreFamiliar());
        lblEmail.setText(MainActivity.usuario.getCorreo());
    }

    private void setUp_Variables() {
        btnEditName = view.findViewById(R.id.btnEditName);
        btnEditName.setOnClickListener(this);

        btnEditPass = view.findViewById(R.id.btnEditPass);
        btnEditPass.setOnClickListener(this);

        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnLogOut.setOnClickListener(this);

        lblName = view.findViewById(R.id.lblNombreFamilia);
        lblEmail = view.findViewById(R.id.lblEmail);

        cm = new CommonMethods(getContext());
    }

    public void alertDialog_changePassword() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog__change_password, null);

        ImageButton ibtnConfirm = view.findViewById(R.id.iBtnCheck);
        ImageButton ibtnReturn = view.findViewById(R.id.iBtnReturn);
        TextView txtPass1 = view.findViewById(R.id.etPassword1);
        TextView txtPass2 = view.findViewById(R.id.etPassword2);
        TextView txtOldPass = view.findViewById(R.id.etOldPassword);
        TextView txtEmail = view.findViewById(R.id.etEmail);

        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setView(view)
                .create();

        ibtnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String email = txtEmail.getText().toString();
                email = cm.validate_EmailUsuario(email, true);
                if (email == null) return;

                String oldPass = txtOldPass.getText().toString();
                oldPass = cm.validate_ContraseniaUsuario(oldPass, true);
                if (oldPass == null) return;

                String pass1 = txtPass1.getText().toString();
                pass1 = cm.validate_ContraseniaUsuario(pass1, true);
                if (pass1 == null) return;

                String pass2 = txtPass2.getText().toString();
                pass2 = cm.validate_ContraseniaUsuario(pass2, true);
                if (pass2 == null) return;

                if (!pass1.equals(pass2)) {
                    cm.show_toast("Las contraseñas deben de ser iguales");
                    return;
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                AuthCredential credential = EmailAuthProvider
                        .getCredential(email, oldPass);

                String finalPass = pass1;
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    user.updatePassword(finalPass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                cm.show_toast("Contraseña actualizada exitosamente");
                                                alertDialog.dismiss();
                                            } else {
                                                cm.show_toast("Error actualizando su contraseña");
                                                alertDialog.dismiss();
                                            }
                                        }
                                    });
                                } else {
                                    cm.show_toast("Correo o antigua-contraseña invalidos.");
                                }
                            }
                        });

            }
        });
        ibtnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    public void alertDialog_EditName() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_dialog__update_name, null);

        TextView lblQuestion = view.findViewById(R.id.lblQuestion);
        lblQuestion.setText("Ingrese el nuevo nombre");

        Button btnConfirm = view.findViewById(R.id.btnRegister);
        Button btnExit = view.findViewById(R.id.btnExit);
        TextView txtName = view.findViewById(R.id.txtNombre);


        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .create();

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newNombre = cm.validate_NombreUsuario(txtName.getText().toString(), true);
                if(newNombre == null) return;

                Usuario newUsuario = MainActivity.usuario;
                newUsuario.setNombreFamiliar(newNombre);
                newUsuario.setLog_fecha_modificado(cm.getTime_ForDataBase());

                new UsuarioDAO(getContext()).update_NombreFamiliar_where_IdUsuario(newUsuario);

                setUp_Labels();

                ((MainActivity) getActivity()).setUp_familyName();

                alertDialog.dismiss();
            }
        });
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    // * View.OnClickListener ->
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnEditName: {
                alertDialog_EditName();
                break;
            }
            case R.id.btnEditPass: {
                alertDialog_changePassword();
                break;
            }
            case R.id.btnLogOut: {
                FirebaseAuth.getInstance().signOut();

                new DataBaseQuery(getContext()).deleteAll_tablesData();

                Intent intent = new Intent(getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

                break;
            }
            default: {

                break;
            }
        }
    }


//    @Override
//    public void onBackPressed() {
//        this.finish();
//        System.exit(0);
//    }

}