package com.example.androidfood;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.androidfood.Common.Common;
import com.example.androidfood.Database.Database;
import com.example.androidfood.Helper.RecyclerItemTouchHelper;
import com.example.androidfood.Interface.RecyclerItemTouchHelperListener;
import com.example.androidfood.Model.MyResponse;
import com.example.androidfood.Model.Notification;
import com.example.androidfood.Model.Order;
import com.example.androidfood.Model.Request;
import com.example.androidfood.Model.Sender;
import com.example.androidfood.Model.Token;
import com.example.androidfood.Remote.APIService;
import com.example.androidfood.ViewHolder.CartAdapter;
import com.example.androidfood.ViewHolder.CartViewHolder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import info.hoang8f.widget.FButton;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Cart extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference requests;

   public TextView txtTotalPrice;
    Button btnPlace;

    private String address;

    List<Order> cart= new ArrayList<>();

    CartAdapter adapter;

    APIService mService;

    RelativeLayout rootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar

        mService=Common.getFCMClient();


     //Firebase
        database=FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");

        //init
        recyclerView=findViewById(R.id.listCart);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        rootLayout=findViewById(R.id.rootLayout);

        //swipe para eliminar
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);


        txtTotalPrice = findViewById(R.id.total);
        btnPlace = findViewById(R.id.btnPlaceOrder);

        btnPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.size()>0)
                showAlertDialog();
                else
                    Toast.makeText(Cart.this,"Su carro esta vacio!!!",Toast.LENGTH_SHORT).show();
              

        }

    });

        loadListFood();


    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("Un paso mas!!");
        alertDialog.setMessage("Seleccione las opciones ");

        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment = inflater.inflate(R.layout.order_address_comment,null);

        final MaterialEditText edtAdress = order_address_comment.findViewById(R.id.edtAddress);
        final MaterialEditText edtComment = order_address_comment.findViewById(R.id.edtComment);
        final MaterialEditText edtentrecalles = order_address_comment.findViewById(R.id.edtentrecalles);
        final MaterialEditText edtpisodepartamento = order_address_comment.findViewById(R.id.edtpisoodepartamento);
        final MaterialEditText edtLocalidad = order_address_comment.findViewById(R.id.edtlocalidad);

        /*final RadioButton rdiHomeAddress = (RadioButton) order_address_comment.findViewById(R.id.rdiHomeAddress);//veremos seria para implementar en la direccion de su casa

        rdiHomeAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {//veremos
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                {
                   if (Common.currentuser.getHomeAdress() !=null ||!TextUtils.isEmpty(Common.currentuser.getHomeAdress() )
                           && (Common.currentuser.getEntrecalles() !=null  || !TextUtils.isEmpty(Common.currentuser.getEntrecalles())
                           && (Common.currentuser.getPisoydepartamento() !=null || !TextUtils.isEmpty(Common.currentuser.getPisoydepartamento())
                           && (Common.currentuser.getLocalidad() !=null || !TextUtils.isEmpty(Common.currentuser.getLocalidad()) ))))

                   {
                        address=Common.currentuser.getHomeAdress();
                        address=Common.currentuser.getEntrecalles();
                        address=Common.currentuser.getPisoydepartamento();
                        address=Common.currentuser.getLocalidad();
                   }
                   else
                   {
                       Toast.makeText(Cart.this,"Por favor suba los datos de su domicilio",Toast.LENGTH_SHORT).show();
                   }

                }
            }
        });*/

        alertDialog.setView(order_address_comment);
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Eliminar carro
                new Database(getBaseContext()).cleanCart();
                Request request = new Request(
                        Common.currentuser.getPhone(),
                        Common.currentuser.getName(),
                        edtAdress.getText().toString(),
                        edtentrecalles.getText().toString(),
                        edtpisodepartamento.getText().toString(),
                        edtLocalidad.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        "0", // seria los estados
                        edtComment.getText().toString(),
                        cart
                );
            //enviar a firebase
                //usaremos System.CurrentMilli para llave

                String order_number =String.valueOf(System.currentTimeMillis());
                requests.child(order_number).setValue(request);


                Toast.makeText(Cart.this, "Muchas gracias,por su orden", Toast.LENGTH_SHORT).show();
                finish();

                sendNotificationOrder(order_number);


            }

            private void sendNotificationOrder(final String order_number) {
              DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
              Query data = tokens.orderByChild("serverToken").equalTo(true);
              data.addValueEventListener(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      for (DataSnapshot postSnapShot:dataSnapshot.getChildren()) {

                          Token serverToken = postSnapShot.getValue(Token.class);

                          Notification notification = new Notification("New Boedo", "Tienes una nueva orden" + order_number);
                          Sender content = new Sender(serverToken.getToken(),notification);

                          mService.sendNotification(content)
                                  .enqueue(new Callback<MyResponse>() {
                                      @Override
                                      public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {

                                          if(response.code() == 200) {
                                              if (response.body().success == 1) {
                                                  Toast.makeText(Cart.this, "Muchas gracias,por su orden", Toast.LENGTH_SHORT).show();
                                                  finish();
                                              } else {
                                                  Toast.makeText(Cart.this, "Hubo un problema", Toast.LENGTH_SHORT).show();
                                              }

                                          }

                                      }

                                      @Override
                                      public void onFailure(Call<MyResponse> call, Throwable t) {
                                          Log.e("ERROR","FALLA"+t.getMessage());

                                      }
                                  });

                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });

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
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

        //Calcular precio total
        int total = 0;
        for (Order order: cart)
            total+=(Integer.parseInt(order.getPrecio()))*(Integer.parseInt(order.getCantidad()));
        Locale locale = new Locale("es","AR");//simbolo de moneda
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

        txtTotalPrice.setText(fmt.format(total));

    }
