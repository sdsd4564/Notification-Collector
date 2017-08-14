package hanbat.encho.com.notificationcollactor.Model;

import android.content.pm.ApplicationInfo;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

public class AppInfo extends ApplicationInfo {
    private Drawable icon;
    private String name;
    private String packageName;

    @BindingAdapter({"iconImage"})
    public static void loadDrawableImage(ImageView iv, Drawable drawable) {
        iv.setImageDrawable(drawable);
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
