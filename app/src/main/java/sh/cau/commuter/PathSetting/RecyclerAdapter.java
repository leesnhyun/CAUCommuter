package sh.cau.commuter.PathSetting;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import sh.cau.commuter.Maps.CommuteLocation;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-12-03.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<ViewHolder> {

    private List<CommuteLocation> list;
    private Context ctx;

    public RecyclerAdapter(Context ctx, List<CommuteLocation> list){
        this.list = list;
        this.ctx = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        CommuteLocation item = list.get(position);

        holder.title.setText(item.getStnName());
        holder.id.setText(item.getStnId());

        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }
}
