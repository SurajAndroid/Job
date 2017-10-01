package utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.startupsoch.jobpool.AppController;

public class SavedData {
    private static final String SELECT_PACKAGE = "selectPackage";



    static SharedPreferences prefs;

    public static SharedPreferences getInstance() {
        if (prefs == null) {
            prefs = PreferenceManager.getDefaultSharedPreferences(AppController.getInstance());
        }
        return prefs;
    }



    public static String getPack() {
        return getInstance().getString(SELECT_PACKAGE, null);
    }

    public static void savePack(String selectPackage) {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.putString(SELECT_PACKAGE, selectPackage);
        editor.apply();
    }

    public static void clear() {
        SharedPreferences.Editor editor = getInstance().edit();
        editor.clear();
        editor.apply();
    }
}