package com.example.androidfood.Common;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfood.R;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;


public class MercadoPago extends AppCompatActivity {

    private static final int REQUEST_CODE = 1;
    private static final int CHECKOUT_REQUEST_CODE = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mercadopago);


    }


    public void startMercadoPagoCheckout(View view) {
        //startMercadoPagoCheckout();
    }

    private void startMercadoPagoCheckout(final String checkoutPreferenceId) {
        new MercadoPagoCheckout.Builder("TEST-376b510e-a0b5-452c-8640-8d68e14349b7", checkoutPreferenceId)
                .build()
                .startPayment(MercadoPago.this, REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);
                ((TextView) findViewById(R.id.mp_results)).setText("Resultado del pago: " + payment.getPaymentStatus());
                //Done!
            } else if (resultCode == RESULT_CANCELED) {
                if (data != null && data.getExtras() != null
                        && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    final MercadoPagoError mercadoPagoError =
                            (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
                    ((TextView) findViewById(R.id.mp_results)).setText("Error: " + mercadoPagoError.getMessage());
                    //Resolve error in checkout
                } else {
                    //Resolve canceled checkout
                }
            }
        }
    }

}



