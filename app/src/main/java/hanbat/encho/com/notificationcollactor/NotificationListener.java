package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

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
            ApplicationInfo app = null;

            for (String _key : extras.keySet()) {
                if (extras.get(_key) instanceof ApplicationInfo) {
                    app = (ApplicationInfo) extras.get(_key);
                    break;
                }
            }

            String title = extras.getString(Notification.EXTRA_TITLE) != null ? extras.getString(Notification.EXTRA_TITLE) : "";
            int smallIcon = extras.getInt(Notification.EXTRA_SMALL_ICON);
            CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT) != null ? extras.getCharSequence(Notification.EXTRA_TEXT) : "";
            CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
            Bitmap largeIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
            long postTime = sbn.getPostTime();
            String packageName = sbn.getPackageName();

            CharSequence appName = pm.getApplicationLabel(app);
            Drawable d = pm.getApplicationIcon(app);
            saveBitmapToFile(convertDrawableToBitmap(d), packageName);

            NotificationObject obj = new NotificationObject(title, smallIcon, largeIcon, text, subText, postTime, packageName, appName);

            if (text != "" && !title.equals(""))
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

    private Bitmap convertDrawableToBitmap(Drawable d) {
        if (d instanceof BitmapDrawable)
            return ((BitmapDrawable) d).getBitmap();

        Bitmap b = Bitmap.createBitmap(d.getIntrinsicWidth(), d.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(b);
        d.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        d.draw(canvas);

        return b;
    }

    private void saveBitmapToFile(Bitmap bm, String filePath) {
        File f = new File(getExternalCacheDir(), "IconImage");
        if (!f.mkdirs())
            Log.d("hanlog mkdir check", "FAILURE");

        File iconImage = new File(f, filePath + ".png");
        if (!iconImage.exists()) {
            OutputStream stream;
            try {
                f.createNewFile();
                stream = new FileOutputStream(f);

                bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
