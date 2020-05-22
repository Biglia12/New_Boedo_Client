package com.example.androidfood.FinishOrders;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.example.androidfood.Home;
import com.example.androidfood.OrderStatus;
import com.example.androidfood.R;

import java.util.Objects;

public class ThanksForOrderDelivery extends AppCompatActivity {
    Button btnfinishdel,btnseeorderdel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thanks_for_order_delivery);

        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary)); //cambiar color de la barra del telefono

        btnfinishdel=findViewById(R.id.btnfinishdelivery);
        btnseeorderdel=findViewById(R.id.btnseeorderdelivery);

        btnfinishdel.setOnClickListener(v -> {
            Intent btnfinish = new Intent(ThanksForOrderDelivery.this, Home.class);
            startActivity(btnfinish);
        });

        btnseeorderdel.setOnClickListener(v -> {
            Intent btnseeorder = new Intent(ThanksForOrderDelivery.this, OrderStatus.class);
            startActivity(btnseeorder);
        });

    }
}
