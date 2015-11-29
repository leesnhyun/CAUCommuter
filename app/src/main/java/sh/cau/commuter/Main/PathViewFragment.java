package sh.cau.commuter.Main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sh.cau.commuter.R;

/**
 * Created by SH on 2015-11-29.
 */
public class PathViewFragment extends Fragment {

    SharedPreferences pref;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Shared preferences
        this.pref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // Exception handling
        if( this.pref.getBoolean("pref_arrival", false) && this.pref.getBoolean("pref_departure", false) ) {
            view = inflater.inflate(R.layout.fragment_pathview, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_exception, container, false);
            return view;
        }

        /* Thread 작업 */
        new Thread(new Runnable(){

            @Override
            public void run() {
                Log.i("test", "여기까지옴");
            }

        }).run();

        return view;
    }

}
