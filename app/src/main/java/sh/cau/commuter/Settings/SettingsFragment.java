package sh.cau.commuter.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.util.Log;

import sh.cau.commuter.Maps.SearchLocationActivity;
import sh.cau.commuter.PathSetting.PathSettingActivity;
import sh.cau.commuter.R;


/**
 * Created by SH on 2015-11-22.
 */
public class SettingsFragment extends PreferenceFragment
        implements SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {

    Context ctx;
    Preference[] preferences;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        // Create Object
        this.ctx = getActivity().getApplicationContext();

        this.preferences = new Preference[8];
        this.preferences[0] = findPreference("pref_location_departure");
        this.preferences[1] = findPreference("pref_location_arrival");
        this.preferences[2] = findPreference("pref_time_departure");
        this.preferences[3] = findPreference("pref_path_0");
        this.preferences[4] = findPreference("pref_path_1");
        this.preferences[5] = findPreference("pref_path_2");
        this.preferences[6] = findPreference("pref_path_3");
        this.preferences[7] = findPreference("pref_path_4");

        // Register PreferenceChangeListener
        for(int i=0; i<8; i++){
            this.preferences[i].setOnPreferenceClickListener(this);
            this.preferences[i].setOnPreferenceChangeListener(this);
            setOnPreferenceChange(this.preferences[i]);
        }

        PreferenceManager.getDefaultSharedPreferences(ctx).registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onResume() {
        PreferenceManager.getDefaultSharedPreferences(ctx).registerOnSharedPreferenceChangeListener(this);
        super.onResume();
        Log.i("onResume", "called");
    }

    @Override
    public void onPause() {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
        Log.i("onPause", "called");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference pref = findPreference(key);
        Log.i("onSharePref", "called");
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {

        String stringValue = newValue.toString();

        if (preference instanceof EditTextPreference) {
            preference.setSummary(stringValue);
        }

        if (preference != null){
            preference.setSummary(stringValue);
        }

        if (preference instanceof ListPreference) {

            ListPreference listPreference = (ListPreference) preference;
            int index = listPreference.findIndexOfValue(stringValue);

            preference.setSummary(index >= 0 ? listPreference.getEntries()[index] : null);

        }

        return true;
    }

    private void setOnPreferenceChange(Preference mPreference) {
        mPreference.setOnPreferenceChangeListener(this);
        this.onPreferenceChange(mPreference, PreferenceManager.getDefaultSharedPreferences(mPreference.getContext()).getString(mPreference.getKey(), ""));
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {

        String key = preference.getKey();
        Intent i; int pos = 1;

        switch (key) {

            case "pref_location_departure":
                i = new Intent(getActivity(), SearchLocationActivity.class);
                i.putExtra("action", "depart"); startActivity(i);
                return false;

            case "pref_location_arrival":
                i = new Intent(getActivity(), SearchLocationActivity.class);
                i.putExtra("action", "arrive"); startActivity(i);
                return false;

            case "pref_path_0": pos = 0; break;
            case "pref_path_1": pos = 1; break;
            case "pref_path_2": pos = 2; break;
            case "pref_path_3": pos = 3; break;
            case "pref_path_4": pos = 4; break;
        }

        i = new Intent(getActivity(), PathSettingActivity.class);
        i.putExtra("pos", pos+""); startActivity(i);

        Log.i("key", key);

        return false;
    }

}
