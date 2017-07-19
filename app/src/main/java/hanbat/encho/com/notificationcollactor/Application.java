package hanbat.encho.com.notificationcollactor;

import android.content.Context;

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
}
