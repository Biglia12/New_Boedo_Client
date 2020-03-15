package com.example.androidfood;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.andremion.counterfab.CounterFab;
import com.example.androidfood.Common.Common;
import com.example.androidfood.Database.Database;
import com.example.androidfood.Interface.ItemClickListener;
import com.example.androidfood.Model.Categoria;
import com.example.androidfood.Model.Token;
import com.example.androidfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import io.paperdb.Paper;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;

    FirebaseDatabase database;
    DatabaseReference categoria;

    TextView txtFullName,txtFullApellido;

    RecyclerView recycler_menu;
    RecyclerView.LayoutManager layoutManager;
    FirebaseRecyclerAdapter<Categoria, MenuViewHolder> adapter;

    SwipeRefreshLayout swipeRefreshLayout;

    CounterFab fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        swipeRefreshLayout=findViewById(R.id.swipe_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Common.isConnectedToInternet(getBaseContext()))
                    loadMenu();

                else
                {
                    Toast.makeText(getBaseContext(),"Por favor revise su conexion!!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedToInternet(getBaseContext()))
                    loadMenu();

                else
                {
                    Toast.makeText(getBaseContext(),"Por favor revise su conexion!!",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });


        //Iniciar Firebase
        database = FirebaseDatabase.getInstance();
        categoria = database.getReference("Categoria");

        Paper.init(this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cartIntent = new Intent(Home.this, Cart.class);
                startActivity(cartIntent);
            }
        });

        fab.setCount(new Database(this).getCountCart());

        //Abrire los cuadrados de la derecha para que se abra el panel
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //escoger nombre para el usuario
        View headerView = navigationView.getHeaderView(0);
        txtFullName =  headerView.findViewById(R.id.txtFullName);
        txtFullApellido =  headerView.findViewById(R.id.txtFullApellido);

        if (Common.currentuser.getName() != null && Common.currentuser.getApellido()!=null)

            txtFullName.setText(Common.currentuser.getName());
            txtFullApellido.setText(Common.currentuser.getApellido());

        //cargar menu
        recycler_menu =  findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);


        loadMenu();

        updateToken(FirebaseInstanceId.getInstance().getToken());

    }

    @Override
    protected void onResume() {
        super.onResume();
        fab.setCount(new Database(this).getCountCart());

        if (adapter!= null)
            adapter.startListening();
    }

    private void updateToken(String token) {
        FirebaseDatabase db=FirebaseDatabase.getInstance();
        DatabaseReference tokens = db.getReference("Tokens");
        Token data = new Token(token,false);
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
                holder.setItemClickListener(new ItemClickListener() {
                    @Override
                    public void onClick(View view, int position, boolean isLongClick) {
                        //Obtener Categoria ID y enviar a nueva actividad
                        Intent foodList = new Intent(Home.this, FoodList.class);
                        //por que lña categoriaid es la key, asiq ahora nosotros obtenemos la key de este item
                        foodList.putExtra("CategoriaId", adapter.getRef(position).getKey());
                        startActivity(foodList);

                    }
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
            startActivity(new Intent(Home.this,SearchActivity.class));
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

        }else if (id==R.id.nav_info) {
            Intent infoIntent = new Intent(Home.this, Informacion.class);
            startActivity(infoIntent);
        }
        else if (id == R.id.nav_update_name) {
            showUpdateNameDialog();

        /*} else if (id == R.id.nav_home) {
                showAdressDialog();*/

            } else if (id == R.id.nav_log_out) {

            //eliminar rrecordarme usuario y contraseña
            FirebaseAuth.getInstance().signOut();
           // Paper.book().destroy();
            //LogOut
            Intent mainactivity = new Intent(Home.this, MainActivity.class);
            mainactivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(mainactivity);

        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

   /* private void showAdressDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Cambiar Direccion de casa");
        alertDialog.setMessage("Por favor llene toda la informacion");

        LayoutInflater inflater = this.getLayoutInflater();
        View layout_home = inflater.inflate(R.layout.home_address_layout, null);

       /final MaterialEditText edtHomeAddress = (MaterialEditText) layout_home.findViewById(R.id.edtHomeAddress);
        final MaterialEditText edtEntrecalles = (MaterialEditText) layout_home.findViewById(R.id.edtentrecalles);
        final MaterialEditText edtPisoyDepartamento = (MaterialEditText) layout_home.findViewById(R.id.edtpisoodepartamento);
        final MaterialEditText edtLocalidad = (MaterialEditText) layout_home.findViewById(R.id.edtlocalidad);

        alertDialog.setView(layout_home);

        alertDialog.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Common.currentuser.setHomeAdress(edtHomeAddress.getText().toString());
                Common.currentuser.setEntrecalles(edtEntrecalles.getText().toString());
                Common.currentuser.setPisoydepartamento(edtPisoyDepartamento.getText().toString());
                Common.currentuser.setLocalidad(edtLocalidad.getText().toString());

                FirebaseDatabase.getInstance().getReference("User")
                        .child(Common.currentuser.getPhone())
                        .setValue(Common.currentuser)
                        .addOnCompleteListener(task -> Toast.makeText(Home.this,"Direccion Actualizada Exitosa",Toast.LENGTH_SHORT).show());
            }
        });
        alertDialog.show();*/


    private void showUpdateNameDialog() {//

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Home.this);
        alertDialog.setTitle("Establece Tus datos");
        alertDialog.setMessage("Establece tus datos");

        LayoutInflater inflater = this.getLayoutInflater();
        View layout_name = inflater.inflate(R.layout.update_name_layout, null);

        final MaterialEditText edtName = (MaterialEditText) layout_name.findViewById(R.id.edtName);
        final MaterialEditText edtApellido = (MaterialEditText) layout_name.findViewById(R.id.edtapellidoo);
        final MaterialEditText edtemail = (MaterialEditText) layout_name.findViewById(R.id.edtemaill);

        alertDialog.setView(layout_name);

        //Button
        alertDialog.setPositiveButton("Editar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Change Password

                final android.app.AlertDialog waitingDialog = new SpotsDialog.Builder().setContext(Home.this).build();
                waitingDialog.show();

                // Update Name
                Map<String, Object> update_name = new HashMap<>();
                update_name.put("name", edtName.getText().toString());
                update_name.put("apellido", edtApellido.getText().toString());
                update_name.put("email", edtemail.getText().toString());

                FirebaseDatabase.getInstance()
                        .getReference("User")
                        .child(Common.currentuser.getPhone())
                        .updateChildren(update_name)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //Dismiss Dialog
                                waitingDialog.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(Home.this, "Perfil editado!!", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });

            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alertDialog.show();

    }//

    }



