package com.example.jutai;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LocaleHelper {

    private static final String SELECTED_LANGUAGE = "Locale.Language";

    // Save the selected language in SharedPreferences
    public static void setLocale(Context context, String languageCode) {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(SELECTED_LANGUAGE, languageCode);
        editor.apply();

        // Update the app's locale
        updateResources(context, languageCode);
    }

    public static void loadLocale(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String language = preferences.getString(SELECTED_LANGUAGE, "en"); // Default to English
        updateResources(context, language);
    }

    public static Context updateResources(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = context.getResources().getConfiguration();
        config.setLocale(locale);
        return context.createConfigurationContext(config);
    }

}
