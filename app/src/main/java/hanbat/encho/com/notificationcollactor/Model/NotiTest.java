package hanbat.encho.com.notificationcollactor.Model;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

/**
 * Created by Encho on 2017-08-23.
 */

public class NotiTest extends ExpandableGroup<NotificationObject> {
    private String packageName;
    private String appName;

    public NotiTest(String title, String packageName, List<NotificationObject> items) {
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
