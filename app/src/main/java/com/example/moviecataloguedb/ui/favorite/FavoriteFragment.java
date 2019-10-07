package com.example.moviecataloguedb.ui.favorite;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.arch.lifecycle.ViewModelProviders;

import com.example.moviecataloguedb.R;
import com.example.moviecataloguedb.ui.favorite.tab.SectionsPagerAdapter;

public class FavoriteFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        MovieFavoriteViewModel notificationsViewModel = ViewModelProviders.of(this).get(MovieFavoriteViewModel.class);
        View root = inflater.inflate(R.layout.fragment_favorite, container, false);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this.getContext(), getFragmentManager());
        ViewPager viewPager = root.findViewById(R.id.view_pager);
        TabLayout tabs = root.findViewById(R.id.tabs);
        viewPager.setAdapter(sectionsPagerAdapter);
        tabs.setupWithViewPager(viewPager);

        return root;
    }

}