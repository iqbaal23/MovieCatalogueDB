package com.example.moviecataloguedb.ui.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Cursor;

import com.example.moviecataloguedb.parcelable.MovieItems;

import java.util.ArrayList;

public class MovieFavoriteViewModel extends ViewModel {

    private MutableLiveData<ArrayList<MovieItems>> listMovie = new MutableLiveData<>();

    void setMovie(Cursor cursor){
        final ArrayList<MovieItems> listItems = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            MovieItems movieItems = new MovieItems();
            movieItems.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            movieItems.setName(cursor.getString(cursor.getColumnIndex("title")));
            movieItems.setDate(cursor.getString(cursor.getColumnIndex("date")));
            movieItems.setScore(cursor.getString(cursor.getColumnIndex("score")));
            movieItems.setOverview(cursor.getString(cursor.getColumnIndex("overview")));
            movieItems.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
            listItems.add(movieItems);
        }
        listMovie.postValue(listItems);

    }

    LiveData<ArrayList<MovieItems>> getMovies(){
        return listMovie;
    }
}