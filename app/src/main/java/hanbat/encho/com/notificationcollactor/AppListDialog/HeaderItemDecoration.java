package hanbat.encho.com.notificationcollactor.AppListDialog;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HeaderItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        View topView = parent.getChildAt(0);

        int topViewPosition = parent.getChildAdapterPosition(topView);

//        View currentHeader =
    }
}
