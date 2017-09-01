package hanbat.encho.com.notificationcollactor.DiffCallback;

import android.support.v7.util.DiffUtil;

import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by USER on 2017-09-01.
 */

public class NotificationGroupItemDiffCallback extends DiffUtil.Callback {
    private final NotificationGroup mOldGroup;
    private final NotificationGroup mNewGroup;

    public NotificationGroupItemDiffCallback(NotificationGroup mOldGroup, NotificationGroup mNewGroup) {
        this.mOldGroup = mOldGroup;
        this.mNewGroup = mNewGroup;
    }

    @Override
    public int getOldListSize() {
        return mOldGroup.getItemCount();
    }

    @Override
    public int getNewListSize() {
        return mNewGroup.getItemCount();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldGroup.getItems().get(oldItemPosition).getPackageName().equals(mNewGroup.getItems().get(newItemPosition).getPackageName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final NotificationObject oldObject = mOldGroup.getItems().get(oldItemPosition);
        final NotificationObject newObject = mNewGroup.getItems().get(newItemPosition);

        return oldObject.getAppName() == newObject.getAppName()
                && oldObject.getText().equals(newObject.getText())
                && oldObject.getTitle().equals(newObject.getTitle())
                && oldObject.getPostTime() == newObject.getPostTime();
    }
}
