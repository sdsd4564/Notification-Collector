package hanbat.encho.com.notificationcollactor.DiffCallback;

import android.support.v7.util.DiffUtil;

import java.util.List;

import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by Encho on 2017-08-16.
 */

public class NotificationObjectDiffCallback extends DiffUtil.Callback {
    private List<NotificationObject> mOldNotificationObjects;
    private List<NotificationObject> mNewNotificationObjects;

    public NotificationObjectDiffCallback(List<NotificationObject> mOldNotificationObjects, List<NotificationObject> mNewNotificationObjects) {
        this.mOldNotificationObjects = mOldNotificationObjects;
        this.mNewNotificationObjects = mNewNotificationObjects;
    }

    @Override
    public int getOldListSize() {
        return mOldNotificationObjects.size();
    }

    @Override
    public int getNewListSize() {
        return mNewNotificationObjects.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldNotificationObjects.get(oldItemPosition).getPackageName().equals(mNewNotificationObjects.get(newItemPosition).getPackageName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final NotificationObject mOldNotificationObject = mOldNotificationObjects.get(oldItemPosition);
        final NotificationObject mNewNotificationObject = mNewNotificationObjects.get(newItemPosition);

        return mOldNotificationObject.getText().equals(mNewNotificationObject.getText())
                && mOldNotificationObject.getPostTime() == mNewNotificationObject.getPostTime()
                && mOldNotificationObject.getTitle().equals(mNewNotificationObject.getTitle())
                && mOldNotificationObject.getText().equals(mNewNotificationObject.getText());
    }
}
