package sh.cau.commuter.PathSetting;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import sh.cau.commuter.Maps.CommuteLocation;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-12-03.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<CommuteLocation> list;
    private OnItemClickListener callback;

    public RecyclerAdapter(Context ctx, List<CommuteLocation> list){
        this.list = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, null);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        CommuteLocation item = list.get(position);

        holder.title.setText(item.getStnName());
        holder.id.setText(item.getStnId());
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public void setOnItemClickListener(final OnItemClickListener l) {
        this.callback = l;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        protected TextView title;
        protected TextView id;

        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView)itemView.findViewById(R.id.recycler_item_title);
            this.id = (TextView)itemView.findViewById(R.id.recycler_item_id);
            this.title.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("item", "clicked"); Log.i("isNull?", callback.toString());
            if(callback != null) callback.onItemClick(v, getPosition());
        }
    }

}
