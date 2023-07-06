package com.cesi.cube;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class Profil extends AppCompatActivity {
    ActionBarDrawerToggle toggle;
    private List<Post> postList;
    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;

    private List<Relation> relList;
    private RecyclerView relRecyclerView;
    private RelationAdapter relAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Demande de suppression de la barre de titre
        Objects.requireNonNull(getSupportActionBar()).hide(); // Masquer la barre d'action
        setContentView(R.layout.activity_profil); // Définir le contenu de l'activité profil

        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE); // Récupération des préférences partagées pour la session
        long lastLoginTime = preferences.getLong("lastLoginTime", 0); // Récupération du dernier temps de connexion enregistré
        long currentTime = System.currentTimeMillis(); // Temps actuel
        long sessionDuration = 14 * 24 * 60 * 60 * 1000; // Durée de la session : 7 jours en millisecondes

        if (currentTime - lastLoginTime > sessionDuration) {
            Log.d("cesi", "RIIIRIR");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        TextView txtUsername = findViewById(R.id.txtUsername);

        // Récupérer la chaîne JSON à partir des préférences partagées
        String savedJsonString = preferences.getString("token", "");
        String encodedJwtToken = null;
        try {
            JSONObject jsonToken = new JSONObject(savedJsonString);
            encodedJwtToken = jsonToken.getString("token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("cesi", "savedJsonString : " + encodedJwtToken);
        try {
            Claims claims = Jwts.parser().parseClaimsJwt(encodedJwtToken).getBody();

            // Récupérez les claims (informations) du token décodé
            String username = claims.getSubject();
            Date expirationDate = claims.getExpiration();
            String utilisateurId = claims.get("UtilisateurId", String.class);
            txtUsername.setText(utilisateurId);

            Log.d("cesi", "username : " + username);
            Log.d("cesi", "expirationDate : " + expirationDate);
            Log.d("cesi", "utilisateurId : " + utilisateurId);

        } catch (Exception e) {
            // Gérez les erreurs de décodage du token JWT
            e.printStackTrace();
        }

        // Initialisation de ActionBarDrawerToggle pour gérer l'ouverture/fermeture du tiroir de navigation
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Gestion du clic sur le bouton de bascule pour ouvrir/fermer le tiroir de navigation
        ImageView togglerButton = findViewById(R.id.nav_toogle);
        togglerButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        relRecyclerView = findViewById(R.id.relRecyclerView);
        postRecyclerView = findViewById(R.id.postRecyclerView);
        Intent intent = getIntent();

        if(intent.getStringExtra("friends") != null)
        {
            relRecyclerView.setVisibility(View.VISIBLE);
            postRecyclerView.setVisibility(View.GONE);
        }

        // Configuration de la RecyclerView pour afficher les posts
        postRecyclerView = findViewById(R.id.postRecyclerView);
        postList = new ArrayList<>();
        /*
        postList.add(new Post("Utilisateur 2", "Contenu du post 2", null, "https://docker-backend-zip.zip"));
        postList.add(new Post("Utilisateur 2", "Contenu du post 3", "https://download.vikidia.org/vikidia/fr/images/thumb/9/95/Fr%C3%BChlingsallee_Tulpenbl%C3%BCte_2010.jpg/1200px-Fr%C3%BChlingsallee_Tulpenbl%C3%BCte_2010.jpg", null));
        postList.add(new Post("", "", null, null));
        */
        postAdapter = new PostAdapter(postList);
        postRecyclerView.setAdapter(postAdapter);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configuration de la RecyclerView pour afficher les relations
        relRecyclerView = findViewById(R.id.relRecyclerView);
        relList = new ArrayList<>();
        relList.add(new Relation("Marie", "Amie"));
        relList.add(new Relation("Jean", "Ami"));
        relList.add(new Relation("Paul", "Ami"));
        relAdapter = new RelationAdapter(relList);
        relRecyclerView.setAdapter(relAdapter);
        relRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Méthode pour accéder à l'activité Profil
    public void goProfil(MenuItem view) {
        Intent intent = new Intent(this, Profil.class);
        startActivity(intent);
    }

    // Méthode pour accéder à l'activité de connexion
    public void login(MenuItem view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    // Méthode pour accéder à l'activité d'inscription
    public void register(MenuItem view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    // Méthode pour déconnecter l'utilisateur
    public void logout(MenuItem view) {
        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("lastLoginTime", 0);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Méthode pour accéder à l'activité Profil en affichant les amis
    public void goFriends(MenuItem view) {
        Intent intent = new Intent(this, Profil.class);
        intent.putExtra("friends", "true");
        startActivity(intent);
    }

    // Méthode pour accéder à l'activité des paramètres
    public void goSettings(MenuItem view) {
        Intent intent = new Intent(this, Profil.class);
        startActivity(intent);
    }

    // Méthode pour accéder à l'activité home
    public void goHome(MenuItem view) {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

}