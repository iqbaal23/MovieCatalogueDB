package com.example.moviecataloguedb;

import android.database.Cursor;

public interface LoadCallback {
    void preExecute();
    void postExecute(Cursor cursor);
}
