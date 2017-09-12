package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
import java.util.TimeZone;

import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

public class Application extends android.app.Application {
    private static Context mContext;

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

            int redBucket = 0;
            int greenBucket = 0;
            int blueBucket = 0;
            int pixelCount = 0;

            for (int y = 0; y < bitmap.getHeight(); ++y) {
                for (int x = 0; x < bitmap.getWidth(); ++x) {
                    int c = bitmap.getPixel(x, y);

                    pixelCount++;
                    redBucket += Color.red(c);
                    greenBucket += Color.green(c);
                    blueBucket += Color.blue(c);
                }
            }
            int averageColor = Color.rgb(redBucket / pixelCount, greenBucket / pixelCount, blueBucket / pixelCount);
            iv.setImageBitmap(bitmap);
            iv.setBackgroundColor(averageColor);
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
            String[] row = app.split(",");
            ArrayList<NotificationObject> separatedItems = new ArrayList<>();
            for (NotificationObject object : items) {
                if (row[0].equals(object.getPackageName())) {
                    separatedItems.add(object);
                }
            }
            groups.add(new NotificationGroup(row[1], row[0], separatedItems));
        }

        return groups;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initializeWithDefaults(this);
        mContext = this;
    }
}


