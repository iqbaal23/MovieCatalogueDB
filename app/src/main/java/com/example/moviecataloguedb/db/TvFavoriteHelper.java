package com.example.moviecataloguedb.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.moviecataloguedb.parcelable.TvItems;

import static android.provider.BaseColumns._ID;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.DATE;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.OVERVIEW;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.PHOTO;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.SCORE;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.TITLE;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.TABLE_FAVORITE_TV;

public class TvFavoriteHelper {
    private static final String DATABASE_TABLE = TABLE_FAVORITE_TV;
    private static DatabaseHelper databaseHelper;
    private static TvFavoriteHelper INSTANCE;

    private static SQLiteDatabase database;

    public TvFavoriteHelper(Context context) {
        databaseHelper = new DatabaseHelper(context);
    }

    public static TvFavoriteHelper getInstance(Context context){
        if (INSTANCE == null){
            synchronized (SQLiteOpenHelper.class){
                if (INSTANCE == null){
                    INSTANCE = new TvFavoriteHelper(context);
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

    public long insertTvFavorite(TvItems tvItems){
        ContentValues args = new ContentValues();
        args.put(_ID, tvItems.getId()+"");
        args.put(TITLE, tvItems.getName());
        args.put(DATE, tvItems.getDate());
        args.put(SCORE, tvItems.getScore());
        args.put(OVERVIEW, tvItems.getOverview());
        args.put(PHOTO, tvItems.getPhoto());
        return database.insert(DATABASE_TABLE, null, args);
    }

    public Cursor checkTvFavorite(String id){
        String[] tv = { _ID };
        String[] selection = { id };
        return database.query(DATABASE_TABLE, tv, "_id = "+id, null, null, null, null);
    }

    public int deleteTvFavorite(int id){
        return database.delete(TABLE_FAVORITE_TV, "_id = '" + id + "'", null);
    }

    public Cursor selectTvFavoriteById(String id){
        return database.query(DATABASE_TABLE, null, _ID + " = ?", new String[]{id}, null, null, null, null);
    }

    public Cursor selectTvFavorite(){
        return database.query(DATABASE_TABLE, null, null, null, null, null, null);
    }

    public long insertTvFavoriteProvider(ContentValues values){
        return  database.insert(DATABASE_TABLE, null, values);
    }

    public int updateTvFavoriteProvider(String id, ContentValues values){
        return database.update(DATABASE_TABLE, values, _ID + " = ?", new String[]{id});
    }

    public int deleteTvFavoriteProvider(String id){
        return database.delete(DATABASE_TABLE, _ID + " = ?", new String[]{id});
    }
}
