package hanbat.encho.com.notificationcollactor.Model;

import android.os.Parcel;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Encho on 2017-08-23.
 */

public class NotificationGroup extends ExpandableGroup<NotificationObject> {
    private String packageName;
    private String appName;

    public NotificationGroup(String title, String packageName, List<NotificationObject> items) {
        super(title, items);
        this.appName = title;
        this.packageName = packageName;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public int getCount() {
        return this.getItemCount();
    }
}
