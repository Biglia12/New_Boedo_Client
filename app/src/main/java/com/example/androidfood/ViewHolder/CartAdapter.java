package com.example.androidfood.ViewHolder;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.androidfood.Cart;
import com.example.androidfood.Common.Common;
import com.example.androidfood.Database.Database;
import com.example.androidfood.Interface.ItemClickListener;
import com.example.androidfood.Model.Order;
import com.example.androidfood.R;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class CartAdapter extends RecyclerView.Adapter<CartViewHolder>{

    private List<Order> listData = new ArrayList<>();
    private Cart cart;

    public CartAdapter(List<Order> listData, Cart cart) {
        this.listData = listData;
        this.cart = cart;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(cart);
        View itemView = inflater.inflate(R.layout.cart_layout,parent,false);
        return new CartViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        Picasso.get()
                .load(listData.get(position).getImagen())
                .resize(70, 70)
                .into(holder.cart_image);


       /* TextDrawable drawable = TextDrawable.builder()
                .buildRound(""+listData.get(position).getCantidad(), Color.RED);
        holder.img_cart_count.setImageDrawable(drawable);*/

          holder.btn_quantity.setNumber(listData.get(position).getCantidad());
          holder.btn_quantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
              @Override
              public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                  Order order = listData.get(position);
                  order.setCantidad(String.valueOf(newValue));
                  new Database(cart).updateCart(order);

                  //Calcular precio total
                  int total = 0;
                  List<Order> orders = new Database(cart).getCarts();
                  for (Order item: orders)
                      total+=(Integer.parseInt(order.getPrecio()))*(Integer.parseInt(item.getCantidad()));
                  Locale locale = new Locale("es","AR");//simbolo de moneda
                  NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);

                  cart.txtTotalPrice.setText(fmt.format(total));


              }
          });


        Locale locale = new Locale("es","AR");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        int price = (Integer.parseInt(listData.get(position).getPrecio()))*(Integer.parseInt(listData.get(position).getCantidad()));
        holder.txt_price.setText(fmt.format(price));
        holder.txt_cart_name.setText(listData.get(position).getProductoNombre());


    }

    @Override
    public int getItemCount()
    {
        return listData.size();
    }

    public Order getItem(int position){
        return listData.get(position);
    }

    public void removeItem(int position) {
        listData.remove(position);
        notifyItemRemoved(position);
    }


    public void restoreItem(Order item,int position) {
        listData.add(position,item);
        notifyItemInserted(position);
    }
}
