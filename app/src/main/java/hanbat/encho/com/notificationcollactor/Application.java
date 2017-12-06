package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.multidex.MultiDexApplication;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.stetho.Stetho;
import com.google.android.gms.ads.MobileAds;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

public class Application extends MultiDexApplication {
    static Context mContext;

    public static Context getAppContext() {
        return mContext;
    }

    @BindingAdapter({"imageBitmap"})
    public static void loadImage(ImageView iv, Bitmap b) {
        if (b != null)
            iv.setImageBitmap(b);
        else
            iv.setVisibility(View.GONE);
    }

    @BindingAdapter({"loadSmallIcon", "loadPackageName", "loadColor"})
    public static void setSmallIcon(ImageView iv, int smallIcon, String packageName, int color) {
        try {
            PackageManager pm = mContext.getPackageManager();
            Drawable icon = pm.getResourcesForApplication(packageName).getDrawable(smallIcon);
            if (color == 0) {
                icon.setColorFilter(Color.parseColor("#5C7480"), PorterDuff.Mode.SRC_ATOP);
            } else if (color != -1){
                icon.setColorFilter(Color.parseColor(String.format("#%06X", (0xFFFFFF & color))), PorterDuff.Mode.SRC_ATOP);
            }
            iv.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @BindingAdapter({"colorString"})
    public static void setTextColor(TextView tv, int color) {
        tv.setTextColor(Color.parseColor(color != 0 && color != -1 ? String.format("#%06X", (0xFFFFFF & color)) : "#5C7480"));
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

    @Override
    public void onCreate() {
        super.onCreate();
        MobileAds.initialize(this, getString(R.string.admod_app_id));
        Stetho.initializeWithDefaults(this);
        mContext = this;
    }
}


