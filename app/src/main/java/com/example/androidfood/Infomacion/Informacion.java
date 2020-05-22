package com.example.androidfood.Infomacion;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidfood.R;

public class Informacion extends AppCompatActivity {

    ImageView ImageFace, ImageContacto, ImageUbicacion, ImageEmail, ImageHora,ImageAviso,ImageWhats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary)); //cambiar color de la barra del telefono

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar

        ImageFace = findViewById(R.id.imagenface);

        ImageFace.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.facebook.com/newboedo/");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        });
        ImageContacto = findViewById(R.id.imagenContacto);

        ImageContacto.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + 43920183))));

        ImageUbicacion = findViewById(R.id.imagenubica);

        ImageUbicacion.setOnClickListener(v -> {
            Uri uri = Uri.parse("geo:-34.75976749,-58.39763135");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<-34.75976749>,<-58.39763135>?q=<-34.75976749>,<-58.39763135>(Label+New Boedo)"));
            startActivity(intent);
        });
        ImageEmail = findViewById(R.id.imageemail);

        ImageEmail.setOnClickListener((View v) -> {

            Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
            intent.putExtra(Intent.EXTRA_SUBJECT, "");
            intent.putExtra(Intent.EXTRA_TEXT, "");
            intent.setData(Uri.parse("mailto:newboedo@yahoo.com.ar"));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        });
        ImageHora = findViewById(R.id.imgHora);

        ImageHora.setOnClickListener((View v) -> {
            Intent horaintent = new Intent(Informacion.this, Horarios.class);
            startActivity(horaintent);
        });

        ImageAviso = findViewById(R.id.imageAviso);

        ImageAviso.setOnClickListener((View v) -> {
            Intent avisointent = new Intent(Informacion.this, Aviso.class);
            startActivity(avisointent);

        });
        //1554055250

        ImageWhats = findViewById(R.id.imageWhats);


        ImageWhats.setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + 1554055250))));

    }
}
