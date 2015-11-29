package sh.cau.commuter.Main;

import android.content.Intent;
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
        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.navigationView = (NavigationView)findViewById(R.id.main_drawer_view);
        this.drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        this.tabLayout = (TabLayout)findViewById(R.id.tab_main);
        this.tabName = (TextView)findViewById(R.id.tab_text);
        this.tabIcon = (ImageView)findViewById(R.id.tab_icon);

        new Thread(new Runnable() {
            @Override
            public void run() {

                // Toolbar & TabLayout setup
                _initToolbar();
                _initTabLayout();

            }
        }).run();

    }

    /// Private Functions ///
    private void _initToolbar(){
        /* Set ToolBar as ActionBar  */
        if(toolbar != null){
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false); //글자제거
        }

        /* 토글버튼 생성 */
        this.btnDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        btnDrawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.setDrawerListener(btnDrawerToggle);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void _initTabLayout(){

        LinearLayout ctab = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.tab, null);
        ((TextView)ctab.findViewById(R.id.tab_text)).setText((tabLayout.getTabCount() + 1)+"");

        this.tabLayout.addTab(tabLayout.newTab().setCustomView(ctab));
        this.getSupportFragmentManager().beginTransaction().replace(R.id.holder, new PathViewFragment()).commit();
        this.tabLayout.addTab(tabLayout.newTab().setText("+").setTag("ADD_TAB"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                String tag = "";
                if(tab.getTag() != null) tag = tab.getTag().toString();

                switch (tag) {
                    case "ADD_TAB":
                        if (tabLayout.getTabCount() <= Constant.TAB_LIMIT_COUNT) {
                            tabLayout.removeTab(tab);

                            LinearLayout ctab = (LinearLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.tab, null);
                            ((TextView)ctab.findViewById(R.id.tab_text)).setText((tabLayout.getTabCount() + 1) + "");

                            tabLayout.addTab(tabLayout.newTab().setCustomView(ctab));
                            tabLayout.addTab(tabLayout.newTab().setText("+").setTag("ADD_TAB"));
                        } else {
                            Toast.makeText(getApplicationContext(), Constant.TAB_LIMIT_ERROR, Toast.LENGTH_LONG).show();
                        }
                        break;

                    default :
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.holder, new PathViewFragment())
                                .commit();
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }

            @Override
            public void onTabReselected(TabLayout.Tab tab) { }
        });

    }

    /// Override Functions ///
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        btnDrawerToggle.syncState();
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