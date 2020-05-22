package com.example.androidfood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;

import java.sql.Time;
import java.util.Timer;
import java.util.TimerTask;

public class SplashScreen extends AppCompatActivity {

    long Delay = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setStatusBarColor(getResources().getColor(R.color.white)); //cambiar color de la barra del telefono

        ActionBar actionbar = getSupportActionBar();
        actionbar.hide();

        Timer RunSplash = new Timer();

        TimerTask ShowSplash = new TimerTask() {
            @Override
            public void run() {

                finish();

                Intent intent = new Intent(SplashScreen.this,MainActivity.class);
                startActivity(intent);

            }
        };
         RunSplash.schedule(ShowSplash,Delay);

    }
}
