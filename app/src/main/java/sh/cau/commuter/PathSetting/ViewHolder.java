package sh.cau.commuter.PathSetting;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import sh.cau.commuter.R;

/**
 * Created by SH on 2015-12-03.
 */

public class ViewHolder extends RecyclerView.ViewHolder {

    protected TextView title;
    protected TextView id;

    public ViewHolder(View itemView) {
        super(itemView);
        this.title = (TextView)itemView.findViewById(R.id.recycler_item_title);
        this.id = (TextView)itemView.findViewById(R.id.recycler_item_id);
    }

}
