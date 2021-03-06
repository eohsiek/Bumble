package com.keishostudios.android.bumble.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

public class FavoriteViewModel extends AndroidViewModel {
    private FavoriteRepository repository;

    private LiveData<List<Favorite>> allFavorites;

    public FavoriteViewModel (Application application) {
        super(application);
        repository = new FavoriteRepository(application);
        allFavorites = repository.getAllFavorites();
    }

    public LiveData<List<Favorite>> getAllFavorites() { return allFavorites; }

    public void insert(Favorite favorite) { repository.insert(favorite); }

    public void delete(Favorite favorite) { repository.delete(favorite); }
}
