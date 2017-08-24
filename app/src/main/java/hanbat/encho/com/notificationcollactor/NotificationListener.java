package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by USER on 2017-07-19.
 */

public class NotificationListener extends NotificationListenerService {

    public NotificationListenerService mNotificationListenerService;
    private DBhelper db;
    private PackageManager pm;
    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        mNotificationListenerService = this;
        if (db == null)
            db = new DBhelper(Application.getAppContext(), "NC", null, 1);
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"ID :" + sbn.getId() + "\t" + sbn.getNotification().tickerText + "\t" + sbn.getPackageName());
        Notification mNotification = sbn.getNotification();
        pm = Application.getAppContext().getPackageManager();

        ArrayList<String> confirmedApps = PreferenceManager.getInstance().getStringArrayPref(Application.getAppContext(), "Packages");
        Bundle extras = mNotification.extras;

        if (confirmedApps.contains(sbn.getPackageName())) {
            ApplicationInfo app = null;

            for (String _key : extras.keySet()) {
                if (extras.get(_key) instanceof ApplicationInfo) {
                    app = (ApplicationInfo) extras.get(_key);
                    break;
                }
            }

            String title = extras.getString(Notification.EXTRA_TITLE) != null ? extras.getString(Notification.EXTRA_TITLE) : "";
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT) != null ? extras.getCharSequence(Notification.EXTRA_TEXT) : "";
            CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            Bitmap largeIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
            long postTime = sbn.getPostTime();
            String packageName = sbn.getPackageName();

            CharSequence appName = pm.getApplicationLabel(app);

            NotificationObject obj = new NotificationObject(title, largeIcon, text, subText, postTime, packageName, appName);

            if (text.length() > 0 && title.length() > 0)
                db.addNotification(obj);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {

        Notification noti = sbn.getNotification();
        Bundle bundle = noti.extras;
        for (String _key : bundle.keySet()) {
            Log.d("hanlog bundle check", "key=" + _key + " : " + bundle.get(_key));
        }
        Log.d("hanlog enter", "");
    }
}
