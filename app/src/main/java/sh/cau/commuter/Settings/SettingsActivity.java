package sh.cau.commuter.Settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import sh.cau.commuter.R;


/**
 * Created by SH on 2015-11-22.
 */
public class SettingsActivity extends AppCompatActivity {

    SettingsFragment settingsFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        settingsFragment = new SettingsFragment();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.holder, settingsFragment)
                .commit();

    }

}
