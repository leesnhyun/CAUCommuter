package sh.cau.commuter.Settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import sh.cau.commuter.R;


/**
 * Created by SH on 2015-11-22.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    Context ctx;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        this.ctx = getActivity().getApplicationContext();

        // Register PreferenceChangeListener
        //getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

}
