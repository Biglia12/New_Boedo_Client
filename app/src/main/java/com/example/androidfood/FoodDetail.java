package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
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

public class FoodDetail extends AppCompatActivity {

    TextView food_nombre,food_precio,food_descripcion;
    ImageView food_imagen;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton btnCart;
    ElegantNumberButton numberButton;

    String comidaId="";

    FirebaseDatabase database;
    DatabaseReference comidas;

    Food currentFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //firebase
        database=FirebaseDatabase.getInstance();
        comidas=database.getReference("Comidas");

        //iniciar view
        numberButton=(ElegantNumberButton) findViewById(R.id.number_button);
        btnCart=(FloatingActionButton)findViewById(R.id.btnCart);

        btnCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Database(getBaseContext()).addToCart(new Order(
                comidaId,
                currentFood.getNombre(),
                numberButton.getNumber(),
                currentFood.getPrecio(),
                currentFood.getDescuento()
                ));
                Toast.makeText(FoodDetail.this, "Agregado al Carro", Toast.LENGTH_SHORT).show();
        }

    });

        food_descripcion=(TextView)findViewById(R.id.food_descripcion);
        food_nombre=(TextView)findViewById(R.id.food_nombre);
        food_precio=(TextView)findViewById(R.id.food_precio);
        food_imagen=(ImageView) findViewById(R.id.food_imagen);

        collapsingToolbarLayout=(CollapsingToolbarLayout)findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.ExpandedAppbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.CollapsedAppbar);

        //Obtener comidaid del intent
        if (getIntent()!=null);
        comidaId=getIntent().getStringExtra("ComidaId");
        if (!comidaId.isEmpty())
        {
            getDetailFood(comidaId);
        }


    }

    private void getDetailFood(String comidaId) {
      comidas.child(comidaId).addValueEventListener(new ValueEventListener() {
          @Override
          public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
              currentFood =dataSnapshot.getValue(Food.class);

                //set imagen
              Picasso.get().load(currentFood.getImagen()).into(food_imagen);

              collapsingToolbarLayout.setTitle(currentFood.getNombre());

              food_precio.setText(currentFood.getPrecio());

              food_nombre.setText(currentFood.getNombre());

              food_descripcion.setText(currentFood.getDescripcion());


          }

          @Override
          public void onCancelled(@NonNull DatabaseError databaseError) {

          }
      });
    }
}
