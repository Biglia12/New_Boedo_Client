package com.example.androidfood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfood.Common.Common;
import com.example.androidfood.Model.User;
import com.example.androidfood.Remote.APIService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpEmail extends AppCompatActivity {

    EditText email, pass, name, apellido, phone;
    private Button btnregistrarseemail;
    private Button btniniciarsesion;
    FirebaseUser user;


    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    ProgressDialog process;
    APIService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mService = Common.getFCMClient();
        process = new ProgressDialog(SignUpEmail.this);
        process.setMessage("Por favor espera!");
        info();
        mAuth = FirebaseAuth.getInstance();

        btniniciarsesion = findViewById(R.id.btninciarsesion);
        btniniciarsesion.setOnClickListener(v -> {
            Intent btniniciarsesion = new Intent(SignUpEmail.this, SignIn.class);
            startActivity(btniniciarsesion);
        });


        btnregistrarseemail.setOnClickListener(v -> {
            process.show();
            btnregistarse();
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void info() {
        email = findViewById(R.id.edtmail);
        pass = findViewById(R.id.edtPassword);
        name = findViewById(R.id.edtname);
        apellido = findViewById(R.id.edtapellido);
        phone = findViewById(R.id.edtphone);
        btnregistrarseemail = findViewById(R.id.btnRegistrarseemail);

    }


    private void btnregistarse() {
        String Email = name.getText().toString().trim(); //nose por que se desrodena en firebase. Dificil encontrar el error ya que el codigo se encuentra con logica
        String Pass = pass.getText().toString().trim();
        String Name = phone.getText().toString().trim();
        String Apellido = apellido.getText().toString().trim();
        String Phone = email.getText().toString().trim();
        final User Cliente = new User(Email, Pass, Name, Apellido,Phone, "cliente");

        if (Email.isEmpty() || Pass.isEmpty() || Name.isEmpty() || Apellido.isEmpty() || Phone.isEmpty()) {
            process.dismiss();
            Toast.makeText(this, "Completar los campos vacios!. ", Toast.LENGTH_SHORT).show();
        } else {
            mAuth.createUserWithEmailAndPassword(email.getText().toString(), pass.toString()).addOnCompleteListener(task -> {

                if (task.isSuccessful()) {
                    user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(task1 -> {
                        if (task.isSuccessful()) {
                            process.dismiss();
                            Toast.makeText(SignUpEmail.this, "Registro exitoso. Por favor verifique el correo electrónico", Toast.LENGTH_SHORT).show();

                            String userID = user.getUid();
                            mDatabase.child("User").child(userID).setValue(Cliente);
                            //chuyen ve man hinh chinh
                            startActivity(new Intent(SignUpEmail.this, SignIn.class));


                        } else {
                            Toast.makeText(SignUpEmail.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }


                    });
                } else {
                    process.dismiss();
                    Toast.makeText(SignUpEmail.this, "La cuenta ya existe", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}





/*Toast.makeText(SignUpEmail.this, "Registrado con exito", Toast.LENGTH_SHORT).show();
                                                              edtemail.setText("");
                                                             edtPassword.setText("");*/


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
                        Toast.makeText(SignUpEmail.this, "La contraseña debe contener almenos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(SignUpEmail.this, "Debe completar los datos", Toast.LENGTH_SHORT).show();
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
                                startActivity(new Intent(SignUpEmail.this, Home.class));
                                finish();
                            }

                            else {
                                Toast.makeText(SignUpEmail.this, "No se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {

                    Toast.makeText(SignUpEmail.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }

            }
       });


    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser()!=null){
            startActivity(new Intent(SignUpEmail.this,Home.class));
            finish();
        }
    }
}*/


