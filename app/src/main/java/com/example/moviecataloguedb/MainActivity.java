package com.example.moviecataloguedb;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.moviecataloguedb.db.MovieFavoriteHelper;
import com.example.moviecataloguedb.db.TvFavoriteHelper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_movie, R.id.navigation_tv_shows, R.id.navigation_favorite)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        MovieFavoriteHelper movieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
        movieFavoriteHelper.open();
        TvFavoriteHelper tvFavoriteHelper = TvFavoriteHelper.getInstance(getApplicationContext());
        tvFavoriteHelper.open();

    }

}
