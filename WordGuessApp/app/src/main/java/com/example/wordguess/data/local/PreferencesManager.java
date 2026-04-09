package com.example.wordguess.data.local;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferencesManager {
    private final SharedPreferences prefs;

    public PreferencesManager(Context context) {
        prefs = context.getSharedPreferences("word_guess_prefs", Context.MODE_PRIVATE);
    }

    public void savePlayerName(String name) {
        prefs.edit().putString("player_name", name).apply();
    }

    public String getPlayerName() {
        return prefs.getString("player_name", null);
    }
}