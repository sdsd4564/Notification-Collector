package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by USER on 2017-07-19.
 */

public class NotificationListener extends NotificationListenerService {

    public NotificationListenerService mNotificationListenerService;
    private DBhelper db;
    private PackageManager pm;

    @Override
    public void onCreate() {
        mNotificationListenerService = this;
        if (db == null)
            db = new DBhelper(Application.getAppContext(), "NC", null, 1);
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification = sbn.getNotification();
        pm = Application.getAppContext().getPackageManager();

        ArrayList<String> confirmedApps = PreferenceManager.getInstance().getStringArrayPref(Application.getAppContext(), "Packages");
        Bundle extras = mNotification.extras;
        if (confirmedApps.contains(sbn.getPackageName())) {
            String title = extras.getString(Notification.EXTRA_TITLE) != null ? extras.getString(Notification.EXTRA_TITLE) : "";
            int smallIcon = extras.getInt(Notification.EXTRA_SMALL_ICON);
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT) != null ? extras.getCharSequence(Notification.EXTRA_TEXT) : "";
            CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            Bitmap largeIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
            long postTime = sbn.getPostTime();
            String packageName = sbn.getPackageName();
            ApplicationInfo app = (ApplicationInfo) extras.get("android.appInfo");
            CharSequence appName = app != null ? app.loadLabel(pm) : null;

            NotificationObject obj = new NotificationObject(title, smallIcon, largeIcon, text, subText, postTime, packageName, appName);
            if (text != "" && !title.equals(""))
                db.addNotification(obj);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
