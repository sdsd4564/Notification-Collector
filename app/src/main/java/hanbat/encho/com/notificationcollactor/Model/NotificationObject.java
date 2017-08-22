package hanbat.encho.com.notificationcollactor.Model;

import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import hanbat.encho.com.notificationcollactor.Application;

/**
 * Created by Encho on 2017-07-19.
 */

public class NotificationObject {
    private String title;
    private int smallIcon;
    private Bitmap largeIcon;
    private CharSequence text;
    private CharSequence subText;
    private long postTime;
    private String packageName;
    private CharSequence appName;

    public NotificationObject() {
    }

    public NotificationObject(String title, int smallIcon, Bitmap largeIcon, CharSequence text, CharSequence subText, long postTime, String packageName, CharSequence appName) {
        this.title = title;
        this.smallIcon = smallIcon;
        this.largeIcon = largeIcon;
        this.text = text;
        this.subText = subText;
        this.postTime = postTime;
        this.packageName = packageName;
        this.appName = appName;
    }

    public Bitmap getLargeIcon() {
        return largeIcon;
    }

    public void setLargeIcon(Bitmap largeIcon) {
        this.largeIcon = largeIcon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public CharSequence getSubText() {
        return subText;
    }

    public void setSubText(CharSequence subText) {
        this.subText = subText;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public CharSequence getAppName() {
        return appName;
    }

    public void setAppName(CharSequence appName) {
        this.appName = appName;
    }

}
