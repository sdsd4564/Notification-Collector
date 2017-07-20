package hanbat.encho.com.notificationcollactor;

/**
 * Created by Encho on 2017-07-19.
 */

public class NotificationObject {
    private String title;
    private int smallIcon;
    private CharSequence text;
    private CharSequence subText;
    private long postTime;
    private String packageName;

    public NotificationObject() {

    }

    public NotificationObject(String title, int smallIcon, CharSequence text, CharSequence subText, long postTime, String packageName) {
        this.title = title;
        this.smallIcon = smallIcon;
        this.text = text;
        this.subText = subText;
        this.postTime = postTime;
        this.packageName = packageName;
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
}
