package com.example.androidfood.FinishOrders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.androidfood.Home;
import com.example.androidfood.OrderStatus;
import com.example.androidfood.R;

import java.util.Objects;

public class ThanksForOrder extends AppCompatActivity {

    Button btnfinish,btnseeorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_for_order);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary)); //cambiar color de la barra del telefono

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar

        btnfinish=findViewById(R.id.btnfinish);
        btnseeorder=findViewById(R.id.btnseeorder);

        btnfinish.setOnClickListener(v -> {
         Intent btnfinish = new Intent(ThanksForOrder.this, Home.class);
         startActivity(btnfinish);
        });

        btnseeorder.setOnClickListener(v -> {
            Intent btnseeorder = new Intent(ThanksForOrder.this, OrderStatus.class);
            startActivity(btnseeorder);
        });

    }
}
