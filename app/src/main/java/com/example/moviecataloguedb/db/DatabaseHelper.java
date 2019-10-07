package com.example.moviecataloguedb.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static String DATABASE_NAME = "dbmovieapp";

    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_FAVORITE_MOVIE = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            DatabaseContract.FavoriteMovieColumns.TABLE_FAVORITE_MOVIE,
            DatabaseContract.FavoriteMovieColumns._ID,
            DatabaseContract.FavoriteMovieColumns.TITLE,
            DatabaseContract.FavoriteMovieColumns.DATE,
            DatabaseContract.FavoriteMovieColumns.SCORE,
            DatabaseContract.FavoriteMovieColumns.OVERVIEW,
            DatabaseContract.FavoriteMovieColumns.PHOTO);

    private static final String SQL_CREATE_TABLE_FAVORITE_TV = String.format("CREATE TABLE %s" +
            " (%s INTEGER PRIMARY KEY," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL," +
            " %s TEXT NOT NULL)",
            DatabaseContract.FavoriteTvColumns.TABLE_FAVORITE_TV,
            DatabaseContract.FavoriteTvColumns._ID,
            DatabaseContract.FavoriteTvColumns.TITLE,
            DatabaseContract.FavoriteTvColumns.DATE,
            DatabaseContract.FavoriteTvColumns.SCORE,
            DatabaseContract.FavoriteTvColumns.OVERVIEW,
            DatabaseContract.FavoriteTvColumns.PHOTO);

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_FAVORITE_TV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FavoriteMovieColumns.TABLE_FAVORITE_MOVIE);
        onCreate(db);
        db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.FavoriteTvColumns.TABLE_FAVORITE_TV);
        onCreate(db);
    }
}
