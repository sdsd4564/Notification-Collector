package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by USER on 2017-07-19.
 */

public class Application extends android.app.Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getAppContext() {
        return mContext;
    }

    @BindingAdapter({"imageBitmap"})
    public static void loadImage(ImageView iv, Bitmap b) {
        iv.setImageBitmap(b);
    }

    @BindingAdapter({"loadIcon"})
    public static void resourceToDrawable(ImageView iv, String packageName) {
        File imgFile = new File(mContext.getFilesDir(), packageName);
        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            iv.setImageBitmap(bitmap);
        }
    }

    @BindingAdapter({"iconImage"})
    public static void loadDrawableImage(ImageView iv, Drawable drawable) {
        iv.setImageDrawable(drawable);
    }

    @BindingAdapter({"longToDate"})
    public static void longToDate(TextView tv, long time) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.KOREA);
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        long now = System.currentTimeMillis();

        CharSequence ago = DateUtils.getRelativeTimeSpanString(time, now, DateUtils.MINUTE_IN_MILLIS);
        tv.setText(ago);
    }

    public static ArrayList<NotificationGroup> getGroupNotifications(ArrayList<NotificationObject> items) {
        ArrayList<NotificationGroup> groups = new ArrayList<>();
        for (String app : PreferenceManager.getInstance().getStringArrayPref(mContext, "Packages")) {
            ArrayList<NotificationObject> separatedItems = new ArrayList<>();
            CharSequence s = null;
            for (NotificationObject object : items) {
                if (app.equals(object.getPackageName())) {
                    separatedItems.add(object);
                    s = object.getAppName();
                }
            }
            if (!separatedItems.isEmpty())
                groups.add(new NotificationGroup(String.valueOf(s), app, separatedItems));
        }

        return groups;
    }
}


