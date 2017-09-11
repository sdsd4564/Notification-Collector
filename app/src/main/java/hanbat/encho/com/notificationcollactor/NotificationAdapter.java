package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.listeners.OnGroupClickListener;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.DiffCallback.NotificationGroupItemDiffCallback;
import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.databinding.NotificationItemBinding;
import hanbat.encho.com.notificationcollactor.databinding.NotificationParentBinding;

class NotificationAdapter extends ExpandableRecyclerViewAdapter<NotificationAdapter.ParentViewHolder, NotificationAdapter.MainViewHolder> {
    private ArrayList<NotificationGroup> groups;
    private Context mContext;
    private DBhelper db;

    NotificationAdapter(ArrayList<NotificationGroup> groups, Context mContext) {
        super(groups);
        this.groups = groups;
        this.mContext = mContext;
        db = DBhelper.getInstance();
    }

    void setGroups(ArrayList<NotificationGroup> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        notifyDataSetChanged();
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
        final NotificationObject object = (NotificationObject) group.getItems().get(childIndex);
        holder.binding.setNoti(object);
        holder.binding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext)
                        .setTitle(R.string.alert_message_delete);

                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ArrayList<NotificationObject> arr = new ArrayList<NotificationObject>();
                                for (NotificationObject obj : db.deleteNotification(object.getPostTime())) {
                                    if (obj.getPackageName().equals(object.getPackageName())) {
                                        arr.add(obj);
                                    }
                                }
                                if (arr.size() != 0) {
                                    NotificationGroup notificationGroup =
                                            new NotificationGroup(group.getTitle(), ((NotificationGroup) group).getPackageName(), arr);
                                    refreshGroup(notificationGroup);
                                } else {
                                    toggleGroup(group);
                                    NotificationGroup temp = (NotificationGroup) group;
                                    notifyItemRemoved(groups.indexOf(temp));
                                    groups.remove(temp);
                                }
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
    public void onBindGroupViewHolder(final ParentViewHolder holder, int flatPosition, final ExpandableGroup group) {
        holder.parentBinding.setNoti((NotificationGroup) group);
        this.setOnGroupClickListener(new OnGroupClickListener() {
            @Override
            public boolean onGroupClick(int flatPos) {
                if (!isGroupExpanded(flatPos) && group.getItemCount() == 0) {
//                    holder.parentBinding.noItems.setVisibility(View.VISIBLE);
                    animationFade(holder.parentBinding.noItems, true);
                } else {
//                    holder.parentBinding.noItems.setVisibility(View.GONE);
                    animationFade(holder.parentBinding.noItems, false);
                }
                return false;
            }
        });
    }

    private void animationFade(View view, boolean onFade) {
        view.setVisibility(onFade ? View.VISIBLE : View.GONE);
        Animation animation = new AlphaAnimation(onFade ? 0 : 1, onFade ? 1 : 0);
        animation.setDuration(300);
        view.setAnimation(animation);
    }

    private void refreshGroup(NotificationGroup newGroup) {
        for (NotificationGroup obj : groups) {
            if (newGroup.getPackageName().equals(obj.getPackageName())) {
                DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new NotificationGroupItemDiffCallback(obj, newGroup));

                obj.getItems().clear();
                obj.getItems().addAll(newGroup.getItems());

                diffResult.dispatchUpdatesTo(NotificationAdapter.this);
                break;
            }
        }
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
