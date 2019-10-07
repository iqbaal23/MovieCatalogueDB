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
import com.example.moviecataloguedb.db.TvFavoriteHelper;
import com.example.moviecataloguedb.parcelable.TvItems;
import com.example.moviecataloguedb.widget.FavoriteWidget;

import static com.example.moviecataloguedb.widget.FavoriteWidget.UPDATE_WIDGET;

public class TvDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TV = "extra_tv";
    TextView tvName, tvScore, tvDate, tvOverview;
    ImageView imgPhoto;
    TvItems tvItems;
    private TvFavoriteHelper tvFavoriteHelper;

    boolean isFavorite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tv_detail);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        tvName = findViewById(R.id.tv_judul);
        tvScore = findViewById(R.id.tv_score);
        tvDate = findViewById(R.id.tv_date);
        tvOverview = findViewById(R.id.tv_overview);
        imgPhoto = findViewById(R.id.iv_poster);

        tvItems = getIntent().getParcelableExtra(EXTRA_TV);

        tvFavoriteHelper = TvFavoriteHelper.getInstance(getApplicationContext());
        Cursor cursor = tvFavoriteHelper.checkTvFavorite(tvItems.getId()+"");
        isFavorite = cursor.getCount() > 0;

        actionBar.setTitle(tvItems.getName());
        tvName.setText(tvItems.getName());
        tvScore.setText(String.format("%s: %s", getString(R.string.score), tvItems.getScore()));
        tvDate.setText(tvItems.getDate());
        tvOverview.setText(tvItems.getOverview());

        String imageUrl = "https://image.tmdb.org/t/p/w780/";
        Glide.with(this)
                .load(imageUrl + tvItems.getPhoto())
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
                long result = tvFavoriteHelper.deleteTvFavorite(tvItems.getId());
                if (result > 0){
                    Toast.makeText(getApplicationContext(), R.string.success_delete_message, Toast.LENGTH_SHORT).show();
                    sendUpdateFavoriteList(getApplicationContext());
                    finish();
                }
            } else{
                long result = tvFavoriteHelper.insertTvFavorite(tvItems);
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