//esto seria para eliminar el item de la compra manteniendo presiondo sobre la orden
    /*@Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if (item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return super.onContextItemSelected(item);
    }

    private void deleteCart(int order) {
        // it will remove item at List<Order> by position
        cart.remove(order);
        // after that it will delete old data from SQlite
        new Database(this).cleanCart();
        // and finally , we will update  new data from List<order> to Sqlite
        for (Order item : cart)
            new Database(this).addToCart(item);
        //referesh
        loadListFood();
    }*/

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartViewHolder) {
            String name = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()).getProductoNombre();

            final Order deleteItem = ((CartAdapter) recyclerView.getAdapter()).getItem(viewHolder.getAdapterPosition()); // item
            final int deleteIndex = viewHolder.getAdapterPosition(); // position

            adapter.removeItem(deleteIndex);
            new Database(getBaseContext()).remoFormCart(deleteItem.getProductoId(), Common.currentuser.getPhone());

            //Calcular precio total
            int total = 0;
            List<Order> orders = new Database(getBaseContext()).getCarts();
            for (Order item : orders)
                total += (Integer.parseInt(item.getPrecio())) * (Integer.parseInt(item.getCantidad()));
            Locale locale = new Locale("es", "AR");//simbolo de moneda
            NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

            txtTotalPrice.setText(fmt.format(total));

            Snackbar snackbar = Snackbar.make(rootLayout, name + " Eliminado del carro", Snackbar.LENGTH_LONG);
            snackbar.setAction("Agregar de nuevo", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deleteItem, deleteIndex);
                    new Database(getBaseContext()).addToCart(deleteItem);


                    //Calcular precio total
                    int total = 0;
                    List<Order> orders = new Database(getBaseContext()).getCarts();
                    for (Order item : orders)
                        total += (Integer.parseInt(item.getPrecio())) * (Integer.parseInt(item.getCantidad()));
                    Locale locale = new Locale("es", "AR");//simbolo de moneda
                    NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                    txtTotalPrice.setText(fmt.format(total));

                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
