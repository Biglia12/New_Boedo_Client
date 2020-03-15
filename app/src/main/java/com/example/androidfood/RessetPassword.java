package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Bundle;

public class RessetPassword extends AppCompatActivity {

    private EditText mEditTextEmail;
    private Button mbtnResetPassword;

    private String email = "";

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resset_password);



            mAuth = FirebaseAuth.getInstance();
            mDialog = new ProgressDialog(this);

            mEditTextEmail = (EditText) findViewById(R.id.editTextEmail);
            mbtnResetPassword = (Button) findViewById(R.id.btnResetPassword);

            mbtnResetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    email = mEditTextEmail.getText().toString();

                    if (!email.isEmpty()) {
                        mDialog.setMessage("Espere un momento...");
                        mDialog.setCanceledOnTouchOutside(false);
                        mDialog.show();
                        resetPassword();
                    } else {
                        Toast.makeText(RessetPassword.this, "Debe ingresar email", Toast.LENGTH_SHORT).show();
                    }

                    resetPassword();
                }
            });
        }

        private void resetPassword () {

            mAuth.setLanguageCode("es");
            mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {


                    if (task.isSuccessful()) {

                        Toast.makeText(RessetPassword.this, "Se ha enviado un correo para reestablecer su contraseña", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RessetPassword.this, "No se pudo enviar el correo para establecer su contraseña", Toast.LENGTH_SHORT).show();
                    }
                    mDialog.dismiss();
                }

            });

        }

    }



