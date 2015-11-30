package sh.cau.commuter.Settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import sh.cau.commuter.R;


/**
 * Created by SH on 2015-11-22.
 */
public class SettingsActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SettingsFragment settingsFragment;
    private Toolbar toolbar;
    private TextView title;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        settingsFragment = new SettingsFragment();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        /* Connect with View */
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.title = (TextView)findViewById(R.id.toolbar_name);

        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            // Title
            title.setText(getString(R.string.pref_setting));

            // Action
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.holder, settingsFragment)
                .commit();

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.i("pref", "called");
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
