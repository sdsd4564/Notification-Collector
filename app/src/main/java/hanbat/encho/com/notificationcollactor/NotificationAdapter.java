package hanbat.encho.com.notificationcollactor;

import android.animation.Animator;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.RotateAnimation;

import com.thoughtbot.expandablerecyclerview.ExpandableRecyclerViewAdapter;
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;
import com.thoughtbot.expandablerecyclerview.viewholders.ChildViewHolder;
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.databinding.NotificationItemBinding;
import hanbat.encho.com.notificationcollactor.databinding.NotificationParentBinding;

class NotificationAdapter extends ExpandableRecyclerViewAdapter<NotificationAdapter.ParentViewHolder, NotificationAdapter.MainViewHolder> {
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
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
    public void onBindChildViewHolder(final MainViewHolder holder, final int flatPosition, final ExpandableGroup group, final int childIndex) {
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
                                if (group.getItemCount() == 1) {

                                }
                                setGroups(Application.getGroupNotifications(db.deleteNotification(object.getPostTime())));
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
    public void onBindGroupViewHolder(final ParentViewHolder holder, final int flatPosition, final ExpandableGroup group) {
        holder.parentBinding.setNoti((NotificationGroup) group);
    }

    class ParentViewHolder extends GroupViewHolder {
        NotificationParentBinding parentBinding;

        ParentViewHolder(View itemView) {
            super(itemView);
            parentBinding = DataBindingUtil.bind(itemView);
        }

        @Override
        public void expand() {
            animationGroupItem(true);
        }

        @Override
        public void collapse() {
            animationGroupItem(false);
        }

        private void animationGroupItem(final boolean onExpand) {
            boolean isItemsEmpty = parentBinding.getNoti().getCount() == 0;
            RotateAnimation rotate = new RotateAnimation(onExpand ? 360 : 180, onExpand ? 180 : 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            parentBinding.arrow.setAnimation(rotate);
            if (isItemsEmpty) {
                parentBinding.noItems.setVisibility(View.VISIBLE);
                parentBinding.noItems.setScaleX(onExpand ? 0 : 1);
                parentBinding.noItems.setScaleY(onExpand ? 0 : 1);
                parentBinding.noItems.animate()
                        .scaleX(onExpand ? 1 : 0)
                        .scaleY(onExpand ? 1 : 0)
                        .setDuration(200)
                        .setInterpolator(INTERPOLATOR)
                        .setListener(new Animator.AnimatorListener() {
                            @Override
                            public void onAnimationStart(Animator animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animator animation) {
                                parentBinding.noItems.setVisibility(onExpand ? View.VISIBLE : View.GONE);
                            }

                            @Override
                            public void onAnimationCancel(Animator animation) {

                            }

                            @Override
                            public void onAnimationRepeat(Animator animation) {

                            }
                        })
                        .start();
            }
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
