package com.example.androidfood.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfood.Interface.ItemClickListener;
import com.example.androidfood.R;

public class FoodViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView comida_name;
    public ImageView comida_image,quick_cart;

    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public FoodViewHolder(@NonNull View itemView) {
        super(itemView);

        comida_name= itemView.findViewById(R.id.comida_name);
        comida_image = itemView.findViewById(R.id.comida_image);
        //quick_cart=itemView.findViewById(R.id.quick_cart); seria para agregar al producto al carro desde la imagen. pero aun se esta en duda si ponerlo


        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
