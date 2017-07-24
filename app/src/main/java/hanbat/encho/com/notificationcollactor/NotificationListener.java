package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by USER on 2017-07-19.
 */

public class NotificationListener extends NotificationListenerService {

    public static NotificationListenerService mNotificationListenerService;
    private DBhelper db;

    @Override
    public void onCreate() {
        mNotificationListenerService = this;
        if (db == null)
            db = new DBhelper(Application.getAppContext(), "NC", null, 1);
        Toast.makeText(Application.getAppContext(), "리스너 생성 완료!", Toast.LENGTH_SHORT).show();
    }



    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification = sbn.getNotification();
        Bundle extras = mNotification.extras;

        String title = extras.getString(Notification.EXTRA_TITLE);
        int smallIcon = extras.getInt(Notification.EXTRA_SMALL_ICON);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
        Bitmap largeIcon = (Bitmap) extras.get(Notification.EXTRA_LARGE_ICON);
        Toast.makeText(mNotificationListenerService, extras.get(Notification.EXTRA_TITLE) + "사진 존재 !", Toast.LENGTH_SHORT).show();
        long postTime = sbn.getPostTime();
        String packageName = sbn.getPackageName();

        NotificationObject obj = new NotificationObject(title, smallIcon, largeIcon, text, subText, postTime, packageName);
        db.addNotification(obj);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Toast.makeText(this, sbn.getPackageName(), Toast.LENGTH_SHORT).show();
    }
}
