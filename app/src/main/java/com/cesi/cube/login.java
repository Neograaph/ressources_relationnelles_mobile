package com.cesi.cube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.Objects;

public class login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_login);
    }

    public void goHome(View view) {
        Intent intent = new Intent(this, home.class);
        Utils util = new Utils();
        util.faireAppelGET(this, "login=true", new Utils.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                if(response.contains("token")){
                    SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("lastLoginTime", System.currentTimeMillis());
                    editor.apply();

                    startActivity(intent);
                }else{
                    Log.e("CESI", "Le return n'est pas un token mais : "+response);
                }
            }

            @Override
            public void onError() {
                Toast.makeText(login.this, "Utilisateur invalide", Toast.LENGTH_SHORT).show();
            }
        });
        //startActivity(intent);
    }

    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}