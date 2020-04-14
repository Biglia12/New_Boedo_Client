/*package com.example.androidfood.Common;

//import com.mercadopago.android.px.core.MercadoPagoCheckout;

import android.os.Bundle;
import android.preference.Preference;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfood.R;
import com.mercadopago.android.px.model.Item;
import com.mercadopago.android.px.model.Payer;

public class MercadoPago extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mercadopago);

        Preference preference = new Preference();

        Item item = new Item();
        item.setId("1234")
                .setTitle("Awesome Cotton Keyboard")
                .setQuantity(3)
                .setCategoryId("ARS")
                .setUnitPrice((int) 75);

        Payer payer = new Payer();
        payer.setEmail("anibal@gmail.com");

        preference.setPayer(payer);
        preference.appendItem(item);
        preference.save();



    /*new MercadoPagoCheckout.Builder("TEST-9d27b1dc-616d-4486-a115-eedae24cb5ca", "checkout_preference_id")
          .build()
          .startPayment(MercadoPago, requestCode);*/
    /*}*/

   /* public void startMercadoPagoCheckout(View view) {
    }
}*/