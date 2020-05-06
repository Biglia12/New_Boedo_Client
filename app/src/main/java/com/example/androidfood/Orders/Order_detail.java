package com.example.androidfood.Orders;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.example.androidfood.Common.Common;
import com.example.androidfood.R;

public class Order_detail extends AppCompatActivity {

    TextView order_phone,order_address,order_total,order_date;
    String order_id_value="";
    RecyclerView IsFoods;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        order_phone=findViewById(R.id.order_telefono);
        order_address=findViewById(R.id.order_direccion);
        order_total=findViewById(R.id.order_total);
        order_date=findViewById(R.id.order_date);

        IsFoods = findViewById(R.id.IsFoods);
        IsFoods.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        IsFoods.setLayoutManager(layoutManager);

        if (getIntent()!=null)
            order_id_value=getIntent().getStringExtra("OrderId");


        order_phone.setText(Common.currentRequest.getTelefono());
        order_total.setText(Common.currentRequest.getTotal());
        order_address.setText(Common.currentRequest.getDireccion());


        OrderDetailAdapter adapter = new OrderDetailAdapter(Common.currentRequest.getComidas());
        adapter.notifyDataSetChanged();
        IsFoods.setAdapter(adapter);

    }
}

