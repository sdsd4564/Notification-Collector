package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.DiffCallback.NotiTestDiffCallback;
import hanbat.encho.com.notificationcollactor.DiffCallback.NotificationGroupDiffCallback;
import hanbat.encho.com.notificationcollactor.Model.NotiTest;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.databinding.NotificationItemBinding;
import hanbat.encho.com.notificationcollactor.databinding.NotificationParentBinding;


/**
 * Created by Encho on 2017-08-23.
 */

class TestAdapter extends ExpandableRecyclerViewAdapter<TestAdapter.ParentViewHolder, TestAdapter.MainViewHolder> {
    private ArrayList<NotiTest> groups;
    private Context mContext;
    private DBhelper db;

    TestAdapter(ArrayList<NotiTest> groups, Context mContext) {
        super(groups);
        this.groups = groups;
        this.mContext = mContext;
        db = DBhelper.getInstance();
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
    public void onBindChildViewHolder(final MainViewHolder holder, int flatPosition, final ExpandableGroup group, final int childIndex) {
        final int couint = group.getItemCount();
        final NotificationObject object = (NotificationObject) group.getItems().get(childIndex);
        holder.binding.setNoti(object);
        holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext)
                        .setMessage(R.string.alert_message_delete);

                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                db.deleteNotification(object.getPostTime());
                            }
                        });
                builder.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                builder.show();
                return false;
            }
        });
    }

    @Override
    public void onBindGroupViewHolder(final ParentViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.parentBinding.setNoti((NotiTest) group);
    }

    void updateGroupItem(ArrayList<NotiTest> groups) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotificationGroupDiffCallback(this.groups, groups));

        this.groups.clear();
        this.groups.addAll(groups);

        diffResult.dispatchUpdatesTo(TestAdapter.this);
    }

    void refreshGroup(NotiTest oldGroup, NotiTest newGroup) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotiTestDiffCallback(oldGroup, newGroup));

        oldGroup.getItems().clear();
        oldGroup.getItems().addAll(newGroup.getItems());

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
