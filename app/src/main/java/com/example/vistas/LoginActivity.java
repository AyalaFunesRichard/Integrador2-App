package com.example.vistas;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.vistas.Commons.Code_Error;
import com.example.vistas.Commons.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, View.OnKeyListener {


    EditText txtMail, txtPass;
    Button btnLogIn, btnCreate;

    CommonMethods cm;

    TextWatcher watcher_email, watcher_pass;

    private FirebaseAuth mAuth;

    private String idUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        // Set up View's Items
        txtMail = findViewById(R.id.etLogin_username);
        txtPass = findViewById(R.id.etLogin_password);

        btnCreate = findViewById(R.id.btnLogin_signUp);
        btnCreate.setOnClickListener(this);
        btnLogIn = findViewById(R.id.btnLogin_signIn);
        btnLogIn.setOnClickListener(this);

        cm = new CommonMethods(this);
    }

    private void activityDone() {

        Intent intent = new Intent(this, Load_Main.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin_signIn: {

                String email = cm.validate_EmailUsuario(txtMail.getText().toString(), false, (TextInputLayout) findViewById(R.id.txtLytUsername));
                if (email == null) return;

                String pass = cm.validate_ContraseniaUsuario(txtPass.getText().toString(), false, findViewById(R.id.txtLytPasswrod));
                if (pass == null) return;

                mAuth.signInWithEmailAndPassword(email, pass)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {

                                    idUsuario = mAuth.getUid();

                                    activityDone();
                                } else {
                                    cm.show_toast("Usuario invalido. Verificar credenciales");
                                }
                            }
                        });
                break;
            }
            case R.id.btnLogin_signUp: {

                Intent intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
            }
            default: {
                cm.show_toast(Code_Error.ERROR_IDENTIFING_ACTION_IN_BUTTON);
                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        this.finish();
        System.exit(0);
    }

    @Override
    public boolean onKey(View view, int i, KeyEvent keyEvent) {
        return false;
    }
}