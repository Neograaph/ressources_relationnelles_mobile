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
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import android.widget.ImageView;
import android.widget.Toast;


import com.google.android.material.navigation.NavigationView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class home extends AppCompatActivity {
    ActionBarDrawerToggle toggle; // Variable pour la gestion de la barre d'action
    private List<Post> postList; // Liste des posts
    private PostAdapter postAdapter; // Adaptateur pour les posts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Demande de suppression de la barre de titre
        Objects.requireNonNull(getSupportActionBar()).hide(); // Masquer la barre d'action
        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE); // Récupération des préférences partagées pour la session
        long lastLoginTime = preferences.getLong("lastLoginTime", 0); // Récupération du dernier temps de connexion
        long currentTime = System.currentTimeMillis(); // Temps actuel en millisecondes
        long sessionDuration = 7 * 24 * 60 * 60 * 1000; // Durée de session de 7 jours en millisecondes

        // Vérification de la durée de la session
        if (currentTime - lastLoginTime > sessionDuration) {
            setContentView(R.layout.activity_home); // Affichage de l'activité home
        } else {
            setContentView(R.layout.activity_home_con); // Affichage de l'activité home_con
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout); // Récupération du layout du tiroir

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle); // Ajout du listener du tiroir
        toggle.syncState(); // Synchronisation de l'état du tiroir

        ImageView togglerButton = findViewById(R.id.nav_toogle); // Bouton pour ouvrir/fermer le tiroir
        togglerButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START); // Fermer le tiroir s'il est déjà ouvert
            } else {
                drawerLayout.openDrawer(GravityCompat.START); // Ouvrir le tiroir s'il est fermé
            }
        });

        RecyclerView postRecyclerView = findViewById(R.id.postRecyclerView); // Récupération de la RecyclerView pour les posts

        // Initialisation de la liste des posts
        postList = new ArrayList<>();
        postList.add(new Post("Utilisateur 1", "Contenu du post 1", "https://latavernedutesteur.files.wordpress.com/2017/11/testss.png", "Contrat de chantier"));
        postList.add(new Post("Utilisateur 2", "Contenu du post 2", null, "Documentation RE"));
        postList.add(new Post("Utilisateur 2", "Contenu du post 3", "https://download.vikidia.org/vikidia/fr/images/thumb/9/95/Fr%C3%BChlingsallee_Tulpenbl%C3%BCte_2010.jpg/1200px-Fr%C3%BChlingsallee_Tulpenbl%C3%BCte_2010.jpg", null));
        // Ajoutez d'autres posts à la liste
        Utils utils = new Utils();
        utils.faireAppelGET(this, "Ressources", new Utils.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                String jsonString = response;
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonString);
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Gérez les erreurs liées à la conversion du JSON en tableau JSON
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject ressourceObject = jsonArray.getJSONObject(i);

                        // Récupérez les valeurs spécifiques de la ressource
                        String titre = ressourceObject.getString("titre");
                        String contenu = ressourceObject.getString("contenu");
                        String documentUrl = null;

                        if (!ressourceObject.isNull("documentId")) {
                            JSONObject documentObject = ressourceObject.getJSONObject("document");
                            documentUrl = "http://cube-cesi.ddns.net:4200/assets/documents/" + documentObject.getString("chemin");
                        }

                        postList.add(new Post(titre, contenu, documentUrl, null));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Gérez les erreurs liées à la récupération des valeurs de la ressource
                    }
                }


            }

            @Override
            public void onError() {
                Toast.makeText(home.this, "Pas d'accès BDD", Toast.LENGTH_SHORT).show();
            }
        });


        // Configuration de la RecyclerView
        postRecyclerView = findViewById(R.id.postRecyclerView);
        postAdapter = new PostAdapter(postList);
        postRecyclerView.setAdapter(postAdapter);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    // Méthode pour gérer la connexion
    public void login(MenuItem view) {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    // Méthode pour gérer l'inscription
    public void register(MenuItem view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    // Méthode pour gérer la déconnexion
    public void logout(MenuItem view) {
        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("lastLoginTime", 0); // Réinitialisation du dernier temps de connexion
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    // Méthode pour accéder à la page d'accueil
    public void goHome(MenuItem view) {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
    }

    // Méthode pour accéder au profil
    public void goProfil(MenuItem view) {
        Intent intent = new Intent(this, Profil.class);
        startActivity(intent);
    }

    // Méthode pour accéder à la liste d'amis
    public void goFriends(MenuItem view) {
        Intent intent = new Intent(this, Profil.class);
        intent.putExtra("friends", "true");
        startActivity(intent);
    }

    // Méthode pour accéder aux paramètres
    public void goSettings(MenuItem view) {
        Intent intent = new Intent(this, Profil.class);
        startActivity(intent);
    }

    // Méthode pour gérer le clic sur le bouton "Like"
    public void like(View view) {
        ImageView like = findViewById(R.id.likeButton);
        if (like.getColorFilter() != null)
            like.setColorFilter(null); // Si le bouton était déjà liké, on l'enlève
        else
            like.setColorFilter(getResources().getColor(R.color.red)); // Sinon, on applique la couleur rouge pour le liker
    }


    public void PostRessource(){
        Utils utils = new Utils();
        Map<String, String> params = new HashMap<String, String>();
        String titre = "Test";
        String content = "Test";
        params.put("titre", titre);
        params.put("contenu", content);
        params.put("utilisateurId", "1");
        params.put("categorieId", "test");
        params.put("visibiliteId", "Public");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.put("dateCreation", Date.from(Instant.now()).toString());
        }
        params.put("valider", "true");
        utils.faireAppelPOST(this, "Ressources", params, new Utils.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(home.this, "Ressource ajoutée", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError() {

            }
        });
    }

    public void sendPost(View view) {
        PostRessource();
    }
}