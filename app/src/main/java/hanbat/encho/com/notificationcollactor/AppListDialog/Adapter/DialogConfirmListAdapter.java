package hanbat.encho.com.notificationcollactor.AppListDialog.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import hanbat.encho.com.notificationcollactor.Model.AppInfo;
import hanbat.encho.com.notificationcollactor.R;
import hanbat.encho.com.notificationcollactor.databinding.DialogAllowedItemBinding;

/**
 * Created by Encho on 2017-08-10.
 */

public class DialogConfirmListAdapter extends RecyclerView.Adapter<DialogConfirmListAdapter.DialogViewHolder> {

    private ArrayList<AppInfo> confirmApps;
    private Context mContext;

    public DialogConfirmListAdapter(ArrayList<AppInfo> confirmApps, Context mContext) {
        this.confirmApps = confirmApps;
        this.mContext = mContext;
    }

    @Override
    public DialogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dialog_allowed_item, parent, false);
        return new DialogViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DialogViewHolder holder, int position) {
        AppInfo app = confirmApps.get(position);
        holder.binding.setApp(app);
    }

    @Override
    public int getItemCount() {
        return confirmApps == null ? 0 : confirmApps.size();
    }

    class DialogViewHolder extends RecyclerView.ViewHolder {
        DialogAllowedItemBinding binding;

        public DialogViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }
}
