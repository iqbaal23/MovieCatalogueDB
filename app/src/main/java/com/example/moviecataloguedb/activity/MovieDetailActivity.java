package com.example.moviecataloguedb.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.moviecataloguedb.R;
import com.example.moviecataloguedb.db.MovieFavoriteHelper;
import com.example.moviecataloguedb.parcelable.MovieItems;
import com.example.moviecataloguedb.widget.FavoriteWidget;

import java.util.Objects;

import static com.example.moviecataloguedb.widget.FavoriteWidget.UPDATE_WIDGET;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String EXTRA_MOVIE = "extra_movie";
    TextView tvName, tvScore, tvDate, tvOverview;
    ImageView imgPhoto;

    MovieItems movieItems;
    private MovieFavoriteHelper movieFavoriteHelper;

    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ActionBar actionBar = getSupportActionBar();
        Objects.requireNonNull(actionBar).setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        movieItems = getIntent().getParcelableExtra(EXTRA_MOVIE);
        actionBar.setTitle(movieItems.getName());

        tvName = findViewById(R.id.tv_judul);
        tvScore = findViewById(R.id.tv_score);
        tvDate = findViewById(R.id.tv_date);
        tvOverview = findViewById(R.id.tv_overview);
        imgPhoto = findViewById(R.id.iv_poster);

        movieFavoriteHelper = MovieFavoriteHelper.getInstance(getApplicationContext());
        Cursor cursor = movieFavoriteHelper.checkMovieFavorite(movieItems.getId()+"");
        isFavorite = cursor.getCount() > 0;

        tvName.setText(movieItems.getName());
        tvScore.setText(String.format("%s: %s", getString(R.string.score), movieItems.getScore()));
        tvDate.setText(movieItems.getDate());
        tvOverview.setText(movieItems.getOverview());

        String imageUrl = "https://image.tmdb.org/t/p/w780/";
        Glide.with(this)
                .load(imageUrl + movieItems.getPhoto())
                .transform(new RoundedCorners(45))
                .into(imgPhoto);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isFavorite){
            getMenuInflater().inflate(R.menu.favorite_button, menu);
        } else {
            getMenuInflater().inflate(R.menu.not_favorite_button, menu);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == R.id.btn_favorite){
            if (isFavorite){
                long result = movieFavoriteHelper.deleteMovieFavorite(movieItems.getId());
                if (result > 0){
                    Toast.makeText(getApplicationContext(), R.string.success_delete_message, Toast.LENGTH_SHORT).show();
                    sendUpdateFavoriteList(getApplicationContext());
                    finish();
                }
            } else{
                long result = movieFavoriteHelper.insertMovieFavorite(movieItems);
                if (result > 0){
                    Toast.makeText(getApplicationContext(), R.string.success_added_message, Toast.LENGTH_SHORT).show();
                    sendUpdateFavoriteList(getApplicationContext());
                    finish();
                }
            }
        } else {
            onBackPressed();
        }
        return true;
    }

    public void sendUpdateFavoriteList(Context context){
        Intent i = new Intent(context, FavoriteWidget.class);
        i.setAction(UPDATE_WIDGET);
        context.sendBroadcast(i);
    }

}
