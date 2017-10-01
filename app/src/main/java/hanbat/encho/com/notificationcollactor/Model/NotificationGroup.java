package hanbat.encho.com.notificationcollactor.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Encho on 2017-09-15.
 */

public class NotificationGroup {
    private String packageName;
    private String appName;
    private ArrayList<NotificationObject> items = new ArrayList<>();
    private boolean isExpand = false;
    private int count;

    public NotificationGroup(String title, String packageName, List<NotificationObject> items, int count) {
        this.appName = title;
        this.packageName = packageName;
        this.items.addAll(items);
        this.count = count;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getAppName() {
        return appName;
    }

    public ArrayList<NotificationObject> getItems() {
        return items;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
