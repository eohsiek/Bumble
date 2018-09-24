package com.example.android.bumble.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "favoritePrompts")

public class Favorite {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    public int favoriteId;

    @NonNull
    public String favoritePrompt;
}
