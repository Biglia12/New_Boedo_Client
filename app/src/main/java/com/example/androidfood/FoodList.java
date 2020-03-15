package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.icu.text.Transliterator;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.androidfood.Common.Common;
import com.example.androidfood.Database.Database;
import com.example.androidfood.Interface.ItemClickListener;
import com.example.androidfood.Model.Categoria;
import com.example.androidfood.Model.Food;
import com.example.androidfood.Model.Order;
import com.example.androidfood.ViewHolder.FoodViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mancj.materialsearchbar.MaterialSearchBar;
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

    //Funcionalidad buscar
    FirebaseRecyclerAdapter<Food, FoodViewHolder> searchAdapter;
    List<String>suggestList= new ArrayList<>();
    MaterialSearchBar materialSearchBar;

    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar


        //Firebase
        database=FirebaseDatabase.getInstance();
        foodList=database.getReference("Comidas");

        swipeRefreshLayout=findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //obtener intente aca
                if (getIntent() !=null)
                    categoriaId=getIntent().getStringExtra("CategoriaId");
                if (categoriaId != null && !categoriaId.isEmpty()) {

                    /*categoriaId != null && !categoriaId.isEmpty()*/


                    if (Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoriaId);
                    else {
                        Toast.makeText(FoodList.this,"Por favor revise su conexion!!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }

                // Search

               /* materialSearchBar = findViewById(R.id.searchBar);
                materialSearchBar.setHint("Ingrese su comida/bebida");
                loadSuggest(); //  escribir funcion de sugerencia para carga de firebase

                materialSearchBar.setCardViewElevation(10);
                materialSearchBar.addTextChangeListener(new TextWatcher() {

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //cuando un usuario escriba su texto, nostoros cambiaremos la sgugerencia de lista
                        List<String> suggest = new ArrayList<>();
                        for (String search:suggestList){//lazo en lista se sugerir
                            if (search.toLowerCase().contains(materialSearchBar.getText().toLowerCase()))
                                suggest.add(search);
                        }
                        materialSearchBar.setLastSuggestions(suggest);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {

                    }
                });
                materialSearchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                    @Override
                    public void onSearchStateChanged(boolean enabled) {
                        //cuando searchbar esta cerrado
                        //restaurar la sugerencia original
                        if (!enabled)
                            recyclerView.setAdapter(adapter);

                    }

                    @Override
                    public void onSearchConfirmed(CharSequence text) {
                        //cuando el buscador finaliza
                        //muestra resultado del search adapter
                        startSearch(text);

                    }

                    @Override
                    public void onButtonClicked(int buttonCode) {

                    }
                });*/
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //obtener intente aca
                if (getIntent() !=null)
                    categoriaId=getIntent().getStringExtra("CategoriaId");
                if (categoriaId != null && !categoriaId.isEmpty()) {

                    /*categoriaId != null && !categoriaId.isEmpty()*/



                    if (Common.isConnectedToInternet(getBaseContext()))
                        loadListFood(categoriaId);
                    else {
                        Toast.makeText(FoodList.this,"Por favor revise su conexion!!",Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
        });

        recyclerView=findViewById(R.id.recycler_food);

        recyclerView.hasFixedSize();
        /*sethasFixedSize(true);*/
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adapter!=null)
            adapter.startListening();
    }

    private void startSearch(CharSequence text) {
      FirebaseRecyclerOptions<Food>options =
              new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList.orderByChild("nombre").equalTo(text.toString()),Food.class).build();

      searchAdapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
          @Override
          protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull Food model) {
              holder.comida_name.setText(model.getNombre());
              Picasso.get().load(model.getImagen()).into(holder.comida_image);

              holder.setItemClickListener(new ItemClickListener() {
                  @Override
                  public void onClick(View view, int position, boolean isLongClick) {
                      //empezar actividad de la food details
                      Intent foodDetails = new Intent(FoodList.this,FoodDetail.class);
                      foodDetails.putExtra("ComidaId",searchAdapter.getRef(position).getKey());//enviar comidaid a nueva actividad
                      startActivity(foodDetails);
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
        searchAdapter.startListening();
        searchAdapter.notifyDataSetChanged();
        recyclerView.setAdapter(searchAdapter);
    }

    private void loadSuggest() {
        foodList.orderByChild("menuId").equalTo(categoriaId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot:dataSnapshot.getChildren())
                {
                    Food item = postSnapshot.getValue(Food.class);
                    suggestList.add(item.getNombre());//agregar nombre de comida para la sugerencia de la lista
                }
                materialSearchBar.setLastSuggestions(suggestList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void loadListFood(String categoriaId)  {

        FirebaseRecyclerOptions<Food>options =
        new FirebaseRecyclerOptions.Builder<Food>().setQuery(foodList.orderByChild("menuId").equalTo(categoriaId),Food.class).build();

        adapter = new FirebaseRecyclerAdapter<Food, FoodViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull FoodViewHolder holder, int position, @NonNull final Food model) {
             holder.comida_name.setText(model.getNombre());
                Picasso.get().load(model.getImagen()).into(holder.comida_image);

                //quick cart para agregar desde la imagen
                /*holder.quick_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Database(getBaseContext()).addToCart(new Order(
                                adapter.getRef(position).getKey(),
                                model.getNombre(),
                                "1",
                                model.getPrecio(),
                                model.getDescuento(),
                                model.getImagen()
                        ));
                        Toast.makeText(FoodList.this, "Agregado al Carro", Toast.LENGTH_SHORT).show();
                    }

                });*/

                final Food local=model;
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
        swipeRefreshLayout.setRefreshing(false);
    }
}
