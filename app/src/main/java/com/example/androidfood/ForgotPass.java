package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import info.hoang8f.widget.FButton;

public class ForgotPass extends AppCompatActivity {

    EditText email;
    Button btnGetPass;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);


        //set color status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        //anh xa
        email =  findViewById(R.id.EmailForgotPass);
        btnGetPass =  findViewById(R.id.btnGetPass);
        firebaseAuth = FirebaseAuth.getInstance();
        //xu li
        btnGetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Email = email.getText().toString().trim();
                if(Email.isEmpty()) {
                    Toast.makeText(ForgotPass.this, "Vui lòng điền Email", Toast.LENGTH_SHORT).show();
                }
                else {

                    firebaseAuth.sendPasswordResetEmail(Email)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(ForgotPass.this, "Abra el correo electrónico para cambiar su contraseña", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ForgotPass.this, SignIn.class));
                                    } else {
                                        Toast.makeText(ForgotPass.this, "El correo no existe", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }


}
