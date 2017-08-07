package hanbat.encho.com.notificationcollactor;

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
    private boolean isSelected = false;

    @BindingAdapter({"iconImage"})
    public static void loadDrawableImage(ImageView iv, Drawable drawable) {
        iv.setImageDrawable(drawable);
    }

    @BindingAdapter({"setBackground"})
    public static void setBackground(View view, boolean checked) {
        view.setBackgroundColor(checked ? Color.MAGENTA : Color.WHITE);
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
        Log.d("hanlog package check", packageName);
        view.setBackgroundColor(this.isSelected ? Color.MAGENTA : Color.WHITE);
    }
}
