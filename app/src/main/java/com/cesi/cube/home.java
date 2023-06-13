package com.cesi.cube;

import androidx.annotation.NonNull;
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
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

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
        Utils util = new Utils();
        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        long lastLoginTime = preferences.getLong("lastLoginTime", 0);
        long currentTime = System.currentTimeMillis();
        long sessionDuration = 7 * 24 * 60 * 60 * 1000; // 7 jours en millisecondes

        if (currentTime - lastLoginTime > sessionDuration) {
            setContentView(R.layout.activity_home);
        } else {
            setContentView(R.layout.activity_home_con);
        }

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
        postList.add(new Post("Utilisateur 1", "Contenu du post 1", "https://latavernedutesteur.files.wordpress.com/2017/11/testss.png", "https://docker-backend-nothing.jar"));
        postList.add(new Post("Utilisateur 2", "Contenu du post 2", null, "https://docker-backend-zip.zip"));
        postList.add(new Post("Utilisateur 2", "Contenu du post 3", "https://download.vikidia.org/vikidia/fr/images/thumb/9/95/Fr%C3%BChlingsallee_Tulpenbl%C3%BCte_2010.jpg/1200px-Fr%C3%BChlingsallee_Tulpenbl%C3%BCte_2010.jpg", null));
        postList.add(new Post("","",null, null));
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

    public void logout(MenuItem view)
    {
        SharedPreferences preferences = getSharedPreferences("session", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("lastLoginTime", 0);
        editor.apply();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void like(View view){
        ImageView like = findViewById(R.id.likeButton);
        if(like.getColorFilter() != null)
            like.setColorFilter(null);
        else
            like.setColorFilter(getResources().getColor(R.color.red));
    }
}