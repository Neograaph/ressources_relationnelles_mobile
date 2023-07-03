package com.cesi.cube;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Demande de suppression de la barre de titre
        Objects.requireNonNull(getSupportActionBar()).hide(); // Masquer la barre d'action
        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE); // Récupération des préférences partagées pour la session
        long lastLoginTime = preferences.getLong("lastLoginTime", 0); // Récupération du dernier temps de connexion enregistré
        long currentTime = System.currentTimeMillis(); // Temps actuel
        long sessionDuration = 14 * 24 * 60 * 60 * 1000; // Durée de la session : 7 jours en millisecondes

        if (currentTime - lastLoginTime > sessionDuration) {
            setContentView(R.layout.activity_main); // Si la durée de la session est dépassée, définir le contenu de l'activité principale
        } else {
            Intent intent = new Intent(this, home.class); // Sinon, créer une intention pour accéder à l'activité home
            startActivity(intent); // Redirection vers l'activité home
        }
    }

    // Méthode pour accéder à l'activité de connexion
    public void login(View view) {
        Intent intent = new Intent(this, login.class); // Création d'une intention pour accéder à l'activité de connexion
        startActivity(intent); // Redirection vers l'activité de connexion
    }

    // Méthode pour accéder à l'activité d'inscription
    public void register(View view) {
        Intent intent = new Intent(this, register.class); // Création d'une intention pour accéder à l'activité d'inscription
        startActivity(intent); // Redirection vers l'activité d'inscription
    }

    // Méthode pour accéder à l'activité home
    public void goHome(View view) {
        Intent intent = new Intent(this, home.class); // Création d'une intention pour accéder à l'activité home
        startActivity(intent); // Redirection vers l'activité home
    }

    // Méthode pour accéder à l'activité MainActivity
    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class); // Création d'une intention pour accéder à l'activité MainActivity
        startActivity(intent); // Redirection vers l'activité MainActivity
    }

}