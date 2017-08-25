package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import junit.framework.Test;

import java.util.ArrayList;
import java.util.List;

import hanbat.encho.com.notificationcollactor.AppListDialog.Adapter.DialogListAdapter;
import hanbat.encho.com.notificationcollactor.DiffCallback.AppInfoDiffCallback;
import hanbat.encho.com.notificationcollactor.DiffCallback.NotificationGroupDiffCallback;
import hanbat.encho.com.notificationcollactor.Model.AppInfo;
import hanbat.encho.com.notificationcollactor.Model.NotiTest;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.databinding.NotificationItemBinding;
import hanbat.encho.com.notificationcollactor.databinding.NotificationParentBinding;


/**
 * Created by Encho on 2017-08-23.
 */

public class TestAdapter extends ExpandableRecyclerViewAdapter<TestAdapter.ParentViewHolder, TestAdapter.MainViewHolder> {
    private ArrayList<NotiTest> groups;

    public TestAdapter(ArrayList<NotiTest> groups) {
        super(groups);
        this.groups = groups;
    }

    @Override
    public ParentViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_parent, parent, false);
        return new ParentViewHolder(view);
    }

    @Override
    public MainViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new MainViewHolder(view);
    }

    @Override
    public void onBindChildViewHolder(MainViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        NotificationObject object = (NotificationObject) group.getItems().get(childIndex);
        holder.binding.setNoti(object);
    }

    @Override
    public void onBindGroupViewHolder(final ParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.parentBinding.setNoti((NotiTest) group);
    }

    public void updateAppListItem(ArrayList<NotiTest> groups) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotificationGroupDiffCallback(this.groups, groups));

        this.groups.clear();
        this.groups.addAll(groups);

        diffResult.dispatchUpdatesTo(TestAdapter.this);
    }

    class ParentViewHolder extends GroupViewHolder {
        NotificationParentBinding parentBinding;

        ParentViewHolder(View itemView) {
            super(itemView);
            parentBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void expand() {
            animationRotate(true);
        }

        @Override
        public void collapse() {
            animationRotate(false);
        }

        private void animationRotate(boolean onExpand) {
            RotateAnimation rotate = new RotateAnimation(onExpand ? 360 : 180, onExpand ? 180 : 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            parentBinding.arrow.setAnimation(rotate);
        }
    }

    class MainViewHolder extends ChildViewHolder {
        NotificationItemBinding binding;

        MainViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

    }
}
