package com.example.moviecataloguedb.ui.tvShows;

import android.app.SearchManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.moviecataloguedb.R;
import com.example.moviecataloguedb.activity.SettingsActivity;
import com.example.moviecataloguedb.adapter.TvAdapter;
import com.example.moviecataloguedb.parcelable.TvItems;

import java.util.ArrayList;

public class TvShowFragment extends Fragment {
    private TvAdapter adapter;
    private ProgressBar progressBar;
    TvShowViewModel tvViewModel;

    public TvShowFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_tv_shows, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        progressBar = view.findViewById(R.id.progressBar);

        tvViewModel = ViewModelProviders.of(this).get(TvShowViewModel.class);
        tvViewModel.getTv().observe(this, getTv);

        adapter = new TvAdapter();
        adapter.notifyDataSetChanged();

        RecyclerView rvMovies = view.findViewById(R.id.rv_list);
        rvMovies.setLayoutManager(new LinearLayoutManager(getContext()));
        rvMovies.setAdapter(adapter);

        tvViewModel.setMovie("default", "");

        showLoading(true);
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        inflater.inflate(R.menu.main_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null){
            SearchView searchView = (SearchView) (menu.findItem(R.id.search)).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            searchView.setQueryHint(getString(R.string.searchHint) + "...");
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    tvViewModel.setMovie("cari", query);
                    showLoading(true);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_setting){
            Intent intent = new Intent(getContext(), SettingsActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(item);
    }
}