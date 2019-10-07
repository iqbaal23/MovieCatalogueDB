package com.example.moviecataloguedb.helper;

import android.database.Cursor;

import com.example.moviecataloguedb.db.DatabaseContract;
import com.example.moviecataloguedb.parcelable.MovieItems;
import com.example.moviecataloguedb.parcelable.TvItems;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

public class MappingHelper {
    public static ArrayList<MovieItems> mapMovieCursorToArrayList(Cursor movieCursor){
        ArrayList<MovieItems> movieItems = new ArrayList<>();
        while (movieCursor.moveToNext()){
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(_ID));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.TITLE));
            String date = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.DATE));
            String score = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.SCORE));
            String overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.OVERVIEW));
            String photo = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteMovieColumns.PHOTO));
            movieItems.add(new MovieItems(id, title, date, score, overview, photo));
        }
        return movieItems;
    }

    public static ArrayList<TvItems> mapTvCursorToArrayList(Cursor movieCursor){
        ArrayList<TvItems> tvItems = new ArrayList<>();
        while (movieCursor.moveToNext()){
            int id = movieCursor.getInt(movieCursor.getColumnIndexOrThrow(_ID));
            String title = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteTvColumns.TITLE));
            String date = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteTvColumns.DATE));
            String score = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteTvColumns.SCORE));
            String overview = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteTvColumns.OVERVIEW));
            String photo = movieCursor.getString(movieCursor.getColumnIndexOrThrow(DatabaseContract.FavoriteTvColumns.PHOTO));
            tvItems.add(new TvItems(id, title, date, score, overview, photo));
        }
        return tvItems;
    }
}
