package com.example.androidfood.Infomacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andremion.counterfab.CounterFab;
import com.example.androidfood.R;
import com.mercadopago.android.px.internal.view.FixAppBarLayoutBehavior;

public class Horarios extends AppCompatActivity {

CounterFab fabTelefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary)); //cambiar color de la barra del telefono

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar

        fabTelefono = findViewById(R.id.fabtelefono);

        fabTelefono.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + 43920183))));

    }
}
