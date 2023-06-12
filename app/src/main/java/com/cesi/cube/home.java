package com.cesi.cube;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class home extends AppCompatActivity {

    ActionBarDrawerToggle toggle;

    ScrollView templateContainer;
    private List<Post> postList;
    private RecyclerView postRecyclerView;
    private PostAdapter postAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_home);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        toggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.nav_home:
                    Log.d("clicked home", "clicked home");
                    break;
                default:
                    Log.d("TAGGED", String.valueOf(item.getItemId()));
                    break;
            }
            return true;
        });
        ImageView togglerButton = findViewById(R.id.nav_toogle);
        togglerButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        RecyclerView postRecyclerView = findViewById(R.id.postRecyclerView);

        // Initialisez la liste de posts
        postList = new ArrayList<>();
        postList.add(new Post("Utilisateur 1", "Contenu du post 1", ""));
        postList.add(new Post("Utilisateur 2", "Contenu du post 2", ""));
        // Ajoutez d'autres posts à la liste

        // Configurez la RecyclerView
        postRecyclerView = findViewById(R.id.postRecyclerView);
        postAdapter = new PostAdapter(postList);
        postRecyclerView.setAdapter(postAdapter);
        postRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }

    public void login(MenuItem view)
    {
        Intent intent = new Intent(this, login.class);
        startActivity(intent);
    }

    public void register(MenuItem view)
    {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
    }

    public void onTemplateButtonClicked(View view) {
        // Effacez le contenu actuel du conteneur de modèle
        //templateContainer.removeAllViews();

        // Inflatez le modèle XML et ajoutez-le au conteneur
        View templateView = LayoutInflater.from(this).inflate(R.layout.feed_tp, null);
        templateContainer.addView(templateView);

    }
}