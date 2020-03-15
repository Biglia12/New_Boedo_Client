package com.example.androidfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfood.Common.Common;
import com.example.androidfood.Model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.HashMap;
import java.util.Map;

public class SignUp extends AppCompatActivity {

    private MaterialEditText edtName;
    private MaterialEditText edtApellido;//
    private MaterialEditText edtMail;//
    private MaterialEditText edtPassword;
    private MaterialEditText edtPhone;
    private Button btnSignUp2;

    private String name = "";//
    private String apellido = "";//
    private String email = "";//
    private String telefono = "";//
    private String password = "";//

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        edtName = findViewById(R.id.edtName);
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignUp2 = findViewById(R.id.btnSignUp2);

        btnSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Common.isConnectedToInternet(getBaseContext())) {
                    final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                    mDialog.setMessage("Espera Por Favor...");
                    mDialog.show();

                    table_user.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //check si el usuario fue usado
                            if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {
                                mDialog.dismiss();
                                Toast.makeText(SignUp.this, "Numero de telefono ya esta registrado", Toast.LENGTH_SHORT).show();
                            } else {
                                mDialog.dismiss();
                                User user = new User(edtName.getText().toString(),
                                        edtPassword.getText().toString());
                                //edtSecureCode.getText().toString();
                                table_user.child(edtPhone.getText().toString()).setValue(user);
                                Toast.makeText(SignUp.this, "Registro Exitoso", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                } else {
                    Toast.makeText(SignUp.this, "Por favor revise su conexion!!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}



         /*mAuth = FirebaseAuth.getInstance();//
        mDatabase = FirebaseDatabase.getInstance().getReference();//

        edtName = findViewById(R.id.edtName);
        edtApellido = findViewById(R.id.edtApellido);//
        edtMail = findViewById(R.id.edtmail);//
        edtPhone = findViewById(R.id.edtPhone);
        edtPassword = findViewById(R.id.edtPassword);

        //edtSecureCode = findViewById(R.id.edtSecureCode);

        btnSignUp2 = findViewById(R.id.btnSignUp2);

        btnSignUp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name = edtName.getText().toString();
                apellido = edtApellido.getText().toString();
                email = edtMail.getText().toString();
                telefono = edtPhone.getText().toString();
                password = edtPassword.getText().toString();

                if (!name.isEmpty() && !apellido.isEmpty() && !email.isEmpty() && !telefono.isEmpty() && !password.isEmpty()) {

                    if (password.length() >= 6) {
                        registerUser();

                    } else {
                        Toast.makeText(SignUp.this, "La contraseña debe contener almenos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SignUp.this, "Debe completar los datos", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

   private void registerUser() {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {

                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("apellido", apellido);
                    map.put("email", email);
                    map.put("telefono", telefono);
                    map.put("password", password);

                    String id = mAuth.getCurrentUser().getUid();

                    mDatabase.child("User").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()) {
                                startActivity(new Intent(SignUp.this, Home.class));
                                finish();
                            }

                            else {
                                Toast.makeText(SignUp.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {

                    Toast.makeText(SignUp.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }

            }
       });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(SignUp.this,Home.class));
            finish();
        }
    }
}*/


