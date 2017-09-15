package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by USER on 2017-07-19.
 */

public class NotificationListener extends NotificationListenerService {

    public static NotificationListenerService mNotificationListenerService;
    private DBhelper db;
    private PackageManager pm;
    private String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationListenerService = this;
        db = DBhelper.getInstance();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification = sbn.getNotification();
        pm = Application.getAppContext().getPackageManager();

        ArrayList<String> confirmedApps = new ArrayList<>();
        for (String str : PreferenceManager.getInstance().getStringArrayPref(Application.getAppContext(), "Packages"))
            confirmedApps.add(str.split(",")[0]);

        Bundle extras = mNotification.extras;

        if (confirmedApps.contains(sbn.getPackageName())) {
            ApplicationInfo app = null;

            for (String _key : extras.keySet()) {
                if (extras.get(_key) instanceof ApplicationInfo) {
                    app = (ApplicationInfo) extras.get(_key);
                    break;
                }
            }

            CharSequence appName = pm.getApplicationLabel(app);
            String title = String.valueOf(extras.get(Notification.EXTRA_TITLE));
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT) != null ? extras.getCharSequence(Notification.EXTRA_TEXT) : "";
            CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            Bitmap largeIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
            long postTime = sbn.getPostTime();
            String packageName = sbn.getPackageName();

            NotificationObject obj = new NotificationObject(title, largeIcon, text, subText, postTime, packageName, appName);

            if (text.length() > 0 && title.length() > 0) {
                db.addNotification(obj);
            }
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
    }
}
