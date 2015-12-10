package sh.cau.commuter.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sh.cau.commuter.PathSetting.PathSettingActivity;
import sh.cau.commuter.R;
import sh.cau.commuter.Settings.SettingsActivity;

/**
 * Created by SH on 2015-11-29.
 */
public class PathViewFragment extends Fragment {

    private SharedPreferences pref;
    private View view;
    private int pos;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Shared preferences
        this.pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

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

            return view;
        }

        return view;
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