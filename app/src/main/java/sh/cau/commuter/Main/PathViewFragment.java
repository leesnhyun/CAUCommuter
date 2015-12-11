package sh.cau.commuter.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

import sh.cau.commuter.Model.Constant;
import sh.cau.commuter.Model.Database;
import sh.cau.commuter.PathSetting.*;
import sh.cau.commuter.R;
import sh.cau.commuter.Settings.SettingsActivity;

/**
 * Created by SH on 2015-11-29.
 */
public class PathViewFragment extends Fragment {

    private SharedPreferences pref;
    private ArrayList<Transport> path;
    private ArrayList<Calendar> times;
    private ArrayList<String> list = new ArrayList<>();

    private Calendar today;

    private View view;
    private int pos;

    private sh.cau.commuter.Main.RecyclerAdapter adapter;
    private RecyclerView recyclerView;

    private Database DBhelper;
    private SQLiteDatabase db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Shared preferences
        this.pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        this.path = new ArrayList<>();
        this.DBhelper = new Database(getActivity().getApplicationContext(), Constant.DB_FILE, null, 1);
        this.db = DBhelper.getReadableDatabase();
        this.times = new ArrayList<>();
        this.today = Calendar.getInstance();

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
                _getPaths();

                times.add(today);
                Log.i(">>", today.get(Calendar.HOUR) + ":" + today.get(Calendar.MINUTE));

                view = inflater.inflate(R.layout.fragment_pathview, container, false);
                this.recyclerView = (RecyclerView)view.findViewById(R.id.point_holder);
                _attachTransferPoint();

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
                                        today.add(Calendar.MINUTE, elapsed);
                                        times.add( today );
                                        today.add(Calendar.MINUTE, elapsed);
                                        times.add( today );

                                        ArrayList<String> stringTime = new ArrayList<String>();


                                        adapter = new sh.cau.commuter.Main.RecyclerAdapter(getActivity().getApplicationContext(), list, list);
                                        recyclerView.setAdapter(adapter);
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

    private void _attachTransferPoint(){
        ArrayList<String> times = new ArrayList<>();

        for(int i=0; i<path.size(); i++){
            if( path.get(i).getMethod().equals("foot") ) continue;

            list.add(path.get(i).getStart());
            list.add(path.get(i).getDest());
        }

        Log.i("test", list.size()+"");

        LinearLayoutManager llm = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(llm);
        recyclerView.setEnabled(false);

        /*this.adapter = new sh.cau.commuter.Main.RecyclerAdapter(getActivity().getApplicationContext(), list, times);
        recyclerView.setAdapter(adapter);*/
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