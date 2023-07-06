package com.cesi.cube;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.time.Instant;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class register extends AppCompatActivity {

    EditText input_identifiant;
    EditText input_identifiant2;
    EditText input_mail;
    EditText input_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Demande de suppression de la barre de titre
        Objects.requireNonNull(getSupportActionBar()).hide(); // Masquer la barre d'action
        setContentView(R.layout.activity_register); // Définir le contenu de l'activité d'inscription

        input_identifiant = findViewById(R.id.input_identifiant); // Récupération de l'EditText pour l'identifiant
        input_identifiant2 = findViewById(R.id.input_identifiant2); // Récupération de l'EditText pour l'identifiant
        input_mail = findViewById(R.id.input_mail); // Récupération de l'EditText pour l'identifiant
        input_password = findViewById(R.id.input_password); // Récupération de l'EditText pour le mot de passe
    }

    // Méthode pour accéder à l'activité home
    public void goHome(View view) {
        Intent intent = new Intent(this, home.class);
        Intent intentLogin = new Intent(this, login.class);
        Utils util = new Utils();
        Map<String, String> params = new HashMap<>();
        params.put("nom", input_identifiant.getText().toString()); // Récupération de l'identifiant saisi
        params.put("prenom", input_identifiant2.getText().toString()); // Récupération du mot de passe saisi
        params.put("email", input_mail.getText().toString()); // Récupération de l'identifiant saisi
        params.put("motDePasse", input_password.getText().toString()); // Récupération du mot de passe saisi
        params.put("telephone", "None"); // Récupération du mot de passe saisi



// Obtenez la date actuelle
        Date currentDate = new Date();

// Définissez le format souhaité
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

// Formatez la date actuelle selon le format spécifié
        String formattedDate = dateFormat.format(currentDate);
        params.put("dateNaissance", formattedDate); // Récupération du mot de passe saisi
        Log.d("CESI", "Date : " + formattedDate);



        // Vérifications de validité des champs saisis
        if (input_identifiant.getText().toString().equals("")) {
            Toast.makeText(this, "Veuillez saisir un identifiant", Toast.LENGTH_SHORT).show();
            return;
        }
        if (input_password.getText().toString().equals("")) {
            Toast.makeText(this, "Veuillez saisir un mot de passe", Toast.LENGTH_SHORT).show();
            return;
        }
        if (input_password.getText().toString().length() > 12) {
            Toast.makeText(this, "Le mot de passe doit contenir moins de 12 caractères", Toast.LENGTH_SHORT).show();
            return;
        }

        // Appel à la méthode faireAppelPOST de la classe Utils pour effectuer une requête POST
        util.POST(this,"Utilisateurs", params, new Utils.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                if (response.contains("token")) {
                    SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putLong("lastLoginTime", System.currentTimeMillis());
                    editor.putLong("token", Long.parseLong(response));
                    editor.apply();

                    startActivity(intent);
                } else {
                    Toast.makeText(register.this, "Identifiant Invalide", Toast.LENGTH_SHORT).show();
                    Log.e("CESI", "Le return n'est pas un token mais : " + response);
                }
            }

            @Override
            public void onError(String response) {
                //Toast.makeText(register.this, response, Toast.LENGTH_SHORT).show();
                startActivity(intentLogin);
            }
        });
    }

    // Méthode pour accéder à l'activité MainActivity
    public void goMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}