package com.example.re;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.re.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {
    private Button buttonLogin;
    private Button buttonCreate;
    private Button buttonGo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreate = findViewById(R.id.buttonCreate);
        buttonGo = findViewById(R.id.buttonGo);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin();
            }
        });

        buttonCreate.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                openCreate();
            }
        });

        buttonGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSite();
            }
        });
    }

    public void openLogin(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void openCreate(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
    public void openSite(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}