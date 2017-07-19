package hanbat.encho.com.notificationcollactor;

import android.graphics.Bitmap;

/**
 * Created by Encho on 2017-07-19.
 */

public class NotificationObject {
    private String title;
    private int smallIcon;
    private CharSequence text;
    private CharSequence subText;

    public NotificationObject(String title, int smallIcon, Bitmap largeIcon, CharSequence text, CharSequence subText) {
        this.title = title;
        this.smallIcon = smallIcon;
        this.text = text;
        this.subText = subText;
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
}
