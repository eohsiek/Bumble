package com.example.android.bumble;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bumble.database.Favorite;

import java.util.List;

public class FavoriteListAdapter extends RecyclerView.Adapter<FavoriteListAdapter.FavoriteViewHolder> {

    class FavoriteViewHolder extends RecyclerView.ViewHolder {
        private final TextView favoriteItemView;

        private FavoriteViewHolder(View favoriteView) {
            super(favoriteView);
            favoriteItemView = favoriteView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Favorite> mFavorites; // Cached copy of words

    FavoriteListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public FavoriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_favorite, parent, false);
        return new FavoriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FavoriteViewHolder holder, int position) {
        if (mFavorites != null) {
            Favorite current = mFavorites.get(position);
            holder.favoriteItemView.setText(current.favoritePrompt);
        } else {
            // Covers the case of data not being ready yet.
            holder.favoriteItemView.setText("No Favorites Set Yet");
        }
    }

    void setFavorites(List<Favorite> favorites) {
        mFavorites = favorites;
        notifyDataSetChanged();
    }

    public Favorite getFavoriteAt(int position) {
        return mFavorites.get(position);
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mFavorites != null)
            return mFavorites.size();
        else return 0;
    }
}
