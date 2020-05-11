package com.example.androidfood;

import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andremion.counterfab.CounterFab;
import com.example.androidfood.Common.Common;
import com.example.androidfood.Database.Database;
import com.example.androidfood.Model.Food;
import com.example.androidfood.Model.Order;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;

public class FoodDetail extends AppCompatActivity {

    private Integer totalQuantity = 1;
    private TextView food_nombre, food_precio, food_descripcion;
    private ImageView food_imagen;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private CounterFab btnCart;
    //ElegantNumberButton numberButton;
    private AppCompatImageButton buttonAdd;
    private AppCompatImageButton buttonRemove;
    private TextView tvUnitProduct;
    private String comidaId = "";
    private FirebaseDatabase database;
    private DatabaseReference comidas;
    private Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar

        //firebase
        database = FirebaseDatabase.getInstance();
        comidas = database.getReference("Comidas");

        //init view
        initView();

        btnCart.setOnClickListener(v -> {
            new Database(getBaseContext()).addToCart(new Order(
                    Common.currentuser.getPhone(),/////////////////////////////
                    comidaId,
                    currentFood.getNombre(),
                    tvUnitProduct.getText().toString(),
                    currentFood.getPrecio(),
                    currentFood.getDescuento(),
                    currentFood.getImagen()
            ));
            Toast.makeText(FoodDetail.this, "Agregado al Carro", Toast.LENGTH_SHORT).show();
        });


       //btnCart.setCount(new Database(this).getCountCart());/////////////////////////////////////////////
        btnCart.setCount(new Database(this).getCountCart(Common.currentuser.getPhone()));

    }

    private void initView() {
        tvUnitProduct = findViewById(R.id.tvUnitProduct);
        buttonAdd = findViewById(R.id.buttonAdd);
        buttonRemove = findViewById(R.id.buttonRemove);

        btnCart = findViewById(R.id.btnCart);


        food_descripcion = findViewById(R.id.food_descripcion);
        food_nombre = findViewById(R.id.food_nombre);
        food_precio = findViewById(R.id.food_precio);
        food_imagen = findViewById(R.id.food_imagen);


        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.CollapsedAppbar);



        //Obtener comidaid del intent
        if (getIntent() != null) ;
        comidaId = getIntent().getStringExtra("ComidaId");
        if (!comidaId.isEmpty())
        {
           if (Common.isConnectedToInternet(getBaseContext()))
               getDetailFood(comidaId);
           else
           {
               Toast.makeText(FoodDetail.this,"Por favor revise su conexion!!",Toast.LENGTH_SHORT).show();
               return;
           }
        }

        calculateQuantity();
    }

    private void getDetailFood(String comidaId) {
        comidas.child(comidaId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentFood = dataSnapshot.getValue(Food.class);

                //set imagen
                Picasso.get().load(currentFood.getImagen()).into(food_imagen);

                collapsingToolbarLayout.setTitle(currentFood.getNombre());

                food_precio.setText(currentFood.getPrecio());

                food_nombre.setText(currentFood.getNombre());

                food_descripcion.setText(Html.fromHtml(currentFood.getDescripcion()));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void calculateQuantity() {
        tvUnitProduct.setText(String.valueOf(totalQuantity));

        buttonAdd.setOnClickListener(view -> {
            totalQuantity = totalQuantity + 1;
            tvUnitProduct.setText(String.valueOf(totalQuantity));
        });


        buttonRemove.setOnClickListener(view -> {
            if (totalQuantity > 1) {
                totalQuantity = totalQuantity - 1;
                tvUnitProduct.setText(String.valueOf(totalQuantity));
            }
        });
    }
}
