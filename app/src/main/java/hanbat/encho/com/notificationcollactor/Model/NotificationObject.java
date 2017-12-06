package hanbat.encho.com.notificationcollactor.Model;

import android.databinding.Bindable;
import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.ByteArrayOutputStream;

import hanbat.encho.com.notificationcollactor.BR;

/**
 * Created by Encho on 2017-07-19.
 */

public class NotificationObject implements Parcelable {

    private String title;
    private int smallIcon;
    private int color = -1;
    private Bitmap largeIcon;
    private CharSequence text;
    private CharSequence subText;
    private long postTime;
    private String packageName;
    private CharSequence appName;

    public NotificationObject() {
    }


    public NotificationObject(String title, int smallIcon, int color, Bitmap largeIcon, CharSequence text, CharSequence subText, long postTime, String packageName, CharSequence appName) {
        this.title = title;
        this.smallIcon = smallIcon;
        this.color = color;
        this.largeIcon = largeIcon;
        this.text = text;
        this.subText = subText;
        this.postTime = postTime;
        this.packageName = packageName;
        this.appName = appName;
    }


    protected NotificationObject(Parcel in) {
        title = in.readString();
        smallIcon = in.readInt();
        color = in.readInt();
        largeIcon = in.readParcelable(Bitmap.class.getClassLoader());
        postTime = in.readLong();
        packageName = in.readString();
    }

    public static final Creator<NotificationObject> CREATOR = new Creator<NotificationObject>() {
        @Override
        public NotificationObject createFromParcel(Parcel in) {
            return new NotificationObject(in);
        }

        @Override
        public NotificationObject[] newArray(int size) {
            return new NotificationObject[size];
        }
    };

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
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

    public static Creator<NotificationObject> getCREATOR() {
        return CREATOR;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setTitle(String title) {
        this.title = title;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        largeIcon.compress(Bitmap.CompressFormat.PNG, 0, stream);
        byte[] largeIconArray = stream.toByteArray();
        parcel.writeString(title);
        parcel.writeInt(smallIcon);
        parcel.writeInt(color);
        parcel.writeByteArray(largeIconArray);
        parcel.writeString(text.toString());
        parcel.writeString(subText.toString());
        parcel.writeLong(postTime);
        parcel.writeString(packageName);
        parcel.writeString(appName.toString());
    }
}
