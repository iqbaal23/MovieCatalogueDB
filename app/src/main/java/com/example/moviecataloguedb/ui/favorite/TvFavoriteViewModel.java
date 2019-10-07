package com.example.moviecataloguedb.ui.favorite;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Cursor;

import com.example.moviecataloguedb.parcelable.TvItems;

import java.util.ArrayList;

public class TvFavoriteViewModel extends ViewModel {

    private MutableLiveData<ArrayList<TvItems>> listTv = new MutableLiveData<>();

    void setTv(Cursor cursor){
        final ArrayList<TvItems> listItems = new ArrayList<>();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()){
            TvItems tvItems = new TvItems();
            tvItems.setId(cursor.getInt(cursor.getColumnIndex("_id")));
            tvItems.setName(cursor.getString(cursor.getColumnIndex("title")));
            tvItems.setDate(cursor.getString(cursor.getColumnIndex("date")));
            tvItems.setScore(cursor.getString(cursor.getColumnIndex("score")));
            tvItems.setOverview(cursor.getString(cursor.getColumnIndex("overview")));
            tvItems.setPhoto(cursor.getString(cursor.getColumnIndex("photo")));
            listItems.add(tvItems);
        }
        listTv.postValue(listItems);

    }

    LiveData<ArrayList<TvItems>> getTvSeries(){
        return listTv;
    }
}