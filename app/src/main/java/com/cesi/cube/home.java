package com.cesi.cube;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.cesi.cube.fragment.RGPD;
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
    private static final int REQUEST_CODE_IMAGE = 0;
    private static final int REQUEST_CODE_DOCUMENT = 2;
    private RGPD rgpdFragment; // Fragment RGPD
    ActionBarDrawerToggle toggle; // Variable pour la gestion de la barre d'action
    private List<Post> postList; // Liste des posts
    private PostAdapter postAdapter; // Adaptateur pour les posts
    RecyclerView postRecyclerView; // RecyclerView pour les posts

    Uri imageUri; // Uri de l'image
    Uri documentUri; // Uri du document

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // Demande de suppression de la barre de titre
        Objects.requireNonNull(getSupportActionBar()).hide(); // Masquer la barre d'action
        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE); // Récupération des préférences partagées pour la session
        long lastLoginTime = preferences.getLong("lastLoginTime", 0); // Récupération du dernier temps de connexion
        long currentTime = System.currentTimeMillis(); // Temps actuel en millisecondes
        long sessionDuration = 14 * 24 * 60 * 60 * 1000; // Durée de session de 7 jours en millisecondes
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

        // Initialisation de la liste des posts
        postList = new ArrayList<>();
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
                        String dateCreation = ressourceObject.getString("dateCreation");
                        String imageUrl = null;
                        String documentUrl = null;

                        JSONObject utilisateurObject = ressourceObject.getJSONObject("utilisateur");
                        String author = utilisateurObject.getString("nom") + " " + utilisateurObject.getString("prenom");

                        if (!ressourceObject.isNull("documentId")) {
                            JSONObject documentObject = ressourceObject.getJSONObject("document");
                            Log.d("extension", documentObject.getString("extension"));
                            if(documentObject.getString("extension").equals(".pdf")) {
                                documentUrl = "http://cube-cesi.ddns.net:4200/assets/documents/" + documentObject.getString("chemin");
                            } else {
                                imageUrl = "http://cube-cesi.ddns.net:4200/assets/documents/" + documentObject.getString("chemin");
                            }
                        }

                        postList.add(new Post(titre, contenu, imageUrl, documentUrl, author, dateCreation));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        // Gérez les erreurs liées à la récupération des valeurs de la ressource
                    }
                }

                // Configuration de la RecyclerView
                postRecyclerView = findViewById(R.id.postRecyclerView);
                postAdapter = new PostAdapter(postList);
                postRecyclerView.setAdapter(postAdapter);
                postRecyclerView.setLayoutManager(new LinearLayoutManager(home.this));

                // Notifier les changements de données à l'adaptateur
                postAdapter.notifyDataSetChanged();


            }

            @Override
            public void onError(String response) {
                Toast.makeText(home.this, "Pas d'accès BDD", Toast.LENGTH_SHORT).show();
            }
        });

        // Initialisation des spinner uniquement si connecté
        if (currentTime - lastLoginTime < sessionDuration) {
            Spinner categorySpinner = findViewById(R.id.category);
            String[] options = {"Communication", "Cultures", "Développement personnel", "Intelligence émotionnelle", "Loisirs", "Monde professionnel", "Parentalité", "Qualité de vie", "Recherche de sens", "Santé physique", "Santé psychique", "Spiritualité", "Vie affective"}; // Remplacez par vos options réelles

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(adapter);

            Spinner typeSpinner = findViewById(R.id.typeRessource);
            String[] options2 = {"Activité / Jeu à réaliser", "Article", "Carte défi", "Cours au format PDF", "Exercice / Atelier", "Fiche de lecture", "Jeu en ligne", "Vidéo"}; // Remplacez par vos options réelles

            ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options2);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            typeSpinner.setAdapter(adapter2);


            Button btnAjouterImage = findViewById(R.id.btnAjouterImage);
            btnAjouterImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Code pour la sélection d'image
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_IMAGE);
                }
            });

            Button btnAjouterDoc = findViewById(R.id.btnAjouterDocument);
            btnAjouterDoc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Code pour la sélection d'image
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivityForResult(intent, REQUEST_CODE_DOCUMENT);
                }
            });
        }




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK && data != null) {
            // Récupérer l'image sélectionnée
            imageUri = data.getData();
            // Faire quelque chose avec l'image sélectionnée (par exemple, l'envoyer dans le post)
        }
        if (requestCode == REQUEST_CODE_DOCUMENT && resultCode == RESULT_OK && data != null) {
            // Récupérer l'image sélectionnée
            documentUri = data.getData();
            // Faire quelque chose avec l'image sélectionnée (par exemple, l'envoyer dans le post)
        }
    }


    public void showRGPD(MenuItem view) {
        String url = "http://cube-cesi.ddns.net:4200/mentionsLegales";

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        startActivity(intent);
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


    public void PostRessource(View view){
        Utils utils = new Utils();
        Map<String, String> params = new HashMap<String, String>();
        String titre = findViewById(R.id.titreRessource).toString();
        String content = findViewById(R.id.contentRessource).toString();
        String category = findViewById(R.id.category).toString();
        String typeRessource = findViewById(R.id.typeRessource).toString();
        params.put("titre", titre);
        params.put("contenu", content);
        params.put("utilisateurId", "1");
        params.put("categorieId", category);
        params.put("visibiliteId", "Public");
        params.put("ressource",null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params.put("dateCreation", Date.from(Instant.now()).toString());
        }
        params.put("valider", "true");
        utils.POST(this, "Ressources", params, new Utils.VolleyCallback() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(home.this, "Ressource ajoutée", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(String response) {
                Toast.makeText(home.this, response, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public void sendPost(View view) {
        LinearLayout postRessource = findViewById(R.id.postRessource);
        if (postRessource.getVisibility() == View.GONE) {
            postRessource.setVisibility(View.VISIBLE);
        } else {
            postRessource.setVisibility(View.GONE);
        }
    }
}