package hanbat.encho.com.notificationcollactor.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Encho on 2017-09-15.
 */

public class TestGroup {
    private String packageName;
    private String appName;
    private ArrayList<NotificationObject> items = new ArrayList<>();
    private boolean isExpand = false;

    public TestGroup(String title, String packageName, List<NotificationObject> items) {
        this.appName = title;
        this.packageName = packageName;
        this.items.addAll(items);
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
        return items.size();
    }
}
