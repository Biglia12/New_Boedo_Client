package com.example.androidfood;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.se.omapi.Session;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.andremion.counterfab.CounterFab;
import com.example.androidfood.Common.Common;
import com.example.androidfood.Database.Database;
import com.example.androidfood.Infomacion.Informacion;
import com.example.androidfood.Model.Categoria;
import com.example.androidfood.Model.Token;
import com.example.androidfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    FirebaseDatabase database;
    DatabaseReference categoria;

    private SharedPreferences sharedpreferences;

    TextView txtFullName, txtFullApellido;



    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Categoria, MenuViewHolder> adapter;


    SwipeRefreshLayout swipeRefreshLayout;

    CounterFab fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences settings = getSharedPreferences("pref_name", 0); // Este codigo nos serviraa para que alert dialog aparezca la pimera vez instalada la app. luego no aparecera
        boolean installed = settings.getBoolean("installed", false);

        if(!installed) {

            AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
            alertDialog.setTitle("Bienvenido!");
            alertDialog.setMessage("Complete los campos con su infomacion");



            LayoutInflater inflater = this.getLayoutInflater();
            View layout_name = inflater.inflate(R.layout.update_name_layout, null);

            final MaterialEditText nombre = layout_name.findViewById(R.id.edtName);
            final MaterialEditText apellido = layout_name.findViewById(R.id.edtapellidoo);
            final MaterialEditText edtemail = layout_name.findViewById(R.id.edtemaill);

            sharedpreferences = getSharedPreferences("info", MODE_PRIVATE);
            nombre.setText(sharedpreferences.getString("nombre", ""));
            apellido.setText(sharedpreferences.getString("apellido", ""));
            edtemail.setText(sharedpreferences.getString("email", ""));

            alertDialog.setView(layout_name);


            //Button
            alertDialog.setPositiveButton("Editar", (dialog, which) -> {

                final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(Home.this).build();
                waitingDialog.show();


                //guardara el nombre en edittext
                SharedPreferences.Editor preferencesEditor = sharedpreferences.edit();
                if (nombre.getText().length() > 0) // Not empty
                    preferencesEditor.putString("nombre", String.valueOf(nombre.getText()));
                if (apellido.getText().length() > 0) // Not empty
                    preferencesEditor.putString("apellido", String.valueOf(apellido.getText()));
                if (edtemail.getText().length() > 0) // Not empty
                    preferencesEditor.putString("email", String.valueOf(edtemail.getText()));




                // Update Name
                Map<String, Object> update_name = new HashMap<>();
                update_name.put("name", nombre.getText().toString());
                update_name.put("apellido", apellido.getText().toString());
                update_name.put("email", edtemail.getText().toString());

                FirebaseDatabase.getInstance()
                        .getReference("User")
                        .child(Common.currentuser.getPhone())
                        .updateChildren(update_name)
                        .addOnCompleteListener(task -> {
                            //Dismiss Dialog
                            waitingDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(Home.this, "¡¡Perfil editado!!", Toast.LENGTH_SHORT).show();
                                preferencesEditor.commit();
                            }


                        });


            }).setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

            alertDialog.show();


            alertDialog.setView(layout_name);


            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("installed", true);
            editor.commit();

        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);




        swipeRefreshLayout = findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (Common.isConnectedToInternet(getBaseContext()))
                loadMenu();

            else {
                Toast.makeText(getBaseContext(), "Por favor revise su conexion!!", Toast.LENGTH_SHORT).show();
                return;
            }
        });

        swipeRefreshLayout.post(() -> {
            if (Common.isConnectedToInternet(getBaseContext()))
                loadMenu();

            else {
                Toast.makeText(getBaseContext(), "Por favor revise su conexion!!", Toast.LENGTH_SHORT).show();
                return;
            }
        });


        //Iniciar Firebase
        database = FirebaseDatabase.getInstance();
        categoria = database.getReference("Categoria");

        Paper.init(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);
        });


        //fab.setCount(new Database(this).getCountCart());//////////////////////////////


        fab.setCount(new Database(this).getCountCart(Common.currentuser.getPhone()));

        //Abrire los cuadrados de la derecha para que se abra el panel

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(Home.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //escoger nombre para el usuario
                View headerView = navigationView.getHeaderView(0);
                txtFullName = headerView.findViewById(R.id.txtFullName);
                txtFullApellido = headerView.findViewById(R.id.txtFullApellido);
                    txtFullName.setText(Common.currentuser.getName());
                txtFullApellido.setText(Common.currentuser.getApellido());
                invalidateOptionsMenu();

            }
        };

        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //escoger nombre para el usuario
       /* View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullApellido = headerView.findViewById(R.id.txtFullApellido);

        if (Common.currentuser.getName() != null && Common.currentuser.getApellido() != null)
            txtFullName.setText(Common.currentuser.getName());
        txtFullApellido.setText(Common.currentuser.getApellido());*/


        //cargar menu
        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);


        loadMenu();

        updateToken(FirebaseInstanceId.getInstance().getToken());


    }

    @Override
    protected void onResume() {
        super.onResume();
        // fab.setCount(new Database(this).getCountCart());////////////////////////////////////////////////
        fab.setCount(new Database(this).getCountCart(Common.currentuser.getPhone()));

        if (adapter != null)
            adapter.startListening();
    }

    private void updateToken(String token) {
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token, false);
        tokens.child(Common.currentuser.getPhone()).setValue(data);
    }

    //se cargara lo de firebase
    private void loadMenu() {
        FirebaseRecyclerOptions<Categoria> options =
                new FirebaseRecyclerOptions.Builder<Categoria>()
                        .setQuery(categoria, Categoria.class)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Categoria, MenuViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int i, @NonNull Categoria model) {
                holder.txtMenuNombre.setText(model.getNombre());

                Picasso.get().load(model.getImagen()).into(holder.imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
                holder.txtMenuNombre.setText(model.getNombre());
                final Categoria clickItem = model;
                holder.setItemClickListener((view, position, isLongClick) -> {
                    //Obtener Categoria ID y enviar a nueva actividad
                    Intent foodList = new Intent(Home.this, FoodList.class);
                    //por que la categoriaid es la key, asiq ahora nosotros obtenemos la key de este item
                    foodList.putExtra("CategoriaId", adapter.getRef(position).getKey());
                    startActivity(foodList);

                });
            }

            @NonNull
            @Override
            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
                View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);
                return new MenuViewHolder(view);
            }
        };
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recycler_menu.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        recycler_menu.setAdapter(adapter);
        swipeRefreshLayout.setRefreshing(false);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
            startActivity(new Intent(Home.this, SearchActivity.class));
        }


        return super.onOptionsItemSelected(item);
    }

    //esto fue creado cuando se implementa setanavigationitemselected. es la ocpion dos y asi se puede abrir los cuadrados
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_menu) {


        } else if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);

        } else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(Home.this, OrderStatus.class);
            startActivity(orderIntent);

        } else if (id == R.id.nav_info) {
            Intent infoIntent = new Intent(Home.this, Informacion.class);
            startActivity(infoIntent);
        } else if (id == R.id.nav_update_name) {
            showUpdateNameDialog();

        } else if (id == R.id.nav_log_out) {

            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);
            builder.setTitle("Confirmacion");
            builder.setMessage("Estas seguro que quiere salir de la sesion?");
            builder.setPositiveButton("SI", (dialog, which) -> {
                FirebaseAuth.getInstance().signOut();
                Intent mainactivity = new Intent(Home.this, MainActivity.class);
                mainactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainactivity);
            });
            builder.setNegativeButton("NO", (dialog, which) -> {

                // Do nothing
                dialog.dismiss();
            });

            AlertDialog alert = builder.create();
            alert.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }




    private void showUpdateNameDialog() {//


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Establece Tus datos");
        alertDialog.setMessage("Complete con su informacion");

        LayoutInflater inflater = this.getLayoutInflater();
        View layout_name = inflater.inflate(R.layout.update_name_layout, null);

        final MaterialEditText nombre = layout_name.findViewById(R.id.edtName);
        final MaterialEditText apellido = layout_name.findViewById(R.id.edtapellidoo);
        final MaterialEditText edtemail = layout_name.findViewById(R.id.edtemaill);

        sharedpreferences = getSharedPreferences("info", MODE_PRIVATE);
        nombre.setText(sharedpreferences.getString("nombre", ""));
        apellido.setText(sharedpreferences.getString("apellido", ""));
        edtemail.setText(sharedpreferences.getString("email", ""));



        alertDialog.setView(layout_name);


        //Button
        alertDialog.setPositiveButton("Editar", (dialog, which) -> {




        })
                .setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog=alertDialog.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {

            if(nombre.getText().toString().isEmpty() || apellido.getText().toString().isEmpty()  || edtemail.getText().toString().isEmpty() ){
                Toast.makeText(Home.this, "No dejar Campos en blanco", Toast.LENGTH_SHORT).show();
            }

            else {

                final AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(Home.this).build();
                waitingDialog.show();


                //guardara el nombre en edittext
                SharedPreferences.Editor preferencesEditor = sharedpreferences.edit();
                if (nombre.getText().length() > 0) // Not empty
                    preferencesEditor.putString("nombre", String.valueOf(nombre.getText()));
                if (apellido.getText().length() > 0) // Not empty
                    preferencesEditor.putString("apellido", String.valueOf(apellido.getText()));
                if (edtemail.getText().length() > 0) // Not empty
                    preferencesEditor.putString("email", String.valueOf(edtemail.getText()));


                // Update Name
                Map<String, Object> update_name = new HashMap<>();
                update_name.put("name", nombre.getText().toString());
                update_name.put("apellido", apellido.getText().toString());
                update_name.put("email", edtemail.getText().toString());


                FirebaseDatabase.getInstance()
                        .getReference("User")
                        .child(Common.currentuser.getPhone())
                        .updateChildren(update_name)
                        .addOnCompleteListener(task -> {
                            //Dismiss Dialog
                            waitingDialog.dismiss();
                            if (task.isSuccessful()) {
                                Toast.makeText(Home.this, "¡¡Perfil editado!!", Toast.LENGTH_SHORT).show();
                                preferencesEditor.commit();
                                dialog.dismiss();
                            }

                        });
            }
        });


        }

    }





