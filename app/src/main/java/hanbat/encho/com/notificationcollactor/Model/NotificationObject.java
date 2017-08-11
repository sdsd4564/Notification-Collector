package hanbat.encho.com.notificationcollactor.Model;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by Encho on 2017-07-19.
 */

public class NotificationObject {
    private String title;
    private int smallIcon;
    private Bitmap largeIcon;
    private Drawable icon;
    private CharSequence text;
    private CharSequence subText;
    private long postTime;
    private String packageName;

    @BindingAdapter({"imageBitmap"})
    public static void loadImage(ImageView iv, Bitmap b) {
        iv.setImageBitmap(b);
    }

    public NotificationObject() {
    }

    public NotificationObject(String title, int smallIcon, Bitmap largeIcon, CharSequence text, CharSequence subText, long postTime, String packageName) {
        this.title = title;
        this.smallIcon = smallIcon;
        this.largeIcon = largeIcon;
        this.text = text;
        this.subText = subText;
        this.postTime = postTime;
        this.packageName = packageName;
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

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
