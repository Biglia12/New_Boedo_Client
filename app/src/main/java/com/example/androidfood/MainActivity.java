package com.example.androidfood;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfood.Common.Common;
import com.example.androidfood.Model.User;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {

    Button btnregistrarse;
    TextView txtSlogan;
    Button btnRegistrarseporemail;


    private FirebaseAuth firebaseAuth;//
    private FirebaseAuth.AuthStateListener listener;//
    private List<AuthUI.IdpConfig> providers;//

    private DatabaseReference users;//
    private FirebaseDatabase database;

    private static final int REQUEST_CODE = 7171;//

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(listener);

    }

    @Override
    protected void onStop() {
        if (listener != null)
            firebaseAuth.removeAuthStateListener(listener);
        super.onStop();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();
        providers = Arrays.asList(new AuthUI.IdpConfig.PhoneBuilder().build());
        listener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user != null)
                checkUserFromFirebase(user);
        };
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance();
        users = database.getReference("User");

        btnregistrarse = findViewById(R.id.btnRegistrarse);

        txtSlogan = findViewById(R.id.txtSlogan);
        //tipo de letra
        Typeface face = Typeface.createFromAsset(getAssets(), "fonts/sacredbridge.ttf");
        txtSlogan.setTypeface(face);

       /*btnRegistrarseporemail=findViewById(R.id.btnRegistrarseporemail);
        btnRegistrarseporemail.setOnClickListener(v -> {
            Intent btnregistarseporemail = new Intent(MainActivity.this, SignUpEmail.class);
            startActivity(btnregistarseporemail);
        });*/


        btnregistrarse.setOnClickListener(v -> {
//                Intent signUp = new Intent(MainActivity.this, SignUpEmail.class);
//                startActivity(signUp);
            startLogininSystem();
        });

    }

    private void checkUserFromFirebase(FirebaseUser user) {
//Show Dialog
        final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(this).build();
        waitingDialog.show();
        waitingDialog.setMessage("Por favor espere!");
        waitingDialog.setCancelable(false);

        users.orderByKey().equalTo(user.getPhoneNumber())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.child(user.getPhoneNumber()).exists()) { // If not exists
                            User newUser = new User();
                            newUser.setPhone(user.getPhoneNumber());
                            newUser.setName("");
                            newUser.setApellido("");


                            users.child(user.getPhoneNumber())
                                    .setValue(newUser)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(MainActivity.this, "Registrado con exito!", Toast.LENGTH_SHORT).show();

                                            // Login
                                            users.child(user.getPhoneNumber())
                                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot1) {
                                                            User localUser = dataSnapshot1.getValue(User.class);

                                                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                                            Common.currentuser = localUser;
                                                            startActivity(homeIntent);

                                                            //Dismiss dialog
                                                            waitingDialog.dismiss();
                                                            finish();
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {
                                                            waitingDialog.dismiss();
                                                            Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();

                                                        }
                                                    });
                                        }
                                    });

                        } else { // If exists
                            // Just Login
                            // Login
                            users.child(user.getPhoneNumber())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            User localUser = dataSnapshot.getValue(User.class);

                                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                                            Common.currentuser = localUser;
                                            startActivity(homeIntent);

                                            //Dismiss dialog
                                            waitingDialog.dismiss();
                                            finish();
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {
                                            waitingDialog.dismiss();
                                            Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        waitingDialog.dismiss();
                        Toast.makeText(MainActivity.this, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void startLogininSystem() {
        startActivityForResult(AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            IdpResponse response = IdpResponse.fromResultIntent(data);
            if (resultCode == RESULT_OK) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            } else {
                Toast.makeText(this, "Fallo el registro", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void login(final String phone, final String pwd) {
        //copiar codigo de signin

        //Init Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        if (Common.isConnectedToInternet(getBaseContext())) {

            final ProgressDialog mDialog = new ProgressDialog(MainActivity.this);
            mDialog.setMessage("Espera Por Favor...");
            mDialog.show();


            table_user.addValueEventListener(new ValueEventListener() {


                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    //checkear que el usuario no exista en la base de datos
                    if (dataSnapshot.child(phone).exists()) {
                        //obtener usuario informacion
                        mDialog.dismiss();

                        User user = dataSnapshot.child(phone).getValue(User.class);
                        user.setPhone(phone);//establecer telefono
                        if (user.getPass().equals(pwd)) {

                            Intent homeIntent = new Intent(MainActivity.this, Home.class);
                            Common.currentuser = user;
                            startActivity(homeIntent);
                            finish();

                        } else {
                            Toast.makeText(MainActivity.this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        mDialog.dismiss();
                        Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {
            Toast.makeText(MainActivity.this, "Por favor revise su conexion!!", Toast.LENGTH_SHORT).show();
            return;
        }


    }

}