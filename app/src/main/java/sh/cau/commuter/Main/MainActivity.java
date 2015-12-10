package sh.cau.commuter.Main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import sh.cau.commuter.Model.Constant;
import sh.cau.commuter.R;
import sh.cau.commuter.Settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private PathViewFragment[] pathViewFragments = new PathViewFragment[Constant.TAB_LIMIT_COUNT];

    private SharedPreferences pref;
    private Activity activity = this;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle btnDrawerToggle;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    private TextView tabName;
    private ImageView tabIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* create object */
        this.pref = PreferenceManager.getDefaultSharedPreferences(this);
        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.navigationView = (NavigationView)findViewById(R.id.main_drawer_view);
        this.drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        this.tabLayout = (TabLayout)findViewById(R.id.tab_main);
        this.tabName = (TextView)findViewById(R.id.tab_text);
        this.tabIcon = (ImageView)findViewById(R.id.tab_icon);

    }

    /// Override Functions ///
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        _initToolbar();
        _initTabLayout();

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Set FirstVisit to TRUE
                if( pref.getBoolean("pref_isFirstVisit", true) ) pref.edit().putBoolean("pref_isFirstVisit", false).apply();
            }
        }).start();
    }

    /// Private Functions ///
    private void _initToolbar(){
        /* Set ToolBar as ActionBar  */
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false); //글자제거
        }

        /* 토글버튼 생성 */
        drawerLayout.setDrawerListener(btnDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);

        btnDrawerToggle = new ActionBarDrawerToggle(activity, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        btnDrawerToggle.setDrawerIndicatorEnabled(true);
        btnDrawerToggle.syncState();
    }

    private int _getActivatedTabCount(){

        int i=0;

        for(i=0; i<Constant.TAB_LIMIT_COUNT; i++) {
            String pref_activate = "pref_path_" + i + "_isActivated";
            if( !pref.getBoolean(pref_activate, false) ) break;
            Log.i("pref"+i, "true");
        }

        return i;
    }

    private void _createTab(String pos){
        LinearLayout ctab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab, null);
        ((TextView) ctab.findViewById(R.id.tab_text)).setText( pos );
        tabLayout.addTab(tabLayout.newTab().setCustomView(ctab));
    }

    private void _initTabLayout(){

        int preCount = _getActivatedTabCount();
        if( preCount > 0 ){

            for(int i=0; i<preCount; i++) {
                _createTab((i+1)+"");
            }

        } else {
            _createTab((tabLayout.getTabCount() + 1) + "");

            Bundle arg = new Bundle();
            arg.putInt("pos", 0);

            pathViewFragments[0] = new PathViewFragment();
            pathViewFragments[0].setArguments(arg);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.holder, pathViewFragments[0])
                    .commit();
        }

        tabLayout.addTab(tabLayout.newTab().setText("+").setTag("ADD_TAB"));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                String tag = "";
                if (tab.getTag() != null) tag = tab.getTag().toString();

                switch (tag) {
                    case "ADD_TAB":
                        if (tabLayout.getTabCount() <= Constant.TAB_LIMIT_COUNT) {
                            tabLayout.removeTab(tab);
                            _createTab((tabLayout.getTabCount() + 1) + "");
                            tabLayout.addTab(tabLayout.newTab().setText("+").setTag("ADD_TAB"));
                        } else {
                            Toast.makeText(getApplicationContext(), Constant.TAB_LIMIT_ERROR, Toast.LENGTH_LONG).show();
                        }
                        break;

                    default:
                        Bundle arg = new Bundle();
                        arg.putInt("pos", tab.getPosition());

                        pathViewFragments[tab.getPosition()] = new PathViewFragment();
                        pathViewFragments[tab.getPosition()].setArguments(arg);

                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.holder, pathViewFragments[tab.getPosition()])
                                .commit();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        Intent i;

        switch (id) {

            case R.id.drawer_pref:
                i = new Intent(this, SettingsActivity.class);
                startActivity(i);
                break;

        }

        drawerLayout.closeDrawers();
        menuItem.setChecked(true);

        return true;
    }

}