package sh.cau.commuter.Settings;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import sh.cau.commuter.R;


/**
 * Created by SH on 2015-11-22.
 */
public class SettingsActivity extends AppCompatActivity {

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

}
