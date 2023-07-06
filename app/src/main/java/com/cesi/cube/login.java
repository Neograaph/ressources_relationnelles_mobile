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
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class login extends AppCompatActivity {

    EditText input_identifiant; // Champ de saisie de l'identifiant
    EditText input_password; // Champ de saisie du mot de passe

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Demande de suppression de la barre de titre
        Objects.requireNonNull(getSupportActionBar()).hide(); // Masquer la barre d'action
        setContentView(R.layout.activity_login); // Définir le contenu de l'activité comme le layout de l'activité de connexion
        input_identifiant = findViewById(R.id.input_identifiant); // Récupération de la référence du champ de saisie de l'identifiant
        input_password = findViewById(R.id.input_password); // Récupération de la référence du champ de saisie du mot de passe
    }

    // Méthode pour accéder à la page d'accueil
    public void goHome(View view) {
        Intent intent = new Intent(this, home.class); // Création d'une intention pour accéder à l'activité home
        Utils util = new Utils(); // Instance de l'utilitaire Utils

        Map<String, String> params = new HashMap<>(); // Création d'une carte pour stocker les paramètres de la requête POST
        params.put("email", input_identifiant.getText().toString()); // Ajout de l'identifiant saisi comme paramètre "Email"
        params.put("motDePasse", input_password.getText().toString()); // Ajout du mot de passe saisi comme paramètre "MotDePasse"

        // Vérification des saisies de l'utilisateur
        if (input_identifiant.getText().toString().equals("")) {
            Toast.makeText(this, "Veuillez saisir un identifiant", Toast.LENGTH_SHORT).show(); // Affichage d'un message d'erreur si l'identifiant est vide
            return;
        }
        if (input_password.getText().toString().equals("")) {
            Toast.makeText(this, "Veuillez saisir un mot de passe", Toast.LENGTH_SHORT).show(); // Affichage d'un message d'erreur si le mot de passe est vide
            return;
        }

        // Vérification des identifiants de connexion administrateur
        if (input_identifiant.getText().toString().equals("admin") && input_password.getText().toString().equals("admin")) {
            SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE); // Récupération des préférences partagées pour la session
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong("lastLoginTime", System.currentTimeMillis()); // Enregistrement du temps de connexion actuel
            editor.apply();

            startActivity(intent); // Redirection vers l'activité home
            return;
        }

        // Appel de la méthode faireAppelPOST de l'utilitaire Utils pour effectuer une requête POST
        util.POST(this, "Utilisateurs/authenticate", params, new Utils.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                if (response.contains("token")) {
                    SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("lastLoginTime", System.currentTimeMillis());
                    // Convertir l'objet JSON en une chaîne de caractères
                    String jsonString = response.toString();
                    editor.putString("token", jsonString);
                    editor.apply();

                    startActivity(intent);
                } else {
                    Toast.makeText(login.this, "Identifiant Invalide", Toast.LENGTH_SHORT).show(); // Affichage d'un message d'erreur en cas d'identifiant invalide
                    Log.e("CESI", "Le return n'est pas un token mais : " + response);
                }
            }

            @Override
            public void onError(String response) {
                Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show(); // Affichage d'un message d'erreur en cas d'utilisateur invalide
            }
        });
    }

    // Méthode pour accéder à l'activité MainActivity
    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class); // Création d'une intention pour accéder à l'activité MainActivity
        startActivity(intent); // Redirection vers l'activité MainActivity
    }

}