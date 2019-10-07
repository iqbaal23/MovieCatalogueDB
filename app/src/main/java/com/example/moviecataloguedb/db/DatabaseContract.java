package com.example.moviecataloguedb.db;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {
    public static final String AUTHORITY = "com.example.moviecataloguedb";
    private static final String SCHEME = "content";

    private DatabaseContract() {
    }

    public static final class FavoriteMovieColumns implements BaseColumns{
        public static String TABLE_FAVORITE_MOVIE = "favorite_movie";
        public static String TITLE = "title";
        public static String DATE = "date";
        public static String SCORE = "score";
        public static String OVERVIEW = "overview";
        public static String PHOTO = "photo";

        public static final Uri MOVIE_CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE_MOVIE)
                .build();
    }

    public static final class FavoriteTvColumns implements BaseColumns{
        public static String TABLE_FAVORITE_TV = "favorite_tv";
        public static String TITLE = "title";
        public static String DATE = "date";
        public static String SCORE = "score";
        public static String OVERVIEW = "overview";
        public static String PHOTO = "photo";

        public static final Uri TV_CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_FAVORITE_TV)
                .build();
    }

    public static String getColumnString(Cursor cursor, String columnName) {
        return cursor.getString(cursor.getColumnIndex(columnName));
    }

    public static int getColumnInt(Cursor cursor, String columnName) {
        return cursor.getInt(cursor.getColumnIndex(columnName));
    }

    public static long getColumnLong(Cursor cursor, String columnName) {
        return cursor.getLong(cursor.getColumnIndex(columnName));
    }

}
