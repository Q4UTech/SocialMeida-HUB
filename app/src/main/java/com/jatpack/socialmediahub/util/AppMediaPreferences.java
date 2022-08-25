package com.jatpack.socialmediahub.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppMediaPreferences {
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;

    private static final String SELECTED_POSITION = "_selected_pos";
    private static final String IMAGE_FILE_REFRESH = "IMAGE_FILE_REFRESH";
    private Context context;

    public AppMediaPreferences(Context context) {
        setPreferences(PreferenceManager.getDefaultSharedPreferences(context));
        editor = getPreferences().edit();
        this.context = context;
    }

    private SharedPreferences getPreferences() {
        return preferences;
    }

    private void setPreferences(SharedPreferences preferences) {
        this.preferences = preferences;
    }


    public void setgallerycount(int pos) {
        editor.putInt(SELECTED_POSITION, pos);
        editor.commit();
    }

    public int getgallerycount() {
        return getPreferences().getInt(SELECTED_POSITION, 0);
    }

    public boolean getRefreshImages() {
        return getPreferences().getBoolean(IMAGE_FILE_REFRESH, false);
    }

    public void setRefreshImages(boolean isRefresh) {
        editor.putBoolean(IMAGE_FILE_REFRESH, isRefresh);
        editor.commit();
    }
}

