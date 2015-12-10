package sh.cau.commuter.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import sh.cau.commuter.Maps.SubwayStation;
import sh.cau.commuter.Model.Constant;
import sh.cau.commuter.Model.Database;
import sh.cau.commuter.PathSetting.PathSettingActivity;
import sh.cau.commuter.PathSetting.Transport;
import sh.cau.commuter.R;
import sh.cau.commuter.Settings.SettingsActivity;

/**
 * Created by SH on 2015-11-29.
 */
public class PathViewFragment extends Fragment {

    private SharedPreferences pref;
    private ArrayList<Transport> path;
    private View view;
    private int pos;

    private Database DBhelper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Shared preferences
        this.pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.path = new ArrayList<>();
        this.DBhelper = new Database(getActivity().getApplicationContext(), Constant.DB_FILE, null, 1);
        this.db = DBhelper.getReadableDatabase();

        // Get Bundles
        Bundle getPos = this.getArguments();
        if( getPos != null ) this.pos = getPos.getInt("pos");

        // Exception handling
        if( this.pref.getBoolean("pref_location_depart_set", false) && this.pref.getBoolean("pref_location_arrival_set", false) ) {

            String pref_activate = "pref_path_" + this.pos + "_isActivated";
            Log.i("pref_path", pref_activate);

            if( !this.pref.getBoolean(pref_activate, false) ) {
                view = inflater.inflate(R.layout.fragment_no_path, container, false);
                (view.findViewById(R.id.txt_no_path)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), PathSettingActivity.class);
                        i.putExtra("pos", pos + ""); startActivity(i);
                    }
                });
            } else {
                view = inflater.inflate(R.layout.fragment_pathview, container, false);

                _getPaths();
                _attachTransferPoint(view);

                String temp = pref.getString("path_" + this.pos, "");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for(int i=0; i<path.size(); i++) {
                            if (path.get(i).getMethod().equals("bus")) {
                                double[] coord = _getCord(path.get(i));
                                if (coord == null) continue;
                                BusPathParser bpp = new BusPathParser();
                                bpp.setOnCallbackListener(new OnPathCallBack() {
                                    @Override
                                    public void success(int elapsed, int distance) {
                                        Log.i("걸리는 시간(거리)", elapsed+","+distance);
                                    }

                                    @Override
                                    public void fail() {

                                    }
                                });
                                bpp.execute(coord[0] + "", coord[1] + "", coord[2] + "", coord[3] + "", path.get(i).getLine());
                            }
                        }

                    }
                }).start();

            }

        } else {
            view = inflater.inflate(R.layout.fragment_exception, container, false);
            (view.findViewById(R.id.txt_no_path)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity(), SettingsActivity.class);
                    startActivity(i);
                }
            });
        }

        return view;
    }

    private void _getPaths(){

        String temp = pref.getString("path_" + this.pos, "");

        String[] node = temp.split("##");
        String[] subNode;

        for(int i=0; i<node.length; i++){
            subNode = node[i].split(";");

            if( subNode[0].equals("bus") || subNode[0].equals("subway") ) {
                path.add(new Transport(subNode[0], subNode[1], subNode[2], subNode[3]));
            } else {
                path.add(new Transport(subNode[0], subNode[1]));
            }
        }

    }

    private double[] _getCord(Transport t) {
        double[] cord = new double[4];

        if (t.getStart() == null || t.getDest() == null){
            Log.i("null", "Exist");
            return null;
        }

        String r1 = "select gpsX, gpsY from stops where stnName = '"+t.getStart()+"'";
        String r2 = "select gpsX, gpsY from stops where stnName = '"+t.getDest()+"'";

        Cursor result = db.rawQuery(r1, null);

        // 시작주소
        if(result.moveToFirst()){
            cord[0] = Double.valueOf(result.getString(0));
            cord[1] = Double.valueOf(result.getString(1));
        }
        result.close();

        result = db.rawQuery(r2, null);

        // 도착좌표
        if(result.moveToFirst()){
            cord[2] = Double.valueOf(result.getString(0));
            cord[3] = Double.valueOf(result.getString(1));
        }
        result.close();

        return cord;
    }

    private void _attachTransferPoint(View v){

        final LinearLayout holder = (LinearLayout)v.findViewById(R.id.point_holder);

        for(int i=0; i<path.size(); i++){

            if(path.get(i).getMethod().equals("foot")) continue;

            String start = path.get(i).getStart();

            Log.i("path_start", start);

            View vpath = getActivity().getLayoutInflater().inflate(R.layout.important_path_item, null);
            ((TextView)vpath.findViewById(R.id.location_name)).setText(path.get(i).getStart());

            holder.addView(vpath);

            View vpath2 = getActivity().getLayoutInflater().inflate(R.layout.important_path_item, null);
            ((TextView)vpath2.findViewById(R.id.location_name)).setText(path.get(i).getDest());

            holder.addView(vpath2);
        }

    }

    @Override
    public void onResume() {
        // Shared preferences
        this.pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        String pathStr = "path_"+this.pos;
        Log.i("main_pref", pref.getString(pathStr, ""));

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.holder, this)
                .commit();

        super.onResume();
    }
}