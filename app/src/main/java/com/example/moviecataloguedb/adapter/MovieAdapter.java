package com.example.moviecataloguedb.adapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.example.moviecataloguedb.activity.MovieDetailActivity;
import com.example.moviecataloguedb.R;
import com.example.moviecataloguedb.parcelable.MovieItems;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private ArrayList<MovieItems> mData = new ArrayList<>();

    public ArrayList<MovieItems> getData() {
        return mData;
    }

    public void setData(ArrayList<MovieItems> items){
        this.mData.clear();
        this.mData.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_movie, viewGroup, false);
        return new MovieViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder movieViewHolder, int i) {
        movieViewHolder.bind(mData.get(i));
        final MovieItems movieItems = mData.get(i);

        movieViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(movieViewHolder.itemView.getContext(), MovieDetailActivity.class);
                MovieItems movie = new MovieItems();
                movie.setId(movieItems.getId());
                movie.setName(movieItems.getName());
                movie.setScore(movieItems.getScore());
                movie.setDate(movieItems.getDate());
                movie.setOverview(movieItems.getOverview());
                movie.setPhoto(movieItems.getPhoto());

                intent.putExtra(MovieDetailActivity.EXTRA_MOVIE, movie);
                movieViewHolder.itemView.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        TextView textViewJudul, textViewScore;
        ImageView imgPhoto;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewJudul = itemView.findViewById(R.id.tv_judul);
            textViewScore = itemView.findViewById(R.id.tv_score);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
        }

        void bind(MovieItems movieItems){
            String imageUrl = "https://image.tmdb.org/t/p/w780/";
            textViewJudul.setText(movieItems.getName());
            textViewScore.setText(String.format("%s: %s", itemView.getResources().getString(R.string.score), movieItems.getScore()));

            Glide.with(itemView.getContext())
                    .load(imageUrl + movieItems.getPhoto())
                    .transform(new RoundedCorners(45))
                    .into(imgPhoto);
        }
    }
}
