package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidfood.Common.Common;
import com.example.androidfood.Interface.ItemClickListener;
import com.example.androidfood.Model.Categoria;
import com.example.androidfood.Model.Food;
import com.example.androidfood.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> adapter;
    String categoriaId="";





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);




        //Firebase
        database=FirebaseDatabase.getInstance();
        foodList=database.getReference("Comidas");

        recyclerView=findViewById(R.id.recycler_food);

        recyclerView.hasFixedSize();
        /*sethasFixedSize(true);*/
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
      //obtener intente aca
        if (getIntent() !=null)

            categoriaId=getIntent().getStringExtra("CategoriaId");

        if (categoriaId != null && !categoriaId.isEmpty()) {
/*categoriaId != null && !categoriaId.isEmpty()*/

            if (Common.isConnectedToInternet(getBaseContext()))
                loadListFood(categoriaId);
            else {
                Toast.makeText(this, "Please Check the Internet Connection", Toast.LENGTH_SHORT).show();
                return;
            }
        }

    }


    private void loadListFood(String categoriaId)  {

        FirebaseRecyclerOptions<Food>options =
        new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList.orderByChild("menuId").equalTo(categoriaId),Food.class).build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int i, @NonNull final Food model) {
             holder.comida_name.setText(model.getNombre());
                Picasso.get().load(model.getImagen()).into(holder.comida_image);

                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                   //Empezar nueva actividad
                        Intent foodDetail = new Intent(FoodList.this,FoodDetail.class);
                        foodDetail.putExtra("ComidaId",adapter.getRef(position).getKey());//Enviar comida a nueva actividad
                        startActivity(foodDetail);
                    }
                });
            }
            @NonNull
            @Override
            public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
                return new FoodViewHolder(view);
            }

        };
        //setiamos el adaptador
        adapter.startListening();
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }
}
