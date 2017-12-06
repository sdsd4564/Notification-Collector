package hanbat.encho.com.notificationcollactor;

import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.NotificationGroup;
import hanbat.encho.com.notificationcollactor.Model.NotificationObject;
import hanbat.encho.com.notificationcollactor.databinding.ActivityMainBinding;
import hanbat.encho.com.notificationcollactor.databinding.NotificationGroupBinding;
import hanbat.encho.com.notificationcollactor.databinding.NotificationItemBinding;


public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.GroupViewHolder> {
    private final int VIEW_ITEM = 1;
    private final int VIEW_LOAD = 0;
    private ArrayList<NotificationGroup> groups;
    private Context mContext;
    private ActivityMainBinding binding;
    private DBhelper db;
    private TestChildAdapter mAdapter;

    public NotificationAdapter(ArrayList<NotificationGroup> groups, Context mContext, ActivityMainBinding binding) {
        this.groups = groups;
        this.mContext = mContext;
        this.binding = binding;
        db = DBhelper.getInstance();
    }

    public void setGroups(ArrayList<NotificationGroup> groups) {
        this.groups.clear();
        this.groups.addAll(groups);
        for (NotificationGroup obj : this.groups)
            obj.setExpand(false);
        notifyDataSetChanged();
    }


    @Override
    public NotificationAdapter.GroupViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_group, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final NotificationAdapter.GroupViewHolder holder, int i) {
        final NotificationGroup group = groups.get(i);
        holder.groupBinding.setNoti(group);

        final LinearLayoutManager mManager = new LinearLayoutManager(mContext);

        if (group.isExpand()) {
            mAdapter = new TestChildAdapter(group, holder.groupBinding);
            mAdapter.setHasStableIds(true);
            holder.groupBinding.childListView.setLayoutManager(mManager);
            holder.groupBinding.childListView.setAdapter(mAdapter);
        } else
            mAdapter = null;

        holder.groupBinding.childListView.setVisibility(group.isExpand() ? View.VISIBLE : View.GONE);
        animationGroupItem(group.isExpand(), holder.groupBinding.arrow);
        holder.groupBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notifyItemChanged(holder.getAdapterPosition());
                group.setExpand(!group.isExpand());
            }
        });

        holder.groupBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext)
                        .setTitle(group.getAppName())
                        .setMessage(R.string.alert_message_delete);
                builder.setPositiveButton("삭제",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setGroups(db.deleteGroupNotification(group.getPackageName()));
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
        binding.noItem.setVisibility(groups.isEmpty() ? View.VISIBLE : View.GONE);
        return groups == null ? 0 : groups.size();
    }

    @Override
    public long getItemId(int position) {
        NotificationGroup forId = groups.get(position);
        return (forId.getPackageName()).hashCode();
    }

    private void animationGroupItem(boolean onExpand, View arrow) {
        RotateAnimation rotate =
                new RotateAnimation(onExpand ? 0 : 180, onExpand ? 180 : 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setDuration(300);
        rotate.setFillAfter(true);
        arrow.setAnimation(rotate);
    }

    private class TestChildAdapter extends RecyclerView.Adapter {
        NotificationGroup group;
        NotificationGroupBinding parentBinding;

        TestChildAdapter(NotificationGroup group, NotificationGroupBinding parentBinding) {
            this.group = group;
            this.parentBinding = parentBinding;
        }

        @Override
        public long getItemId(int position) {
            return group.getItems().get(position) != null ? group.getItems().get(position).getPostTime() : 0;
        }

        @Override
        public int getItemViewType(int position) {
            return group.getItems().get(position) != null ? VIEW_ITEM : VIEW_LOAD;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view;
            if (i == VIEW_ITEM) {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_item, viewGroup, false);
                return new ChildViewHolder(view);
            } else {
                view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.load_more_item, viewGroup, false);
                return new LoadingHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
            if (holder instanceof ChildViewHolder) {
                final NotificationObject item = group.getItems().get(position);
                ((ChildViewHolder) holder).childBinding.setNoti(item);
                ((ChildViewHolder) holder).childBinding.getRoot().setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext)
                                .setTitle(R.string.alert_message_delete);

                        builder.setPositiveButton("삭제",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        ArrayList<NotificationGroup> list = db.deleteNotification(item.getPackageName(), item.getPostTime());
                                        if (TestChildAdapter.this.getItemCount() == 1) {
                                            setGroups(list);
                                        } else {
                                            group.getItems().remove(holder.getAdapterPosition());
                                            TestChildAdapter.this.notifyItemRemoved(holder.getAdapterPosition());
                                            parentBinding.notificationCount.setText(String.valueOf(TestChildAdapter.this.getItemCount()));
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
            } else {
                ((LoadingHolder) holder).mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int beforeLoadedSize = group.getItems().size() - 1;
                        group.getItems().addAll(db.getNotification(group.getPackageName(), beforeLoadedSize));
                        group.getItems().remove(beforeLoadedSize);
                        if (group.getCount() != group.getItems().size())
                            group.getItems().add(null);
                        notifyDataSetChanged();
                    }
                });
            }
        }

        @Override
        public int getItemCount() {
            return group != null ? group.getItems().size() : 0;
        }
    }

    class GroupViewHolder extends RecyclerView.ViewHolder {
        NotificationGroupBinding groupBinding;

        GroupViewHolder(View itemView) {
            super(itemView);
            groupBinding = DataBindingUtil.bind(itemView);
        }
    }

    private class ChildViewHolder extends RecyclerView.ViewHolder {
        NotificationItemBinding childBinding;

        ChildViewHolder(View itemView) {
            super(itemView);
            childBinding = DataBindingUtil.bind(itemView);
        }
    }

    private class LoadingHolder extends RecyclerView.ViewHolder {
        View mView;
        ImageView loadingImage;

        LoadingHolder(View itemView) {
            super(itemView);
            mView = itemView;
            loadingImage = mView.findViewById(R.id.load_item);
        }
    }
}


