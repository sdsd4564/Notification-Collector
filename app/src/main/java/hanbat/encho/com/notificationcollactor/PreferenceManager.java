package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Enter on 2017-08-02.
 */

public class PreferenceManager {

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
