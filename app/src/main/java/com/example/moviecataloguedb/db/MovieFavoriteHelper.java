package com.example.moviecataloguedb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moviecataloguedb.parcelable.MovieItems;

import static android.provider.BaseColumns._ID;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteMovieColumns.DATE;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteMovieColumns.OVERVIEW;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteMovieColumns.PHOTO;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteMovieColumns.SCORE;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteMovieColumns.TITLE;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteMovieColumns.TABLE_FAVORITE_MOVIE;

public class MovieFavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_FAVORITE_MOVIE;
    private static DatabaseHelper databaseHelper;
    private static MovieFavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    public MovieFavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static MovieFavoriteHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new MovieFavoriteHelper(context);
                }
            }
        }
        return INSTANCE;
    }

    public void open() throws SQLException{
        database = databaseHelper.getWritableDatabase();
    }

    public void close(){
        databaseHelper.close();

        if (database.isOpen()){
            database.close();
        }
    }

    public long insertMovieFavorite(MovieItems movieItems){
        ContentValues args = new ContentValues();
        args.put(_ID, movieItems.getId()+"");
        args.put(TITLE, movieItems.getName());
        args.put(DATE, movieItems.getDate());
        args.put(SCORE, movieItems.getScore());
        args.put(OVERVIEW, movieItems.getOverview());
        args.put(PHOTO, movieItems.getPhoto());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public Cursor checkMovieFavorite(String id){
        String[] movie = { _ID };
        String[] selection = { id };
        return database.query(DATABASE_TABLE, movie, "_id = "+id, null, null, null, null);
    }

    public int deleteMovieFavorite(int id){
        return database.delete(TABLE_FAVORITE_MOVIE, "_id = '" + id + "'", null);
    }

    public Cursor selectMovieFavoriteById(String id){
        return database.query(DATABASE_TABLE, null, _ID + " = ?", new String[]{id}, null, null, null, null);
    }

    public Cursor selectMovieFavorite(){
        return database.query(DATABASE_TABLE, null, null, null, null, null, _ID + " ASC");
    }

    public long insertMovieFavoriteProvider(ContentValues values){
        return  database.insert(DATABASE_TABLE, null, values);
    }

    public int updateMovieFavoriteProvider(String id, ContentValues values){
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteMovieFavoriteProvider(String id){
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }

}
