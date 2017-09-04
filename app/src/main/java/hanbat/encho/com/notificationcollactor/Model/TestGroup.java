package hanbat.encho.com.notificationcollactor.Model;

import java.util.ArrayList;

/**
 * Created by Encho on 2017-09-04.
 */

public class TestGroup {
    private String appName;
    private String packageName;
    private ArrayList<NotificationObject> list;

    public TestGroup(String appName, String packageName, ArrayList<NotificationObject> list) {
        this.appName = appName;
        this.packageName = packageName;
        this.list = list;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public ArrayList<NotificationObject> getList() {
        return list;
    }

    public int getItemCount() {
        return list.size();
    }
}
