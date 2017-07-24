package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by USER on 2017-07-19.
 */

public class Application extends android.app.Application{
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
}
