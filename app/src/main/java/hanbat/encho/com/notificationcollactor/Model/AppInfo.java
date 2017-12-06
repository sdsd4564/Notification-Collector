package hanbat.encho.com.notificationcollactor.Model;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

public class AppInfo extends ApplicationInfo {
    private Drawable icon;
    private String name;
    private String packageName;

    public AppInfo(ApplicationInfo orig, Drawable icon) {
        super(orig);
        this.icon = icon;
    }

    public AppInfo(Drawable icon, String name, String packageName) {
        this.icon = icon;
        this.name = name;
        this.packageName = packageName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
