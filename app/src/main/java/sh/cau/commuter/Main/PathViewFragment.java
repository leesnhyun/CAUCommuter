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
import android.widget.ImageView;
import android.widget.Toast;

import sh.cau.commuter.R;
import sh.cau.commuter.Settings.SettingsActivity;

/**
 * Created by SH on 2015-11-29.
 */
public class PathViewFragment extends Fragment {

    private SharedPreferences pref;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Shared preferences
        this.pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Exception handling
        if( this.pref.getBoolean("pref_location_depart_set", false) && this.pref.getBoolean("pref_location_arrival_set", false) ) {
            view = inflater.inflate(R.layout.fragment_pathview, container, false);

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

}