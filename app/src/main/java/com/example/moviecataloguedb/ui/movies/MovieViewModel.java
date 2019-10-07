package com.example.moviecataloguedb.ui.movies;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.example.moviecataloguedb.parcelable.MovieItems;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MovieViewModel extends ViewModel {

    private static final String API_KEY = "e44532ef29107e2417a69fd973886f0c";
    private MutableLiveData<ArrayList<MovieItems>> listMovie = new MutableLiveData<>();

    void setMovie(String type, String query){
        AsyncHttpClient client = new AsyncHttpClient();
        final ArrayList<MovieItems> listItems = new ArrayList<>();
        String url = "";
        if (type == "cari"){
            url = "https://api.themoviedb.org/3/search/movie?api_key=" + API_KEY + "&language=en-US&query=" + query;
        } else if(type == "default"){
            url = "https://api.themoviedb.org/3/discover/movie?api_key=" + API_KEY+ "&language=en-US";
        }

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String result = new String(responseBody);
                    JSONObject responseObject = new JSONObject(result);
                    JSONArray list = responseObject.getJSONArray("results");
                    for (int i = 0; i < list.length(); i++){
                        JSONObject movie = list.getJSONObject(i);
                        MovieItems movieItems = new MovieItems(movie);
                        listItems.add(movieItems);
                    }
                    listMovie.postValue(listItems);
                } catch (Exception e){
                    Log.d("Exception", e.getMessage());
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("onFailure", error.getMessage());
            }
        });
    }

    LiveData<ArrayList<MovieItems>> getMovies(){
        return listMovie;
    }
}