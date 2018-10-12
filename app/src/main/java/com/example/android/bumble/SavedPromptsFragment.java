package com.example.android.bumble;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ClipData;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bumble.database.Favorite;
import com.example.android.bumble.database.FavoriteViewModel;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SavedPromptsFragment extends Fragment {

    private FavoriteViewModel favoriteViewModel;
    private FavoriteListAdapter adapter;

    public SavedPromptsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_saved_prompts, container, false);

        favoriteViewModel = ViewModelProviders.of(this).get(FavoriteViewModel.class);

        adapter = new FavoriteListAdapter(getActivity());
        favoriteViewModel.getAllFavorites().observe(this, new Observer<List<Favorite>>() {
            @Override
            public void onChanged(@Nullable final List<Favorite> favorites) {
                // Update the cached copy of the words in the adapter.
                adapter.setFavorites(favorites);
            }
        });

        RecyclerView recyclerView = view.findViewById(R.id.recyclerview);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                favoriteViewModel.delete(adapter.getFavoriteAt(viewHolder.getAdapterPosition()));
                Toast.makeText(getActivity(), "Prompt Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        return view;
    }
}
