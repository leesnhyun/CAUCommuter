package sh.cau.commuter.Main;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;
import sh.cau.commuter.R;

/**
 * Created by SH on 2015-12-03.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<String> list;
    private List<String> times;

    public RecyclerAdapter(Context ctx, List<String> list, List<String> time){
        this.list = list;
        this.times = time;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.important_path_item, null);
        v.setMinimumHeight(300);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.stopName.setText(list.get(position));
        holder.arrivalTime.setText(times.get(position));
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        protected TextView stopName;
        protected TextView arrivalTime;

        public ViewHolder(View itemView) {
            super(itemView);
            this.stopName = (TextView)itemView.findViewById(R.id.location_name);
            this.arrivalTime = (TextView)itemView.findViewById(R.id.timepicker);

            this.arrivalTime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }

    }

}
