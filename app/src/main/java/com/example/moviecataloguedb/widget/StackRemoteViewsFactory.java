package com.example.moviecataloguedb.widget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.example.moviecataloguedb.R;
import com.example.moviecataloguedb.db.MovieFavoriteHelper;
import com.example.moviecataloguedb.db.TvFavoriteHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.moviecataloguedb.widget.FavoriteWidget.EXTRA_ITEM;

public class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private final List<Bitmap> mWidgetItems = new ArrayList<>();
    private final Context mContext;
    private MovieFavoriteHelper movieFavoriteHelper;
    private TvFavoriteHelper tvFavoriteHelper;

    StackRemoteViewsFactory(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void onCreate() {
        movieFavoriteHelper = MovieFavoriteHelper.getInstance(mContext.getApplicationContext());
        tvFavoriteHelper = TvFavoriteHelper.getInstance(mContext.getApplicationContext());
    }

    @Override
    public void onDataSetChanged() {
        mWidgetItems.clear();
        String imageUrl = "https://image.tmdb.org/t/p/w780/";
        final long identityToken = Binder.clearCallingIdentity();

        Cursor cursorMovie = movieFavoriteHelper.selectMovieFavorite();
        if(cursorMovie.moveToFirst()){
            do{
                try {
                    mWidgetItems.add(Glide.with(mContext)
                            .asBitmap()
                            .load(imageUrl+ cursorMovie.getString(cursorMovie.getColumnIndex("photo")))
                            .submit()
                            .get());
                } catch (InterruptedException | ExecutionException e){
                    Log.e("WidgetLoadError", "errorw: " + e.toString());
                }
            } while (cursorMovie.moveToNext());
        }

        Cursor cursorTv = tvFavoriteHelper.selectTvFavorite();
        if (cursorTv.moveToFirst()){
            do {
                try {
                    mWidgetItems.add(Glide.with(mContext)
                            .asBitmap()
                            .load(imageUrl+ cursorTv.getString(cursorTv.getColumnIndex("photo")))
                            .submit()
                            .get());
                } catch (InterruptedException | ExecutionException e){
                    Log.e("WidgetLoadError", "errorw: " + e.toString());
                }
            } while (cursorTv.moveToNext());
        }
        Binder.restoreCallingIdentity(identityToken);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems.get(position));
        Bundle extras = new Bundle();
        extras.putInt(EXTRA_ITEM, position);
        Intent fillIntent = new Intent();
        fillIntent.putExtras(extras);
        rv.setOnClickFillInIntent(R.id.imageView, fillIntent);
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
