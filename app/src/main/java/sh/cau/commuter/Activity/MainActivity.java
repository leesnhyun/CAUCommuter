package sh.cau.commuter.Activity;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import sh.cau.commuter.R;
import sh.cau.commuter.Settings.SettingsActivity;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle btnDrawerToggle;
    private NavigationView navigationView;
    private TabLayout tabLayout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* create object */
        this.toolbar = (Toolbar)findViewById(R.id.toolbar);
        this.navigationView = (NavigationView)findViewById(R.id.main_drawer_view);
        this.drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        this.tabLayout = (TabLayout)findViewById(R.id.tab_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                // Toolbar setup
                _initToolbar();

                // TabLayout setup
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

        this.tabLayout.addTab(tabLayout.newTab().setText("경로1"));
        this.tabLayout.addTab(tabLayout.newTab().setText("+"));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                String name = tab.getText().toString();
                switch(name){
                    case "+" :
                        tabLayout.addTab(tabLayout.newTab().setText("test"));
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
}
