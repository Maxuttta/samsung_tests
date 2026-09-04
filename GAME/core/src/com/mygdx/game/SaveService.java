package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public final class SaveService {
    private final Preferences preferences;
    public SaveService(String name) { preferences = Gdx.app.getPreferences(name); }
    public int unlockedLevel() { return preferences.getInteger("unlocked", 0); }
    public void unlockedLevel(int value) { preferences.putInteger("unlocked", value).flush(); }
    public boolean musicEnabled() { return preferences.getBoolean("music", true); }
    public void musicEnabled(boolean value) { preferences.putBoolean("music", value).flush(); }
    public boolean soundEnabled() { return preferences.getBoolean("sound", true); }
    public void soundEnabled(boolean value) { preferences.putBoolean("sound", value).flush(); }
    public boolean russian() { return preferences.getBoolean("russian", false); }
    public void russian(boolean value) { preferences.putBoolean("russian", value).flush(); }
}
