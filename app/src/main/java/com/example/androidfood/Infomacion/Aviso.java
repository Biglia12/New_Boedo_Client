package com.example.androidfood.Infomacion;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.androidfood.R;

public class Aviso extends AppCompatActivity {

    ImageView imgMaps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso);

        imgMaps = findViewById(R.id.imageMaps);

        imgMaps.setOnClickListener(v -> {
            Uri uri = Uri.parse("https://www.google.com/maps/d/u/0/embed?mid=1lbabaGxrBjMg1TzqxFMEhOlpR2WbFnuw&ll=-34.75798979870917%2C-58.39903985000001&z=15");
            Intent imgmaps = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(imgmaps);
        });
    }
}
