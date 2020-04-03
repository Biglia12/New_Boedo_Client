package com.example.androidfood;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Horarios extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horarios);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//Botoon para volver hacia atras. se encuentra en action bar
    }
}
