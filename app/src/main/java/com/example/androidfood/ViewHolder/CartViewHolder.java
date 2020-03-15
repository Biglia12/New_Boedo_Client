package com.example.androidfood.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.androidfood.Common.Common;
import com.example.androidfood.Interface.ItemClickListener;
import com.example.androidfood.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
        ,View.OnCreateContextMenuListener{

    public TextView txt_cart_name,txt_price;
    public ElegantNumberButton btn_quantity;
    public ImageView cart_image;

    public RelativeLayout view_background;
    public LinearLayout view_foreground;

    //public TextView tvUnitProduct2;
    //public AppCompatImageButton buttonRemove2;//
    // public AppCompatImageButton buttonAdd2;//
    //public ImageView img_cart_count;


    private ItemClickListener itemClickListener;

    public void setTxt_cart_name(TextView txt_cart_name) {
        this.txt_cart_name = txt_cart_name;
    }

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);
        txt_cart_name = itemView.findViewById(R.id.cart_item_name);
        txt_price = itemView.findViewById(R.id.cart_item_Price);
        btn_quantity = itemView.findViewById(R.id.btn_quantity);
        cart_image = itemView.findViewById(R.id.cart_image);
        view_background=itemView.findViewById(R.id.view_background);
        view_foreground=itemView.findViewById(R.id.view_foreground);

        //tvUnitProduct2 = itemView.findViewById(R.id.tvUnitProduct2);//
        //buttonAdd2=itemView.findViewById(R.id.buttonAdd2);//
        // buttonRemove2=itemView.findViewById(R.id.buttonRemove2);//




        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Selecciona la accion");
        //contextMenu.add(0,0,getAdapterPosition(), Common.DELETE);

    }
}
