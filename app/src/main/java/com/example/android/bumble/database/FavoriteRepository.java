package com.example.android.bumble.database;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class FavoriteRepository {
    private FavoriteDao favoriteDao;
    private LiveData<List<Favorite>> allFavorites;

    FavoriteRepository(Application application) {
        FavoriteDatabase db = FavoriteDatabase.getDatabase(application);
        favoriteDao = db.favoriteDao();
        allFavorites = favoriteDao.getAllFavorites();
    }

    LiveData<List<Favorite>> getAllFavorites() {
        return allFavorites;
    }


    public void insert (Favorite favorite) {
        new insertAsyncTask(favoriteDao).execute(favorite);
    }

    private static class insertAsyncTask extends AsyncTask<Favorite, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        insertAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Favorite... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public void delete (Favorite favorite) {
        new deleteAsyncTask(favoriteDao).execute(favorite);
    }

    private static class deleteAsyncTask extends AsyncTask<Favorite, Void, Void> {

        private FavoriteDao mAsyncTaskDao;

        deleteAsyncTask(FavoriteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Favorite... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
