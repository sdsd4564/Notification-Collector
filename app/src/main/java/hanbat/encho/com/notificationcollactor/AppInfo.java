package hanbat.encho.com.notificationcollactor;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageItemInfo;
import android.databinding.BindingAdapter;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

/**
 * Created by USER on 2017-07-27.
 */

public class AppInfo extends ApplicationInfo {
    private Drawable icon;
    private String name;
    private String packageName;
    private boolean isSelected = false;

    @BindingAdapter({"iconImage"})
    public static void loadDrawableImage(ImageView iv, Drawable drawable) {
        iv.setImageDrawable(drawable);
    }

    public AppInfo(ApplicationInfo orig) {
        super(orig);
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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void onAppClicked(View view) {
        this.setSelected(!isSelected);
        Toast.makeText(Application.getAppContext(), name + " : " + isSelected, Toast.LENGTH_SHORT).show();
        view.setBackgroundColor(this.isSelected ? Color.CYAN : Color.WHITE);
    }
}
