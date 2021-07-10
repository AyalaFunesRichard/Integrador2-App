package com.example.vistas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vistas.Commons.Codes;
import com.example.vistas.Commons.CommonMethods;
import com.example.vistas.DTOs.Usuario;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText txtNombre, txtCorreo, txtContrasenia1, txtContrasenia2;
    Button btnRegister, btnCancel;
    TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPass1, inputLayoutPass2;

    FirebaseAuth mAuth;

    DatabaseReference dbReference;

    CommonMethods cm;

    Context context;

    String correo, password1, nombre;
    Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        setUp_variables();
    }

    private void setUp_variables() {
        txtNombre = findViewById(R.id.etSignUp_userName);
        txtCorreo = findViewById(R.id.etSignUp_email);
        txtContrasenia1 = findViewById(R.id.etSignUp_password1);
        txtContrasenia2 = findViewById(R.id.etSignUp_password2);

        inputLayoutName = findViewById(R.id.txtLytUsername);
        inputLayoutEmail= findViewById(R.id.txtLytEmail);
        inputLayoutPass1 = findViewById(R.id.txtLytPassword1);
        inputLayoutPass2 = findViewById(R.id.txtLytPassword2);


        btnRegister = findViewById(R.id.btnSignUp_signUp);
        btnRegister.setOnClickListener(this);

        btnCancel = findViewById(R.id.btnSignUp_cancel);
        btnCancel.setOnClickListener(this);

        //
        mAuth = FirebaseAuth.getInstance();
        //
        cm = new CommonMethods(this);
        //
        context = this.getApplicationContext();
    }

    //
    private void manage_SignUp() {

        correo = cm.validate_EmailUsuario(txtCorreo.getText().toString(), false, inputLayoutEmail);
        if (correo == null) return;

        password1 = cm.validate_ContraseniaUsuario(txtContrasenia1.getText().toString(), false, inputLayoutPass1);
        String password2 = cm.validate_ContraseniaUsuario(txtContrasenia1.getText().toString(), false, inputLayoutPass2);
        if (password1 == null || password2 == null) return;
        if (!password1.equals(password2)) {
            cm.show_toast("Contraseñas deben ser iguales.");
            return;
        }
        nombre = cm.validate_NombreUsuario(txtNombre.getText().toString(), false, inputLayoutName);
        if (nombre == null) return;

        // create in Authentication server
        mAuth.createUserWithEmailAndPassword(correo, password1)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            String idUsuario = user.getUid();

                            createIn_dataBase(idUsuario);
                        } else {
                            Log.w(null, "createUserWithEmail:failure", task.getException());
                            cm.show_toast("Error registrando usuario");
                        }
                    }
                });
    }

    private void createIn_dataBase(String idUsuario) {
        usuario = new Usuario();
        usuario.setNombreFamiliar(nombre);
        usuario.setIdUsario(idUsuario);
        usuario.setCorreo(correo);
        usuario.setEstado(Codes.USUARIO_ESTADO_ACTIVO);
        usuario.setLog_fecha_creado(cm.getTime_ForDataBase());
        usuario.setLog_fecha_ultimaSesion(cm.getTime_ForDataBase());

        Map<String, Usuario> usuarioMap = new HashMap<>();
        usuarioMap.put(idUsuario, usuario);

        FirebaseDatabase rootNode;
        rootNode = FirebaseDatabase.getInstance();
        dbReference = rootNode.getReference("Usuario");
        dbReference.child(idUsuario).setValue(usuario, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                cm.show_toast("Data saved successfully.");
                doneActivity();
            }
        });
    }

    private void deleteUserIn_authenticationSErver() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.delete()
                    .addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()) {
                                cm.show_log__fireBase("Usuario eliminado de UserAuthentication server");
                                mAuth.signOut();
                            } else {
                                cm.show_log__fireBase("Error eliminado usuario de UserAuthentication server");
                            }
                        }
                    });
        }
    }

    private void doneActivity() {
        Intent intent = new Intent(this, Load_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(Codes.ARG_FIREBASE_IDUSUARIO, usuario);
        startActivity(intent);
    }

    //
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnSignUp_signUp: {
                manage_SignUp();
                break;
            }
            case R.id.btnSignUp_cancel: {
                finish();
                break;
            }
            default: {
                break;
            }
        }
    }

}