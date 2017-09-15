package hanbat.encho.com.notificationcollactor.DiffCallback;

import android.support.v7.util.DiffUtil;
import android.text.TextUtils;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationObject;

/**
 * Created by Encho on 2017-09-16.
 */

public class NotificationObjectDiffCallback extends DiffUtil.Callback {
    private ArrayList<NotificationObject> mOldList;
    private ArrayList<NotificationObject> mNewList;

    public NotificationObjectDiffCallback(ArrayList<NotificationObject> mOldList, ArrayList<NotificationObject> mNewList) {
        this.mOldList = mOldList;
        this.mNewList = mNewList;
    }

    @Override
    public int getOldListSize() {
        return mOldList.size();
    }

    @Override
    public int getNewListSize() {
        return mNewList.size();
    }

    @Override
    public boolean areItemsTheSame(int i, int i1) {
        return mOldList.get(i).getPackageName().equals(mNewList.get(i1).getPackageName());
    }

    @Override
    public boolean areContentsTheSame(int i, int i1) {
        final NotificationObject mOld = mOldList.get(i);
        final NotificationObject mNew = mNewList.get(i1);

        return mOld.getAppName().equals(mNew.getAppName())
                && TextUtils.equals(mOld.getText(), mNew.getText())
                && mOld.getPostTime() == mNew.getPostTime()
                && mOld.getTitle().equals(mNew.getTitle());
    }
}
