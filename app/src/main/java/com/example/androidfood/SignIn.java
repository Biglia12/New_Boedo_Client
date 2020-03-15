package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

import io.paperdb.Paper;

public class SignIn extends AppCompatActivity {

    private EditText mEditTextEmail2;
    private EditText mEditTextPassword2;
    private Button btnSignIn;
    private Button ResetPassword;

    //private String email="";
    //private String password = "";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        EditText editPhone, edtPassword2;
        Button btnSignIn;
        CheckBox ckbRemeber;
        //TextView txtForgotPass;

        FirebaseDatabase database;
        DatabaseReference table_user;


            edtPassword2 = (MaterialEditText) findViewById(R.id.edtPassword2);
            editPhone = (MaterialEditText) findViewById(R.id.edtPhone);
            btnSignIn = findViewById(R.id.btnSignIn);
            ckbRemeber = findViewById(R.id.ckbRemeber);

            //txtForgotPass=findViewById(R.id.txtForgotPass);


            //iniciar papel
            Paper.init(this);

            //Init Firebase
            database = FirebaseDatabase.getInstance();
            table_user = database.getReference("User");

        /*txtForgotPass.setOnClickListener(new View.OnClickListener() { hhghjjjhjghjhjhjhjhjh
            @Override
            public void onClick(View v) {
                showForgotPassDialog();
            }
        });*/

            btnSignIn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (Common.isConnectedToInternet(getBaseContext())) {

                        //garabar usuario y contraseña
                   /* if (ckbRemeber.isChecked()) {
                        Paper.book().write(Common.USER_KEY, editPhone.getText().toString());
                        Paper.book().write(Common.PWD_KEY, edtPassword.getText().toString());}}{}{}}{}

                    }*/

                        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                        mDialog.setMessage("Espera Por Favor...");
                        mDialog.show();


                        table_user.addValueEventListener(new ValueEventListener() {


                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //checkear que el usuario no exista en la base de datos
                                if (dataSnapshot.child(editPhone.getText().toString()).exists()) {
                                    //obtener usuario informacion
                                    mDialog.dismiss();
                                    User user = dataSnapshot.child(editPhone.getText().toString())
                                            .getValue(User.class);
                                    user.setPhone(editPhone.getText().toString());//establecer telefono
                                    if (user.getPassword().equals(edtPassword2.getText().toString())) {

                                        Intent homeIntent = new Intent(SignIn.this, Home.class);
                                        Common.currentuser = user;
                                        startActivity(homeIntent);
                                        finish();

                                    } else {
                                        Toast.makeText(SignIn.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    mDialog.dismiss();
                                    Toast.makeText(SignIn.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    } else {
                        Toast.makeText(SignIn.this, "Por favor revise su conexion!!", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

            });

        }

    }





   /* private void showForgotPassDialog() {

        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Olvido la contraseña");
        builder.setMessage("Ingresar codigo de seguidad");

        LayoutInflater inflater = this.getLayoutInflater();
        View forgot_view = inflater.inflate(R.layout.forgot_password_layout,null);

        builder.setView(forgot_view);
        builder.setIcon(R.drawable.ic_security_black_24dp);

        final MaterialEditText edtPhone=forgot_view.findViewById(R.id.edtPhone);
        final MaterialEditText edtSecureCode=forgot_view.findViewById(R.id.edtSecureCode);

        builder.setPositiveButton("SI", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User user = dataSnapshot.child(edtPhone.getText().toString())
                                .getValue(User.class);

                        if (user.getSecureCode().equals(edtSecureCode.getText().toString()))
                            Toast.makeText(SignIn.this,"Su contraseña : "+user.getPassword(),Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(SignIn.this,"Incorrecto codigo de seguridad",Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.show();

    }
}*/



        /* mAuth=FirebaseAuth.getInstance();


        mEditTextEmail2 = (EditText) findViewById(R.id.edtemail2);
        mEditTextPassword2 = (EditText) findViewById(R.id.edtPassword2);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        ResetPassword= findViewById(R.id.btnResetPassword);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email=mEditTextEmail2.getText().toString();
                password=mEditTextPassword2.getText().toString();

                if (!email.isEmpty()&&!password.isEmpty()){
                    loginUser();
                }
                else {
                    Toast.makeText(SignIn.this,"Complete los datos",Toast.LENGTH_SHORT).show();
                }
            }
        });

        ResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignIn.this,RessetPassword.class));
            }
        });


    }
    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startActivity(new Intent(SignIn.this,Home.class));
                    finish();
                }
                else{
                    Toast.makeText(SignIn.this,"No se pudo iniciar Sesion , compruebe los datos",Toast.LENGTH_SHORT).show();
                }
            }
        });*/







