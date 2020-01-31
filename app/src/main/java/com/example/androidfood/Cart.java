package com.example.androidfood;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfood.Common.Common;
import com.example.androidfood.Database.Database;
import com.example.androidfood.Model.Order;
import com.example.androidfood.Model.Request;
import com.example.androidfood.ViewHolder.CartAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;

public class Cart extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

    TextView txtTotalPrice;
    Button btnPlace;

    List<Order> cart= new ArrayList<>();

    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

     //Firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        //init
        recyclerView=(RecyclerView) findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        txtTotalPrice = (TextView) findViewById(R.id.total);
        btnPlace =(Button) findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
                showAlertDialog();
              

        }

    });

        loadListFood();


    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Un paso mas!!");
        alertDialog.setMessage("Ingrese su direccion: ");

        final EditText edtAdress = new EditText(Cart.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edtAdress.setLayoutParams(lp);
        alertDialog.setView(edtAdress);//Agregar editar texto para el dialogo de alerta
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Request request = new Request(
                        Common.currentuser.getPhone(),
                        Common.currentuser.getName(),
                        edtAdress.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        cart
                );
            //enviar a firebase
                //usaremos Syste.CurrentMilli para llave
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                        //Eliminar carro
                new Database(getBaseContext()).cleanCart();
                Toast.makeText(Cart.this, "Muchas gracias,por su orden", Toast.LENGTH_SHORT).show();
                finish();



            }
        });
     alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {

         }
     });

     alertDialog.show();
    }

    private void loadListFood() {
        cart=new Database(this).getCarts();
        adapter=new CartAdapter(cart,this);
        recyclerView.setAdapter(adapter);

        //Calcular precio total
        int total = 0;
        for (Order order: cart)
            total+=(Integer.parseInt(order.getPrecio()))*(Integer.parseInt(order.getCantidad()));
        Locale locale = new Locale("es","AR");//simbolo de moneda
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }
}
