package com.example.androidfood.Infomacion;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.androidfood.R;

public class Horarios extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar
    }
}
