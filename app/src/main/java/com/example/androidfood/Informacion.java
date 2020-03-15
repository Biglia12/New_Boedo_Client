package com.example.androidfood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class Informacion extends AppCompatActivity {

    ImageView ImageFace, ImageContacto, ImageUbicacion, ImageEmail, ImageHora;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar

        ImageFace = findViewById(R.id.imagenface);

        ImageFace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.facebook.com/newboedo/");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        ImageContacto = findViewById(R.id.imagenContacto);

        ImageContacto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_DIAL).setData(Uri.parse("tel:" + 43920183)));
            }
        });

        ImageUbicacion = findViewById(R.id.imagenubica);

        ImageUbicacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:-34.75976749,-58.39763135");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:<-34.75976749>,<-58.39763135>?q=<-34.75976749>,<-58.39763135>(Label+New Boedo)"));
                startActivity(intent);
            }

        });
        ImageEmail = findViewById(R.id.imageemail);

        ImageEmail.setOnClickListener((View v)-> {

                Intent intent = new Intent(Intent.ACTION_SENDTO); // it's not ACTION_SEND
                intent.putExtra(Intent.EXTRA_SUBJECT, "");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                intent.setData(Uri.parse("mailto:newboedo@yahoo.com.ar"));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

        });
        ImageHora = findViewById(R.id.imgHora);

        ImageHora.setOnClickListener((View v) -> {
            Intent horaintent = new Intent(Informacion.this,Horarios.class);
            startActivity(horaintent);

        });
    }
}