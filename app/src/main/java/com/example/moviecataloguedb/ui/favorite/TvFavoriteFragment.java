package com.example.moviecataloguedb.ui.favorite;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.moviecataloguedb.LoadCallback;
import com.example.moviecataloguedb.R;
import com.example.moviecataloguedb.adapter.TvAdapter;
import com.example.moviecataloguedb.db.TvFavoriteHelper;
import com.example.moviecataloguedb.parcelable.TvItems;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import static com.example.moviecataloguedb.db.DatabaseContract.FavoriteTvColumns.TV_CONTENT_URI;
import static com.example.moviecataloguedb.helper.MappingHelper.mapTvCursorToArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class TvFavoriteFragment extends Fragment implements LoadCallback {
    private RecyclerView rvTv;
    private TvAdapter adapter;
    private ProgressBar progressBar;
    private TextView tvEmpty;
    private static final String EXTRA_STATE = "extra_state";

    public TvFavoriteFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tv_favorite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.i("Action", "onViewCreated");

        tvEmpty = view.findViewById(R.id.tv_empty_message);
        progressBar = view.findViewById(R.id.progressBar);
        adapter = new TvAdapter();

        rvTv = view.findViewById(R.id.rv_list);
    }

    private Observer<ArrayList<TvItems>> getTv = new Observer<ArrayList<TvItems>>() {
        @Override
        public void onChanged(@Nullable ArrayList<TvItems> tvItems) {
            if (tvItems != null){
                adapter.setData(tvItems);
            }
            showLoading(false);
        }
    };

    private void showLoading(Boolean state) {
        if (state) {
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Action", "onResume");

        TvFavoriteViewModel tvFavoriteViewModel = ViewModelProviders.of(this).get(TvFavoriteViewModel.class);
        tvFavoriteViewModel.getTvSeries().observe(this, getTv);

        adapter.notifyDataSetChanged();
        rvTv.setLayoutManager(new LinearLayoutManager(getContext()));

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());
        DataObserver myObserver = new DataObserver(handler, getContext());
        getContext().getContentResolver().registerContentObserver(TV_CONTENT_URI, true, myObserver);

        rvTv.setAdapter(adapter);

        TvFavoriteHelper tvFavoriteHelper = TvFavoriteHelper.getInstance(getActivity().getApplicationContext());
        Cursor cursor = tvFavoriteHelper.selectTvFavorite();

        tvFavoriteViewModel.setTv(cursor);
        if (cursor.getCount() == 0){
            tvEmpty.setText(R.string.empty_message);
        }

        showLoading(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(EXTRA_STATE, adapter.getData());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser){
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            Log.i("Action", "Refresh");
        }
    }

    @Override
    public void preExecute() {
        if (getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    @Override
    public void postExecute(Cursor cursor) {
        progressBar.setVisibility(View.INVISIBLE);
        ArrayList<TvItems> tvItems = mapTvCursorToArrayList(cursor);
        if (tvItems.size() > 0){
            adapter.setData(tvItems);
        } else{
            adapter.setData(new ArrayList<TvItems>());
        }
    }

    private static class LoadTvAsyncTask extends AsyncTask<Void, Void, Cursor>{
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadCallback> weakCallback;

        private LoadTvAsyncTask(Context context, LoadCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            weakCallback.get().preExecute();
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Context context = weakContext.get();
            return context.getContentResolver().query(TV_CONTENT_URI, null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            weakCallback.get().postExecute(cursor);
        }
    }

    public static class DataObserver extends ContentObserver{
        final Context context;

        public DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new LoadTvAsyncTask(context, (LoadCallback) context).execute();
        }
    }
}
