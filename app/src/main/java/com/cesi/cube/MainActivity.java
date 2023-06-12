package com.cesi.cube;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    @SuppressLint("AppCompatMethod")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
    }

    public void login(View view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    public void register(View view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}