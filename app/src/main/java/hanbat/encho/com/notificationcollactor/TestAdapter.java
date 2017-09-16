package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.DiffCallback.TestGroupDiffCallback;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.Model.TestGroup;
import hanbat.encho.com.notificationcollactor.databinding.NotificationItemBinding;
import hanbat.encho.com.notificationcollactor.databinding.TestGroupBinding;

/**
 * Created by Encho on 2017-09-15.
 */

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.GroupViewHolder> {
    private ArrayList<TestGroup> groups;
    private Context mContext;
    private DBhelper db;

    public TestAdapter(ArrayList<TestGroup> groups, Context mContext) {
        this.groups = groups;
        this.mContext = mContext;
        db = DBhelper.getInstance();
    }

    public void setGroups(ArrayList<TestGroup> groups) {
        for (TestGroup obj : this.groups) obj.setExpand(false);
        this.groups.clear();
        this.groups.addAll(groups);
        notifyDataSetChanged();
//        updateList(groups);
    }

    @Override
    public long getItemId(int position) {
        TestGroup forId = groups.get(position);
        return (forId.getPackageName()).hashCode();
    }

    @Override
    public TestAdapter.GroupViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.test_group, parent, false);
        return new GroupViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final TestAdapter.GroupViewHolder holder, final int i) {
        final TestGroup group = groups.get(i);
        holder.groupBinding.setNoti(group);

        TestChildAdapter mAdapter = new TestChildAdapter(group);
        mAdapter.setHasStableIds(true);

        holder.groupBinding.childListView.setLayoutManager(new LinearLayoutManager(mContext));
        holder.groupBinding.childListView.setAdapter(group.isExpand() ? mAdapter : null);
//        holder.groupBinding.childListView.setVisibility(group.isExpand() ? View.VISIBLE : View.INVISIBLE);
        animationGroupItem(group.isExpand(), holder.groupBinding.arrow);
        holder.groupBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(i);
                group.setExpand(!group.isExpand());
            }
        });
        holder.groupBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext)
                        .setTitle(group.getAppName() + " " + mContext.getResources().getText(R.string.alert_message_delete));
                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateList(Application.getTestGroups(db.deleteGroupNotification(group.getPackageName())));
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
    public int getItemCount() {
        return groups == null ? 0 : groups.size();
    }

    public void updateList(ArrayList<TestGroup> groups) {
        final TestGroupDiffCallback diffCallback = new TestGroupDiffCallback(this.groups, groups);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.groups.clear();
        this.groups.addAll(groups);

        diffResult.dispatchUpdatesTo(this);
    }

    private void animationGroupItem(boolean onExpand, View arrow) {
        RotateAnimation rotate =
                new RotateAnimation(onExpand ? 360 : 180, onExpand ? 180 : 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    class TestChildAdapter extends RecyclerView.Adapter<ChildViewHolder> {
        TestGroup group;

        public TestChildAdapter(TestGroup group) {
            this.group = group;
        }

        public void setGroup(TestGroup group) {
            this.group = group;
            this.group.setExpand(false);
            TestChildAdapter.this.notifyDataSetChanged();
        }

        @Override
        public long getItemId(int position) {
            return group.getItems().get(position).getPostTime();
        }

        @Override
        public ChildViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);
            return new ChildViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ChildViewHolder childViewHolder, int i) {
            final NotificationObject item = group.getItems().get(i);
            childViewHolder.childBinding.setNoti(item);
            childViewHolder.childBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext)
                            .setTitle(R.string.alert_message_delete);

                    builder.setPositiveButton("삭제",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ArrayList<TestGroup> list = Application.getTestGroups(db.deleteNotification(item.getPackageName(), item.getPostTime()));
                                    if (list.size() == groups.size()) {
                                        for (TestGroup obj : list) {
                                            if (obj.getPackageName().equals(group.getPackageName())) {
                                                setGroup(obj);
                                                break;
                                            }
                                        }
                                    } else {
                                        updateList(list);
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
        public int getItemCount() {
            return group.getCount();
        }
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        TestGroupBinding groupBinding;

        public GroupViewHolder(View itemView) {
            super(itemView);
            groupBinding = DataBindingUtil.bind(itemView);
        }
    }

    class ChildViewHolder extends RecyclerView.ViewHolder {
        NotificationItemBinding childBinding;

        public ChildViewHolder(View itemView) {
            super(itemView);
            childBinding = DataBindingUtil.bind(itemView);
        }
    }
}

