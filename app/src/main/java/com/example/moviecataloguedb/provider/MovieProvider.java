package com.example.moviecataloguedb.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.moviecataloguedb.db.MovieFavoriteHelper;
import com.example.moviecataloguedb.db.TvFavoriteHelper;
import com.example.moviecataloguedb.ui.favorite.MovieFavoriteFragment;
import com.example.moviecataloguedb.ui.favorite.TvFavoriteFragment;

import static com.example.moviecataloguedb.db.DatabaseContract.AUTHORITY;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteMovieColumns.MOVIE_CONTENT_URI;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteMovieColumns.TABLE_FAVORITE_MOVIE;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.TABLE_FAVORITE_TV;
import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.TV_CONTENT_URI;

public class MovieProvider extends ContentProvider {
    private static final int MOVIE = 1;
    private static final int MOVIE_ID = 2;
    private static final int TV = 3;
    private static final int TV_ID = 4;

    private static final UriMatcher sUriMathcer = new UriMatcher(UriMatcher.NO_MATCH);

    private MovieFavoriteHelper movieFavoriteHelper;
    private TvFavoriteHelper tvFavoriteHelper;

    static {
        sUriMathcer.addURI(AUTHORITY, TABLE_FAVORITE_MOVIE, MOVIE);
        sUriMathcer.addURI(AUTHORITY, TABLE_FAVORITE_MOVIE + "/#", MOVIE_ID);

        sUriMathcer.addURI(AUTHORITY, TABLE_FAVORITE_TV, TV);
        sUriMathcer.addURI(AUTHORITY, TABLE_FAVORITE_TV + "/#", TV_ID);
    }

    @Override
    public boolean onCreate() {
        movieFavoriteHelper = MovieFavoriteHelper.getInstance(getContext());
        tvFavoriteHelper = TvFavoriteHelper.getInstance(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        switch (sUriMathcer.match(uri)){
            case MOVIE:
                movieFavoriteHelper.open();
                cursor = movieFavoriteHelper.selectMovieFavorite();
                break;
            case MOVIE_ID:
                movieFavoriteHelper.open();
                cursor = movieFavoriteHelper.selectMovieFavoriteById(uri.getLastPathSegment());
                break;
            case TV:
                tvFavoriteHelper.open();
                cursor = tvFavoriteHelper.selectTvFavorite();
                break;
            case TV_ID:
                tvFavoriteHelper.open();
                cursor = tvFavoriteHelper.selectTvFavoriteById(uri.getLastPathSegment());
                break;
            default:
                cursor = null;
                break;

        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long added;
        Uri favoriteUri;
        switch (sUriMathcer.match(uri)){
            case MOVIE:
                movieFavoriteHelper.open();
                added = movieFavoriteHelper.insertMovieFavoriteProvider(values);
                getContext().getContentResolver().notifyChange(MOVIE_CONTENT_URI, new MovieFavoriteFragment.DataObserver(new Handler(), getContext()));
                favoriteUri = Uri.parse(MOVIE_CONTENT_URI + "/" + added);
                break;
            case TV:
                tvFavoriteHelper.open();
                added = tvFavoriteHelper.insertTvFavoriteProvider(values);
                favoriteUri = Uri.parse(TV_CONTENT_URI + "/" + added);
                getContext().getContentResolver().notifyChange(TV_CONTENT_URI, new TvFavoriteFragment.DataObserver(new Handler(), getContext()));
                break;
            default:
                throw new SQLException("failed to insert row into " + uri);
        }
        return favoriteUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int deleted = 0;
        switch (sUriMathcer.match(uri)){
            case MOVIE_ID:
                movieFavoriteHelper.open();
                deleted = movieFavoriteHelper.deleteMovieFavoriteProvider(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(MOVIE_CONTENT_URI,
                        new MovieFavoriteFragment.DataObserver(new Handler(), getContext()));
                break;
            case TV_ID:
                tvFavoriteHelper.open();
                deleted = tvFavoriteHelper.deleteTvFavoriteProvider(uri.getLastPathSegment());
                getContext().getContentResolver().notifyChange(TV_CONTENT_URI,
                        new TvFavoriteFragment.DataObserver(new Handler(), getContext()));
                break;
        }
        return deleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
