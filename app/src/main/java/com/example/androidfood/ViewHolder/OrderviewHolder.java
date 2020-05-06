package com.example.androidfood.ViewHolder;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.androidfood.Interface.ItemClickListener;
import com.example.androidfood.R;

public class OrderviewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView btnDetail,txtOrderEstados,txtOrderTelefono,txtOrderDireccion,txtOrderDate,txtTotal;

    private ItemClickListener itemClickListener;

    public OrderviewHolder(@NonNull View itemView) {
        super(itemView);
        txtOrderDireccion = itemView.findViewById(R.id.order_direccion);
        //txtOrderId = itemView.findViewById(R.id.order_id);
        txtTotal=itemView.findViewById(R.id.order_total);
        txtOrderEstados = itemView.findViewById(R.id.order_estados);
        txtOrderTelefono = itemView.findViewById(R.id.order_telefono);
        txtOrderDate = itemView.findViewById(R.id.order_date);

        btnDetail= itemView.findViewById(R.id.btnDetail);

        itemView.setOnClickListener(this);


    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View view) {
                itemClickListener.onClick(view,getAdapterPosition(),false);
    }
}
