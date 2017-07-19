package hanbat.encho.com.notificationcollactor;

import android.app.Notification;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.widget.Toast;

/**
 * Created by USER on 2017-07-19.
 */

public class NotificationListener extends NotificationListenerService {

    public static NotificationListenerService mNotificationListenerService;

    @Override
    public void onCreate() {
        mNotificationListenerService = this;
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Notification mNotification = sbn.getNotification();
        Bundle extras = mNotification.extras;
        String title = extras.getString(Notification.EXTRA_TITLE);
        Bitmap largeIcon = (Bitmap) extras.getParcelable(Notification.EXTRA_LARGE_ICON);
        CharSequence text = extras.getCharSequence(Notification.EXTRA_TEXT);
        CharSequence subText = extras.getCharSequence(Notification.EXTRA_SUB_TEXT);
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Toast.makeText(this, sbn.getPackageName(), Toast.LENGTH_SHORT).show();
    }
}
