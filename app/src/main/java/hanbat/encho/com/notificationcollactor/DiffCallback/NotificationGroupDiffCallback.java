package hanbat.encho.com.notificationcollactor.DiffCallback;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;

/**
 * Created by USER on 2017-08-25.
 */

public class NotificationGroupDiffCallback extends DiffUtil.Callback {
    private ArrayList<NotificationGroup> mOldGroup;
    private ArrayList<NotificationGroup> mNewGroup;


    public NotificationGroupDiffCallback(ArrayList<NotificationGroup> mOldGroup, ArrayList<NotificationGroup> mNewGroup) {
        this.mOldGroup = mOldGroup;
        this.mNewGroup = mNewGroup;
    }

    @Override
    public int getOldListSize() {
        return mOldGroup == null ? 0 : mOldGroup.size();
    }

    @Override
    public int getNewListSize() {
        return mNewGroup == null ? 0 : mNewGroup.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
//        return mOldGroup.getItems().get(oldItemPosition).getPackageName().equals(mNewGroup.getItems().get(newItemPosition).getPackageName());
        return mOldGroup.get(oldItemPosition).getPackageName().equals(mNewGroup.get(newItemPosition).getPackageName());
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        final NotificationGroup mOld = mOldGroup.get(oldItemPosition);
        final NotificationGroup mNew = mNewGroup.get(newItemPosition);

        return mNew.getItems() == mOld.getItems()
                && mNew.getAppName().equals(mOld.getAppName())
                && mNew.getItemCount() == mOld.getItemCount();
    }
}
