package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.androidfood.Common.Common;
import com.example.androidfood.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText editPhone,edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        editPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        btnSignIn=(Button) findViewById(R.id.btnSignIn);

        //Init Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ProgressDialog mDialog=new ProgressDialog(SignIn.this);
                mDialog.setMessage("Espera Por Favor...");
                mDialog.show();


                table_user.addValueEventListener(new ValueEventListener() {


                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //checkear que el usuario no exista en la base de datos
                        if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                            //obtener usuario informacion
                            mDialog.dismiss();
                            User user = dataSnapshot.child(editPhone.getText().toString()).getValue(User.class);
                            user.setPhone(editPhone.getText().toString());//establecer telefono
                            if (user.getPassword().equals(edtPassword.getText().toString())) {

                                Intent homeIntent = new Intent(SignIn.this,Home.class);
                                Common.currentuser=user;
                                startActivity(homeIntent);
                                finish();

                            } else {
                                Toast.makeText(SignIn.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                           mDialog.dismiss();
                            Toast.makeText(SignIn.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                 }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
