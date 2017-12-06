package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class PreferenceManager {

    private static PreferenceManager manager;

    public static PreferenceManager getInstance() {
        if (manager == null)
            manager = new PreferenceManager();

        return manager;
    }

    public void setIntegerPref(Context context, String key, int value) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt(key, value);
        editor.apply();
    }

    public int getIntegerPref(Context context, String key) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);

        return prefs.getInt(key, 61);
    }

    public void setStringArrayPref(Context context, String key, ArrayList<String> values) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        JSONArray jsonArray = new JSONArray();

        for (int i = 0; i < values.size(); ++i)
            jsonArray.put(values.get(i));

        editor.putString(key, values.isEmpty() ? null : jsonArray.toString());
        editor.apply();
    }

    public ArrayList<String> getStringArrayPref(Context context, String key) {
        SharedPreferences prefs = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String json = prefs.getString(key, null);
        ArrayList<String> items = new ArrayList<>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); ++i)
                    items.add(a.optString(i));

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return items;
    }
}
