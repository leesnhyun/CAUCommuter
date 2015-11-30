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

        this.preferences = new Preference[4];
        this.preferences[0] = findPreference("pref_location_departure");
        this.preferences[1] = findPreference("pref_location_arrival");
        this.preferences[2] = findPreference("pref_time_departure");
        this.preferences[3] = findPreference("pref_path_1");

        // Register PreferenceChangeListener
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        for(int i=0; i<4; i++){
            this.preferences[i].setOnPreferenceClickListener(this);
            setOnPreferenceChange(this.preferences[i]);
        }

    }

    @Override
    public void onResume() {
        getPreferenceManager().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
        super.onResume();
        Log.i("onResume", "called");
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
        Log.i("onPause", "called");
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        Preference pref = findPreference(key);

        if ( pref != null){
            pref.setSummary(sharedPreferences.getString(key, ""));
        }

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
        Intent i;

        switch (key) {

            case "pref_location_departure":
                i = new Intent(getActivity(), SearchLocationActivity.class);
                i.putExtra("action", "depart"); startActivity(i);
                break;

            case "pref_location_arrival":
                i = new Intent(getActivity(), SearchLocationActivity.class);
                i.putExtra("action", "arrive"); startActivity(i);
                break;

        }

        Log.i("key", key);

        return false;
    }

}
